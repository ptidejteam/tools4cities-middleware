package ca.concordia.ngci.tools4cities.middleware.consumers;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import ca.concordia.ngci.tools4cities.middleware.middleware.AbstractConsumer;
import ca.concordia.ngci.tools4cities.middleware.middleware.IConsumer;
import ca.concordia.ngci.tools4cities.middleware.middleware.IProducer;

public class OccupancyConsumer extends AbstractConsumer<String> implements IConsumer<String> {

	private ArrayList<String> results;

	public OccupancyConsumer(final Set<IProducer<String>> setOfProducers) {
		super(setOfProducers);
	}

	@Override
	public List<String> getResults() {
		return results;
	}

	@Override
	public final void newDataAvailable(List<?> data) {
		this.results = new ArrayList<String>();
		this.results.addAll((ArrayList<String>) data);
	}
}
