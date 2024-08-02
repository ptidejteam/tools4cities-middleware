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
import ca.concordia.ngci.tools4cities.middleware.middleware.IOperation;
import ca.concordia.ngci.tools4cities.middleware.middleware.IProducer;

public class Test1 {


	@Test
	public void test1() {
		final IProducer<String> producer = new CSVProducer("/Users/gabrielullmann/eclipse-workspace/junit-5-basics2/data/examplecsv.csv");
		final Set<IProducer<String>> producers = new HashSet<IProducer<String>>();
		producers.add(producer);
		
		final IConsumer<String> consumer = new CSVConsumer(producers);
		String loadedCSV = consumer.getResults();
		System.out.println(loadedCSV);

		Assertions.assertNotEquals(loadedCSV, null);
	}


}
