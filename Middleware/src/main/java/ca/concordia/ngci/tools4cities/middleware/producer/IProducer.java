package ca.concordia.ngci.tools4cities.middleware.producer;

import java.io.IOException;

import ca.concordia.ngci.tools4cities.middleware.consumer.IConsumer;

public interface IProducer<E> {
	E fetchData() throws IOException;

	void addObserver(final IConsumer<E> aConsumer);
}
