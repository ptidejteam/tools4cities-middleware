package ca.concordia.ngci.tools4cities.middleware.producer;

import java.io.IOException;

import ca.concordia.ngci.tools4cities.middleware.consumer.IConsumer;

public class TemperatureProducer<E> implements IProducer<E> {
	@Override
	public E fetchData() throws IOException {
		// TODO Using one thread, mimic a producer that produces random integer values
		// every 1 sec.
		return null;
	}

	@Override
	public void addObserver(IConsumer<E> aConsumer) {
		// TODO Auto-generated method stub

	}
}
