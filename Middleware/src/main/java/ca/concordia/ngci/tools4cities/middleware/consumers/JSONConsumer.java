package ca.concordia.ngci.tools4cities.middleware.consumers;

import ca.concordia.ngci.tools4cities.middleware.producers.PersonFromJSONProducer.Person;

public class JSONConsumer {
	public void consumeData(Person person) {
		System.out.println("Received Data: " + person);
	}
}
