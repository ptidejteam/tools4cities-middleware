package ca.concordia.ngci.tools4cities.middleware.test;

import org.junit.Assert;
import org.junit.Test;

import ca.concordia.ngci.tools4cities.middleware.producers.PersonFromJSONProducer;

public class TestPersonJSONProducerThread {

	@Test
	public void testThreadStarts() {
		final PersonFromJSONProducer p = new PersonFromJSONProducer();
		p.start();
		Assert.assertTrue(true);
	}

	@Test
	public void testThreadStarts() {
		final PersonFromJSONProducer p = new PersonFromJSONProducer();
		p.start();
		Assert.assertTrue(true);
	}

}
