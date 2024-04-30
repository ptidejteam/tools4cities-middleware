package ca.concordia.ngci.tools4cities.middleware.middleware;

import java.io.IOException;
import java.util.List;
import java.util.Set;

import ca.concordia.ngci.tools4cities.middleware.consumer.IConsumer;
import ca.concordia.ngci.tools4cities.middleware.producer.IProducer;

// TODO Allow as many consumers and producers at run-time
// TODO Make the Middleware a Singleton

public class Middleware {
	private final IProducer<?> producer;
	private final IConsumer<?> consumer;
	private final IOperations<?> operation;

	public Middleware() {
		this.producer = null;
		this.consumer = null;
		this.operation = null;

	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void requestData() throws IOException {
		// Receive request from Consumer
		final List<?> data = (List) producer.fetchData();

		// Send data to consumer
		consumer.receiveData((List) data);
	}

	public <E> void addProducer(final IProducer<E> aProducer) {
		// aProducer.addObserver(aConsumer);
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Set<IProducer> getProducers(){
		return (Set<IProducer>) producer;
		
	}
	
	public <E> void requestData(IConsumer<E> initiator, Set<IProducer<?>> producer, OrderedSet<IOperations<?>> operations) {
		
		
	}
}
