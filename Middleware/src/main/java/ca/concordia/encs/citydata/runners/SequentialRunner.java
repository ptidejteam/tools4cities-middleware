package ca.concordia.encs.citydata.runners;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import ca.concordia.encs.citydata.core.AbstractRunner;
import ca.concordia.encs.citydata.core.IDataStore;
import ca.concordia.encs.citydata.core.IOperation;
import ca.concordia.encs.citydata.core.IProducer;
import ca.concordia.encs.citydata.core.IRunner;
import ca.concordia.encs.citydata.datastores.InMemoryDataStore;
import ca.concordia.encs.citydata.operations.StringReplaceOperation;
import ca.concordia.encs.citydata.producers.StringProducer;

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

	@Override
	public void runSteps() throws Exception {
		StringProducer producer = null;

		// if there are no steps to run, warn the user and stop
		if (this.steps == null) {
			this.setAsDone();
			throw new RuntimeException("No steps to run! Please provide steps so the runner can execute them.");
		}

		// check if producer exists
		System.out.println("Run started!");
		if (this.steps.get("use").getAsString().equalsIgnoreCase("StringProducer")) {

			// if there are producer params, iterate over them
			if (this.steps.get("withParams") != null) {
				JsonArray producerParams = this.steps.get("withParams").getAsJsonArray();
				String generationProcessParam = null;
				Integer stringLengthParam = null;
				for (JsonElement param : producerParams) {
					JsonObject currentParam = param.getAsJsonObject();
					if (currentParam.get("name").getAsString().equals("generationProcess")) {
						generationProcessParam = currentParam.get("value").getAsString();
					}

					if (currentParam.get("name").getAsString().equals("stringLength")) {
						stringLengthParam = currentParam.get("value").getAsInt();
					}
				}

				producer = new StringProducer();
				producer.setGenerationProcess(generationProcessParam);
				producer.setStringLength(stringLengthParam);
				producer.addObserver(this);
			}

			// if there are operations, apply the first one
			// subsequent operation will be applied on P1' once the first is done
			this.applyNextOperation(producer);

		}
	}

	@Override
	public void applyNextOperation(IProducer<?> producer) throws Exception {
		if (this.steps.get("apply") != null) {
			JsonArray operationsToApply = this.steps.get("apply").getAsJsonArray();
			JsonObject currentOperation = operationsToApply.get(this.operationCounter).getAsJsonObject();
			if (currentOperation.get("name").getAsString().equals("StringReplaceOperation")) {
				String searchFor = "";
				String replaceBy = "";

				// if operation has parameters, extract them and pass them to operation object
				JsonArray operationParams = currentOperation.get("withParams").getAsJsonArray();
				for (JsonElement param : operationParams) {
					JsonObject currentParam = param.getAsJsonObject();

					if (currentParam.get("name").getAsString().equals("searchFor")) {
						searchFor = currentParam.get("value").getAsString();
					} else if (currentParam.get("name").getAsString().equals("replaceBy")) {
						replaceBy = currentParam.get("value").getAsString();
					}
				}
				producer.setOperation(new StringReplaceOperation(searchFor, replaceBy));
				System.out.println(
						"Applying operation " + (this.operationCounter + 1) + " out of " + operationsToApply.size());
				producer.fetch();
			}

		}
	}

	@Override
	public void newDataAvailable(IProducer<?> producer) throws Exception {

		// congratulations, you are done with your operation, go to the next one
		this.operationCounter += 1;

		// but is there really a next one? if not, stop
		JsonArray operationsToApply = this.steps.get("apply").getAsJsonArray();
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
