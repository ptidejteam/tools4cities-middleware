package ca.concordia.encs.citydata.runners;

import java.lang.reflect.Method;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import ca.concordia.encs.citydata.core.AbstractRunner;
import ca.concordia.encs.citydata.core.IOperation;
import ca.concordia.encs.citydata.core.IProducer;
import ca.concordia.encs.citydata.core.IRunner;
import ca.concordia.encs.citydata.core.ReflectionUtils;
import ca.concordia.encs.citydata.datastores.InMemoryDataStore;
import ca.concordia.encs.citydata.producers.ExceptionProducer;

/* This Runner executes a single Producer with no Operations. 
 * It can be used for tests when you want to quickly inspect the output of 
 * a given Producer with parameters. This Runner is also used by Operations 
 * that need to spawn other Producers as part of the transformation they 
 * are applying (e.g. MergeOperation).
 * 
 * Author: Gabriel C. Ullmann 
 * Date: 2025-02-14
 */
public class SingleStepRunner extends AbstractRunner implements IRunner {

	private String targetProducerName;
	private JsonArray targetProducerParams;

	public SingleStepRunner(String targetProducer, JsonArray targetProducerParams) {
		this.targetProducerName = targetProducer;
		this.targetProducerParams = targetProducerParams;
	}

	@Override
	public void runSteps() throws Exception {
		if (this.targetProducerName != null) {

			// instantiate a new Producer instance
			Class<?> targetProducerClass = Class.forName(this.targetProducerName);
			IProducer<?> targetProducerInstance = (IProducer<?>) targetProducerClass.getDeclaredConstructor()
					.newInstance();

			// set Producer params
			for (JsonElement param : this.targetProducerParams) {
				JsonObject paramObject = param.getAsJsonObject();
				String methodName = "set" + ReflectionUtils.capitalize(paramObject.get("name").getAsString());
				for (Method method : targetProducerClass.getMethods()) {
					if (method.getName().equals(methodName) && method.getParameterCount() == 1) {
						Object targetProducerParamValue = ReflectionUtils.convertValue(method.getParameterTypes()[0],
								paramObject.get("value"));
						method.invoke(targetProducerInstance, targetProducerParamValue);
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
		try {
			this.storeResults(producer);
			this.setAsDone();
			System.out.println("Run completed!");
		} catch (Exception e) {
			InMemoryDataStore store = InMemoryDataStore.getInstance();
			store.set(this.getMetadataString("id"), new ExceptionProducer(e));

			// stop runner as soon as an exception is thrown to avoid infinite loops
			this.setAsDone();
			System.out.println(e.getMessage());
		}
	}

	@Override
	public void storeResults(IProducer<?> producer) {
		InMemoryDataStore store = InMemoryDataStore.getInstance();
		String runnerId = this.getMetadata("id").toString();
		store.set(runnerId, producer);
	}

}
