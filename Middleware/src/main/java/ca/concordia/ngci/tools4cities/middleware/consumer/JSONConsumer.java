package ca.concordia.ngci.tools4cities.middleware.consumer;

import ca.concordia.ngci.tools4cities.middleware.middleware.Person;

public class JSONConsumer {
	public void consumeData(Person person) {
		System.out.println("Received Data: " + person);
	}

}
