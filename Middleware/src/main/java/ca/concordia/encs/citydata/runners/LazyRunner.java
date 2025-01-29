package ca.concordia.encs.citydata.runners;

import ca.concordia.encs.citydata.core.AbstractRunner;
import ca.concordia.encs.citydata.core.IDataStore;
import ca.concordia.encs.citydata.core.IOperation;
import ca.concordia.encs.citydata.core.IProducer;
import ca.concordia.encs.citydata.core.IRunner;
import ca.concordia.encs.citydata.datastores.InMemoryDataStore;

/**
 * 
 * This Runner runs a single Producer with no Operations. For test only.
 * 
 */
public class LazyRunner extends AbstractRunner implements IRunner {

	private IProducer<?> producer;

	public LazyRunner(IProducer<?> producer) {
		this.producer = producer;
	}

	@Override
	public void runSteps() throws Exception {
		if (this.producer != null) {
			this.producer.addObserver(this);
			this.producer.fetch();
		}
	}

	@Override
	public void applyNextOperation(IProducer<?> producer) {
		System.out.println("This runner does not support operations!");
	}

	@Override
	public void newOperationApplied(IOperation<?> operation) {
		System.out.println("This runner does not support operations!");
	}

	@Override
	public void newDataAvailable(IProducer<?> producer) {
		this.storeResults(producer);
		this.setAsDone();
		System.out.println("Run completed!");
	}

	@Override
	public void storeResults(IProducer<?> producer) {
		IDataStore store = InMemoryDataStore.getInstance();
		String runnerId = this.getMetadata("id").toString();
		store.set(runnerId, producer);
	}

}
