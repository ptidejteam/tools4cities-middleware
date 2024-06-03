package ca.concordia.ngci.tools4cities.middleware.producers;

import java.io.IOException;

import ca.concordia.ngci.tools4cities.middleware.middleware.AbstractProducer;
import ca.concordia.ngci.tools4cities.middleware.middleware.IProducer;

public class TemperatureProducer<E> extends AbstractProducer<E>
		implements IProducer<E> {

	@Override
	public E fetchData() throws IOException {
		// TODO Using one thread, mimic a producer that produces random integer values
		// every 1 sec.
		return null;
	}
}
