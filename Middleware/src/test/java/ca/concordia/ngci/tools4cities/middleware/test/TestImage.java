package ca.concordia.ngci.tools4cities.middleware.test;

import java.awt.Image;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;


import ca.concordia.ngci.tools4cities.middleware.producers.JPGProducer;
import ca.concordia.ngci.tools4cities.middleware.consumers.JPGConsumer;
import ca.concordia.ngci.tools4cities.middleware.middleware.IProducer;

public class TestImage {

	@Test
	public void testImage() {
		final IProducer<Image> producer = new JPGProducer("./src/main/resources/pele.jpg");
		final Set<IProducer<Image>> producers = new HashSet<IProducer<Image>>();
		producers.add(producer);

		// consume energy consumption records and filter them
		final JPGConsumer consumer = new JPGConsumer(producers);
		List<Image> images = consumer.getResults();

		// there should be 7 records in the CSV file with this prefix
		System.out.print(images.get(0));
		Assertions.assertEquals(1, images.size());
	}
}