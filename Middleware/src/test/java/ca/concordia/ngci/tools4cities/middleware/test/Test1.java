package ca.concordia.ngci.tools4cities.middleware.test;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;

import org.junit.Assert;
import org.junit.Test;

import ca.concordia.ngci.tools4cities.middleware.consumer.IConsumer;
import ca.concordia.ngci.tools4cities.middleware.consumer.StringConsumer;
import ca.concordia.ngci.tools4cities.middleware.middleware.IOperation;
import ca.concordia.ngci.tools4cities.middleware.middleware.Middleware;
import ca.concordia.ngci.tools4cities.middleware.producer.IProducer;

public class Test1 {

	@Test
	public void test() {
		final IConsumer<String> c = new StringConsumer();
		final Middleware m = new Middleware();

		final Set<IProducer<?>> producers = new HashSet<IProducer<?>>();

		final Queue<IOperation<?>> operations = new LinkedList<IOperation<?>>();

		m.requestData(c, producers, operations);
		// Assert.assertArrayEquals(null, null);
	}

}
