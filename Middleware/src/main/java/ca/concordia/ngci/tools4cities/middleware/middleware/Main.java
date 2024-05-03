package ca.concordia.ngci.tools4cities.middleware.middleware;

import java.io.IOException;
import java.util.List;

import ca.concordia.ngci.tools4cities.middleware.consumer.IConsumer;
import ca.concordia.ngci.tools4cities.middleware.consumer.StringConsumer;
import ca.concordia.ngci.tools4cities.middleware.producer.CSVProducer;
import ca.concordia.ngci.tools4cities.middleware.producer.IProducer;

public class Main {
	public static void main(String[] args) throws IOException {
		IProducer<List<String>> producer = new CSVProducer();
		IConsumer<String> consumer = new StringConsumer();

		Middleware middleware = new Middleware();

		// Initiate request for data
		middleware.requestData(null, null, null);
	}
}
