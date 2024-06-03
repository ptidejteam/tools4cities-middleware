package ca.concordia.ngci.tools4cities.middleware.test;

import ca.concordia.ngci.tools4cities.middleware.middleware.IConsumer;
import ca.concordia.ngci.tools4cities.middleware.middleware.IOperation;
import ca.concordia.ngci.tools4cities.middleware.middleware.IProducer;

public class Test1Operation implements IOperation {

	@Override
	public void newDataAvailable(IProducer<?> aProducer) {
		// TODO Auto-generated method stub

	}

	@Override
	public IProducer<?> fetchData() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void addObserver(IConsumer<?> aConsumer) {
		// TODO Auto-generated method stub
		
	}

}
