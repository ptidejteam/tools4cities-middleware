package ca.concordia.ngci.tools4cities.middleware.consumers;

import java.util.Iterator;
import java.util.List;
import java.util.Set;

import ca.concordia.ngci.tools4cities.middleware.middleware.AbstractConsumer;
import ca.concordia.ngci.tools4cities.middleware.middleware.IConsumer;
import ca.concordia.ngci.tools4cities.middleware.middleware.IOperation;
import ca.concordia.ngci.tools4cities.middleware.middleware.IProducer;

public class CSVConsumer extends AbstractConsumer<String> implements IConsumer<String> {

	private String results;
	private Boolean ignoreHeader = true; 

	public CSVConsumer(final Set<IProducer<String>> setOfProducers) {
		super(setOfProducers);
	}

	@Override
	public String getResults() {
		return results;
	}

	@Override
	public final void newDataAvailable(List<?> data) {
		int i = 1;
		Iterator<?> iterator = data.iterator();
		while (iterator.hasNext()) {
			//if (i++ == 1 && ignoreHeader) {
			//	iterator.next();
			//	continue;		
			//}
			// Retrieve the next element
			String item = (String) iterator.next();
			this.results += item;
		}

	}

}
