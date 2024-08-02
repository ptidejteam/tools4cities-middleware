package ca.concordia.ngci.tools4cities.middleware.test;

import java.io.IOException;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Set;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import ca.concordia.ngci.tools4cities.middleware.producers.CSVProducer;
import ca.concordia.ngci.tools4cities.middleware.consumers.CSVConsumer;
import ca.concordia.ngci.tools4cities.middleware.middleware.IConsumer;
import ca.concordia.ngci.tools4cities.middleware.middleware.IProducer;

public class Test1 {

	@Test
	public void test1() {
		final IProducer<String> producer = new CSVProducer("./src/test/data/example.csv");
		final Set<IProducer<String>> producers = new HashSet<IProducer<String>>();
		producers.add(producer);
		final IConsumer<String> consumer = new CSVConsumer(producers);
		List<String> loadedCSV = consumer.getResults();
		Assertions.assertEquals(loadedCSV.size(), 7);
	}

	@Test
	public void test2() {
		
		final Object[] results = new Object[2];

		Thread thread1 = new Thread(() -> {
			final IProducer<String> producer1 = new CSVProducer("./src/test/data/example.csv");
			final Set<IProducer<String>> producers1 = new HashSet<IProducer<String>>();
			producers1.add(producer1);
			final IConsumer<String> consumer1 = new CSVConsumer(producers1);
			results[0] = consumer1;
		});

		Thread thread2 = new Thread(() -> {
			final IProducer<String> producer2 = new CSVProducer("./src/test/data/larger.csv");
			final Set<IProducer<String>> producers2 = new HashSet<IProducer<String>>();
			producers2.add(producer2);
			final IConsumer<String> consumer2 = new CSVConsumer(producers2);
			results[1] = consumer2;
		});
		
		// Start the threads
        thread1.start();
        thread2.start();

        // Wait for the threads to finish
        try {
            thread1.join();
            thread2.join();
            Assertions.assertEquals(7, ((CSVConsumer) results[0]).getResults().size());
            Assertions.assertEquals(0, ((CSVConsumer) results[1]).getResults().size());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

	}

}
