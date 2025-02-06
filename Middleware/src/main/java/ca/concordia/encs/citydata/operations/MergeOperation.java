package ca.concordia.encs.citydata.operations;

import java.util.ArrayList;

import com.google.gson.JsonArray;

import ca.concordia.encs.citydata.core.AbstractOperation;
import ca.concordia.encs.citydata.core.IDataStore;
import ca.concordia.encs.citydata.core.IOperation;
import ca.concordia.encs.citydata.datastores.InMemoryDataStore;
import ca.concordia.encs.citydata.runners.LazyRunner;

/**
 * This operation merges two Producer results together.
 */
public class MergeOperation extends AbstractOperation<String> implements IOperation<String> {

	private String targetProducer;
	private JsonArray targetProducerParams;

	public void setTargetProducer(String targetProducer) {
		this.targetProducer = targetProducer;
	}

	public void setTargetProducerParams(JsonArray targetProducerParams) {
		this.targetProducerParams = targetProducerParams;
	}

	@Override
	public ArrayList<String> apply(ArrayList<String> inputs) {
		ArrayList<String> sourceList = inputs;

		try {
			LazyRunner deckard = new LazyRunner(targetProducer, targetProducerParams);
			Thread runnerTask = new Thread() {
				public void run() {
					try {
						deckard.runSteps();
						while (!deckard.isDone()) {
							System.out.println("Busy waiting!");
						}
					} catch (Exception e) {
						System.out.println(e.getMessage());
					}

				}
			};
			runnerTask.start();
			runnerTask.join();

			String runnerId = deckard.getMetadata("id").toString();
			IDataStore store = InMemoryDataStore.getInstance();

			ArrayList<String> targetList = (ArrayList<String>) store.get(runnerId).getResult();
			if (targetList != null && targetList.size() > 0) {
				sourceList.addAll(targetList);
			}

		} catch (InterruptedException e) {
			System.out.println(e.getMessage());
		}

		return sourceList;
	}
}
