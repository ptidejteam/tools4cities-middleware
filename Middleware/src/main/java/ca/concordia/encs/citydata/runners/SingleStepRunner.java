package ca.concordia.encs.citydata.runners;

import java.lang.reflect.Method;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import ca.concordia.encs.citydata.core.AbstractRunner;
import ca.concordia.encs.citydata.core.IOperation;
import ca.concordia.encs.citydata.core.IProducer;
import ca.concordia.encs.citydata.core.IRunner;
import ca.concordia.encs.citydata.datastores.InMemoryDataStore;

/**
 * 
 * This Runner runs a single Producer with no Operations. For test only.
 * 
 */
public class SingleStepRunner extends AbstractRunner implements IRunner {

	private String targetProducerName;
	private JsonArray targetProducerParams;

	public SingleStepRunner(String targetProducer, JsonArray targetProducerParams) {
		this.targetProducerName = targetProducer;
		this.targetProducerParams = targetProducerParams;
	}

	private String capitalize(String str) {
		return str == null || str.isEmpty() ? str : str.substring(0, 1).toUpperCase() + str.substring(1);
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

	@Override
	public void runSteps() throws Exception {
		if (this.targetProducerName != null) {

			// instantiate a new Producer instance
			Class<?> targetProducerClass = Class.forName(this.targetProducerName);
			IProducer targetProducerInstance = (IProducer) targetProducerClass.getDeclaredConstructor().newInstance();

			// set Producer params
			for (JsonElement param : this.targetProducerParams) {
				JsonObject paramObject = param.getAsJsonObject();
				String methodName = "set" + capitalize(paramObject.get("name").getAsString());
				for (Method method : targetProducerClass.getMethods()) {
					if (method.getName().equals(methodName) && method.getParameterCount() == 1) {
						method.invoke(targetProducerInstance,
								convertValue(method.getParameterTypes()[0], paramObject.get("value")));
						break;
					}
				}
			}

			targetProducerInstance.addObserver(this);
			targetProducerInstance.fetch();
		}
	}

	@Override
	public void applyNextOperation(IProducer<?> producer) {
		System.out.println("This runner does not support operations!");
	}

	@Override
	public void newOperationApplied(IOperation<?> operation) {
		System.out.println("This runner does not support operations!");
	}

	@Override
	public void newDataAvailable(IProducer<?> producer) {
		this.storeResults(producer);
		this.setAsDone();
		System.out.println("Run completed!");
	}

	@Override
	public void storeResults(IProducer<?> producer) {
		InMemoryDataStore store = InMemoryDataStore.getInstance();
		String runnerId = this.getMetadata("id").toString();
		store.set(runnerId, producer);
	}

}
