package ca.concordia.ngci.tools4cities.middleware.consumers;

import java.util.Set;

import ca.concordia.ngci.tools4cities.middleware.middleware.AbstractConsumer;
import ca.concordia.ngci.tools4cities.middleware.middleware.IConsumer;
import ca.concordia.ngci.tools4cities.middleware.middleware.IOperation;
import ca.concordia.ngci.tools4cities.middleware.middleware.IProducer;

public class StringConsumer extends AbstractConsumer implements IConsumer {

	public StringConsumer(final Set<IProducer<?>> setOfProducers) {
		super(setOfProducers);
	}

	@Override
	public String[] getResults() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void newDataAvailable(IProducer aProducer) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void newDataAvailable(IOperation anOperation) throws Exception {
		// TODO Auto-generated method stub
		
	}

}
