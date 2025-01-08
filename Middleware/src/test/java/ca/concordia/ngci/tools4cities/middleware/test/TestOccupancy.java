package ca.concordia.ngci.tools4cities.middleware.test;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;


import ca.concordia.ngci.tools4cities.middleware.producers.OccupancyProducer;
import ca.concordia.ngci.tools4cities.middleware.consumers.OccupancyConsumer;
import ca.concordia.ngci.tools4cities.middleware.middleware.IProducer;

public class TestOccupancy {

	@Test
	public void testCSVFilesystem() {
		final int listSize = 5;
		final IProducer<String> producer = new OccupancyProducer(listSize);
		final Set<IProducer<String>> producers = new HashSet<IProducer<String>>();
		producers.add(producer);

		// consume energy consumption records and filter them
		final OccupancyConsumer consumer = new OccupancyConsumer(producers);
		List<String> randomOccupancyValues = consumer.getResults();

		// there should be 7 records in the CSV file with this prefix
		Assertions.assertEquals(listSize, randomOccupancyValues.size());
	}
}