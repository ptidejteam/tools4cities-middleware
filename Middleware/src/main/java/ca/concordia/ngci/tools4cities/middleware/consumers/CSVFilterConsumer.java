package ca.concordia.ngci.tools4cities.middleware.consumers;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import ca.concordia.ngci.tools4cities.middleware.middleware.AbstractConsumer;
import ca.concordia.ngci.tools4cities.middleware.middleware.IConsumer;
import ca.concordia.ngci.tools4cities.middleware.middleware.IOperation;
import ca.concordia.ngci.tools4cities.middleware.middleware.IProducer;
import ca.concordia.ngci.tools4cities.middleware.operations.FilterOperation;

/**
 * The CSVFilterConsumer consumes string producers and filter these strings by a prefix.
 */
public class CSVFilterConsumer extends AbstractConsumer<String> implements IConsumer<String> {

	private List<String> results;

	public CSVFilterConsumer(final Set<IProducer<String>> setOfProducers) {
		super(setOfProducers);
	}


	@Override
	public List<String> getResults() {
		return results;
	}

	@Override
	public final void newDataAvailable(List<?> data) {
		this.results = new ArrayList<>();
		
		// filter by postal code prefix
		final String postalCodePrefix = "H1B";
		IOperation filtering = new FilterOperation(postalCodePrefix, false);
		try {
			List<String> processedItems = (List<String>) filtering.perform(data);
			this.results.addAll(processedItems);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
