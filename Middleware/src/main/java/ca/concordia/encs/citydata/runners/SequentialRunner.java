package ca.concordia.encs.citydata.runners;

import java.lang.reflect.Method;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import ca.concordia.encs.citydata.core.AbstractRunner;
import ca.concordia.encs.citydata.core.IDataStore;
import ca.concordia.encs.citydata.core.IOperation;
import ca.concordia.encs.citydata.core.IProducer;
import ca.concordia.encs.citydata.core.IRunner;
import ca.concordia.encs.citydata.datastores.InMemoryDataStore;

/**
 *
 * This Runner starts with data provided by a producer P1, then applies
 * operations in order based on P1' (P1 prime). For example: P1 + O1 = P1'. P1'
 * + O2 -> P1'', etc.
 * 
 */
public class SequentialRunner extends AbstractRunner implements IRunner {

	private JsonObject steps = null;
	private int operationCounter = 0;

	public SequentialRunner(JsonObject steps) {
		this.steps = steps;
	}

	// REFLECTION METHODS
	private JsonElement getRequiredField(JsonObject jsonObject, String fieldName) {
		if (!jsonObject.has(fieldName)) {
			throw new IllegalArgumentException("Error: Missing '" + fieldName + "' field");
		}
		return jsonObject.get(fieldName);
	}

	private Object instantiateClass(String className) throws Exception {
		Class<?> clazz = Class.forName(className);
		return clazz.getDeclaredConstructor().newInstance();
	}

	private void setParameters(Object instance, JsonArray params) throws Exception {
		Class<?> clazz = instance.getClass();
		for (JsonElement paramElement : params) {
			JsonObject paramObject = paramElement.getAsJsonObject();
			String paramName = paramObject.get("name").getAsString();
			JsonElement paramValue = paramObject.get("value");
			Method setter = findSetterMethod(clazz, paramName, paramValue);
			setter.invoke(instance, convertValue(setter.getParameterTypes()[0], paramValue));
		}
	}

	private Method findSetterMethod(Class<?> clazz, String paramName, JsonElement paramValue)
			throws NoSuchMethodException {
		String methodName = "set" + capitalize(paramName);
		for (Method method : clazz.getMethods()) {
			if (method.getName().equals(methodName) && method.getParameterCount() == 1) {
				return method;
			}
		}
		throw new NoSuchMethodException("No suitable setter found for " + paramName);
	}

	private Object convertValue(Class<?> targetType, JsonElement value) {
		if (targetType == int.class || targetType == Integer.class) {
			return value.getAsInt();
		} else if (targetType == boolean.class || targetType == Boolean.class) {
			return value.getAsBoolean();
		} else if (targetType == double.class || targetType == Double.class) {
			return value.getAsDouble();
		} else if (targetType == JsonObject.class) {
			return value.getAsJsonObject();
		} else if (targetType == JsonArray.class) {
			return value.getAsJsonArray();
		}
		return value.getAsString();
	}

	private String capitalize(String str) {
		return str == null || str.isEmpty() ? str : str.substring(0, 1).toUpperCase() + str.substring(1);
	}

	// RUNNER METHODS
	@Override
	public void runSteps() throws Exception {
		// if there are no steps to run, warn the user and stop
		if (this.steps == null) {
			this.setAsDone();
			throw new RuntimeException("No steps to run! Please provide steps so the runner can execute them.");
		}

		// start by extracting Producers, Operations and their params from the query
		System.out.println("Run started!");
		String producerName = getRequiredField(this.steps, "use").getAsString();
		JsonArray producerParams = getRequiredField(this.steps, "withParams").getAsJsonArray();

		// instantiate a new Producer instance and set its params
		Object producerInstance = instantiateClass(producerName);
		setParameters(producerInstance, producerParams);

		// add this Runner as an observer of the Producer instance
		Method addObserverMethod = producerInstance.getClass().getMethod("addObserver", IRunner.class);
		addObserverMethod.invoke(producerInstance, this);

		// if there are operations, apply the first one
		// subsequent operation will be applied on P1' once the first is done
		this.applyNextOperation((IProducer<?>) producerInstance);

	}

	@Override
	public void applyNextOperation(IProducer<?> producer) throws Exception {
		/*
		 * get list of operations and choose which one to execute next based on the
		 * sequential operation counter
		 */
		JsonArray operationsToApply = getRequiredField(this.steps, "apply").getAsJsonArray();
		int totalOperations = operationsToApply.size();
		if (producer != null && totalOperations > 0) {

			JsonObject currentOperation = operationsToApply.get(this.operationCounter).getAsJsonObject();

			// instantiate current operation
			JsonObject operationNode = currentOperation.getAsJsonObject();
			String operationName = getRequiredField(operationNode, "name").getAsString();
			Object operationInstance = instantiateClass(operationName);

			// extract operation parameters and set them
			JsonArray operationParams = getRequiredField(operationNode, "withParams").getAsJsonArray();
			setParameters(operationInstance, operationParams);

			// set operation to producer
			Method setOperationMethod = producer.getClass().getMethod("setOperation", IOperation.class);

			setOperationMethod.invoke(producer, operationInstance);

			// trigger data fetching, which will in turn apply the operation
			System.out.println("Applying operation " + (this.operationCounter + 1) + " out of " + totalOperations);
		} else {
			System.out.println("No operations to apply");
		}

		producer.fetch();

	}

	@Override
	public void newDataAvailable(IProducer<?> producer) throws Exception {

		// congratulations, you are done with your operation, go to the next one
		this.operationCounter += 1;

		// but is there really a next one? if not, stop
		JsonArray operationsToApply = getRequiredField(this.steps, "apply").getAsJsonArray();
		if (this.operationCounter >= operationsToApply.size()) {
			this.storeResults(producer);
			this.setAsDone();
			System.out.println("Run completed!");
		} else {
			// if there are operations to be applied, apply the first one
			// subsequent operations will be applied on the P1' once the first is done
			this.applyNextOperation(producer);
		}

	}

	@Override
	public void newOperationApplied(IOperation<?> operation) {
		// this is mostly for debugging purposes, it could be removed in the future
		System.out.println("Operation applied: " + operation.getClass().getCanonicalName());
	}

	@Override
	public void storeResults(IProducer<?> producer) {
		IDataStore store = InMemoryDataStore.getInstance();
		String runnerId = this.getMetadata("id").toString();
		store.set(runnerId, producer);
	}

}
