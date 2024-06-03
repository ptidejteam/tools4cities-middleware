package ca.concordia.ngci.tools4cities.middleware.test;

import java.io.IOException;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;

import org.junit.Assert;
import org.junit.Test;

import ca.concordia.ngci.tools4cities.middleware.consumers.StringConsumer;
import ca.concordia.ngci.tools4cities.middleware.middleware.IConsumer;
import ca.concordia.ngci.tools4cities.middleware.middleware.IOperation;
import ca.concordia.ngci.tools4cities.middleware.middleware.IProducer;

public class Test1 {

	public static class Test1Operation implements IOperation {
		public Test1Operation(final Set<IProducer<?>> producers) {
			// TODO Auto-generated constructor stub
		}

		@Override
		public void newDataAvailable(IProducer<?> aProducer) {
			// TODO Auto-generated method stub

		}

		@Override
		public IProducer<?> fetchData() throws Exception {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public void addObserver(IConsumer<?> aConsumer) {
			// TODO Auto-generated method stub
			
		}

	}

	@Test
	public void test1() {
		final Set<IProducer<?>> producers = new HashSet<IProducer<?>>();
		final Queue<IOperation> operations = new LinkedList<IOperation>();

		final IConsumer<String> consumer = new StringConsumer(producers);

		Assert.assertArrayEquals(consumer.getResults(),
				new String[] { "AAA", "BBB", "CCC" });
	}

	@Test
	public void test2() {
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

			@Override
			public void addObserver(IOperation anOperation) {
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

			@Override
			public void addObserver(IOperation anOperation) {
				// TODO Auto-generated method stub

			}
		};
		producers.add(producer1);
		producers.add(producer2);
		final IConsumer<String> consumer = new StringConsumer(producers);
		final Queue<IOperation> operations = new LinkedList<IOperation>();
		final IOperation operation = new Test1Operation(producers);

		Assert.assertArrayEquals(consumer.getResults(),
				new String[] { "A1", "B2", "C3" });
	}

}
