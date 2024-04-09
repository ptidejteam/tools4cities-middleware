package ca.concordia.ngci.toolsforcitiesmiddleware.producer;

import java.io.IOException;

public class TemperatureProducer<E> implements IProducer<E> {
	@Override
	public E fetchData() throws IOException {
		// TODO Using one thread, mimic a producer that produces random integer values
		// every 1 sec.
		}
}
