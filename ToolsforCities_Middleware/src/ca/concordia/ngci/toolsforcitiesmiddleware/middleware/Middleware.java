package ca.concordia.ngci.toolsforcitiesmiddleware.middleware;

import java.io.IOException;
import java.util.List;
import java.util.function.Consumer;

import ca.concordia.ngci.toolsforcitiesmiddleware.consumer.IConsumer;
import ca.concordia.ngci.toolsforcitiesmiddleware.producer.IProducer;
import ca.concordia.ngci.toolsforcitiesmiddleware.producer.Producer;

// TODO Allow as many consumers and producers at run-time
// TODO Make the Middleware a Singleton

public class Middleware {
	private final Producer<?> producer;
	private final Consumer<?> consumer;

	public Middleware(final Producer<?> producer, final Consumer<?> consumer) {
		this.producer = producer;
		this.consumer = consumer;
	}

	public void requestData() throws IOException {
		// Receive request from Consumer
		final List data = producer.fetchData();

		// Send data to consumer
		consumer.receiveData(data);
	}

	public <E> void addConsumer(final IConsumer<E> aConsumer, final IProducer<E> aProducer) {
		aProducer.addObserver(aConsumer);
	}
}
