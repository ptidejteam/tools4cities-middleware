package ca.concordia.ngci.tools4cities.middleware.middleware;

import java.io.IOException;

import ca.concordia.ngci.tools4cities.middleware.consumer.JSONConsumer;
import ca.concordia.ngci.tools4cities.middleware.consumer.OccupancyConsumer;
import ca.concordia.ngci.tools4cities.middleware.producer.JSONProducer;
import ca.concordia.ngci.tools4cities.middleware.producer.OccupancyProducer;

public class Main {
	public static void main(String[] args) throws IOException {
		//IProducer<List<String>> producer = new CSVProducer();
		//IConsumer<String> consumer = new StringConsumer();

		Middleware middleware = new Middleware();

		// Initiate request for data
		middleware.requestData(null, null, null);
		
		JSONConsumer consumer = new JSONConsumer();
		JSONProducer producer = new JSONProducer(consumer);
		producer.sendData();
		
		OccupancyConsumer consumer_Ocu = new OccupancyConsumer();
		OccupancyProducer producer_Ocu = new OccupancyProducer(consumer_Ocu);
		
	      for (int i = 0; i < 50; i++) {
	            producer_Ocu.produceData();
	        }
	}
}