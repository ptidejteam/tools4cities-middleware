package ca.concordia.ngci.tools4cities.middleware.consumers;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import ca.concordia.ngci.tools4cities.middleware.middleware.AbstractConsumer;
import ca.concordia.ngci.tools4cities.middleware.middleware.IConsumer;
import ca.concordia.ngci.tools4cities.middleware.middleware.IProducer;

/**
 * The IntegerConsumer consumes any producers which return Integer values (e.g., RandomNumberProducer). It was created with the purpose of testing the producers and consumers by using random numbers.
 */
public class IntegerConsumer extends AbstractConsumer<Integer> implements IConsumer<Integer> {

	private ArrayList<Integer> results;

	public IntegerConsumer(final Set<IProducer<Integer>> setOfProducers) {
		super(setOfProducers);
	}

	@Override
	public List<Integer> getResults() {
		return results;
	}

	@Override
	public final void newDataAvailable(List<?> data) {
		this.results = new ArrayList<Integer>();
		this.results.addAll((ArrayList<Integer>) data);
	}

}
