package ca.concordia.ngci.tools4cities.middleware.consumers;

import java.util.Set;

import ca.concordia.ngci.tools4cities.middleware.middleware.AbstractConsumer;
import ca.concordia.ngci.tools4cities.middleware.middleware.IConsumer;
import ca.concordia.ngci.tools4cities.middleware.middleware.IOperation;
import ca.concordia.ngci.tools4cities.middleware.middleware.IProducer;

public class OccupancyConsumer extends AbstractConsumer implements IConsumer {

	public OccupancyConsumer(final Set<IProducer<?>> setOfProducers) {
		super(setOfProducers);
	}

	private int changeCount;

	public void consumeData(String data, int changeCount) {
		System.out.println("Received data from sensor: " + data);
		this.changeCount = changeCount;
		System.out.println("Number of times the value changed: " + changeCount);
	}

	@Override
	public void newDataAvailable(IProducer aProducer) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void newDataAvailable(IOperation anOperation) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Object[] getResults() {
		// TODO Auto-generated method stub
		return null;
	}
}
