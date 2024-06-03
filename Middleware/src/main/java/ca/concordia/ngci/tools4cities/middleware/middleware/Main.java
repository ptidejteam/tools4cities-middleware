package ca.concordia.ngci.tools4cities.middleware.middleware;

import java.io.IOException;

import ca.concordia.ngci.tools4cities.middleware.consumer.OccupancyConsumer;
import ca.concordia.ngci.tools4cities.middleware.producer.OccupancyProducer;

public class Main {
	public static void main(String[] args) throws IOException {
		//IProducer<List<String>> producer = new CSVProducer();
		//IConsumer<String> consumer = new StringConsumer();

		Middleware middleware = new Middleware();

		// Initiate request for data
		middleware.requestData(null, null, null);
		
		OccupancyConsumer consumer = new OccupancyConsumer();
		OccupancyProducer producer = new OccupancyProducer(consumer);
		
	      for (int i = 0; i < 50; i++) {
	            producer.produceData();
	        }
	}
}
