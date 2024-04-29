package ca.concordia.ngci.tools4cities.middleware.middleware;

import java.io.IOException;
import java.util.List;

import ca.concordia.ngci.tools4cities.middleware.consumer.IConsumer;
import ca.concordia.ngci.tools4cities.middleware.producer.IProducer;

// TODO Allow as many consumers and producers at run-time
// TODO Make the Middleware a Singleton

public class Middleware {
	private final IProducer<?> producer;
	private final IConsumer<?> consumer;

	public Middleware(final IProducer<?> producer, final IConsumer<?> consumer) {
		this.producer = producer;
		this.consumer = consumer;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void requestData() throws IOException {
		// Receive request from Consumer
		final List<?> data = (List) producer.fetchData();

		// Send data to consumer
		consumer.receiveData((List) data);
	}

	public <E> void addConsumer(final IConsumer<E> aConsumer, final IProducer<E> aProducer) {
		aProducer.addObserver(aConsumer);
	}
}
