package ca.concordia.ngci.tools4cities.middleware.consumers;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import ca.concordia.ngci.tools4cities.middleware.middleware.AbstractConsumer;
import ca.concordia.ngci.tools4cities.middleware.middleware.IConsumer;
import ca.concordia.ngci.tools4cities.middleware.middleware.IOperation;
import ca.concordia.ngci.tools4cities.middleware.middleware.IProducer;
import ca.concordia.ngci.tools4cities.middleware.operations.FilterOperation;

public class CSVConsumer extends AbstractConsumer<String> implements IConsumer<String> {

	private List<String> results;

	public CSVConsumer(final Set<IProducer<String>> setOfProducers) {
		super(setOfProducers);
	}

	@Override
	public List<String> getResults() {
		return results;
	}

	@Override
	public final void newDataAvailable(List<?> data) {
		this.results = new ArrayList<>();
		IOperation filtering = new FilterOperation("H1B", false);
		try {
			List<String> processedItems = (List<String>) filtering.perform(data);
			this.results.addAll(processedItems);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
