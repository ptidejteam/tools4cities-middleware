package ca.concordia.ngci.tools4cities.middleware.middleware;

import java.util.Queue;
import java.util.Set;

// TODO Allow as many consumers and producers at run-time
// TODO Make the Middleware a Singleton

public class Middleware {
	public Middleware() {
	}

	/*
	 * @SuppressWarnings({ "rawtypes", "unchecked" }) public void requestData()
	 * throws IOException { // Receive request from Consumer final List<?> data =
	 * (List) producer.fetchData();
	 * 
	 * // Send data to consumer consumer.receiveData((List) data); }
	 */
	public <E> void addProducer(final IProducer<E> aProducer) {
		// aProducer.addObserver(aConsumer);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Set<IProducer> getProducers() {
		return null;
	}

	public <E> void requestData(IConsumer<E> initiator,
			Set<IProducer<?>> producer, Queue<IOperation> operations) {

	}
}
