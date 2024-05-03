package ca.concordia.ngci.tools4cities.middleware.test;

import java.io.IOException;
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
	public void test1() {
		final IConsumer<String> consumer = new StringConsumer();
		final Middleware m = new Middleware();

		final Set<IProducer<?>> producers = new HashSet<IProducer<?>>();

		final Queue<IOperation<?>> operations = new LinkedList<IOperation<?>>();

		m.requestData(consumer, producers, operations);
		Assert.assertArrayEquals(consumer.getResults(), new String[] { "AAA", "BBB", "CCC" });
	}

	@Test
	public void test2() {
		final IConsumer<String> consumer = new StringConsumer();
		final Middleware m = new Middleware();

		final Set<IProducer<?>> producers = new HashSet<IProducer<?>>();
		final IProducer<String> producer1 = new IProducer<>() {

			@Override
			public String fetchData() throws IOException {
				return null; // Should provide "A", "B", "C"
			}

			@Override
			public void addObserver(IConsumer<String> aConsumer) {
				// TODO Auto-generated method stub

			}
		};
		final IProducer<Integer> producer2 = new IProducer<>() {

			@Override
			public Integer fetchData() throws IOException {
				return null; // Should provide 1, 2, 3
			}

			@Override
			public void addObserver(IConsumer<Integer> aConsumer) {
				// TODO Auto-generated method stub

			}
		};
		producers.add(producer1);
		producers.add(producer2);
		
		final Queue<IOperation<?>> operations = new LinkedList<IOperation<?>>();
		final IOperation<?> operation = new IOperation<>() {

			@Override
			public IProducer fetchdata(Set<IProducer> p) {
				return null;
			}
		};

		m.requestData(consumer, producers, operations);
		Assert.assertArrayEquals(consumer.getResults(), new String[] { "A1", "B2", "C3" });
	}

}
