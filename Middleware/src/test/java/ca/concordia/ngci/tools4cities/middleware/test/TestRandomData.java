package ca.concordia.ngci.tools4cities.middleware.test;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;

import ca.concordia.ngci.tools4cities.middleware.producers.RandomNumberProducer;
import ca.concordia.ngci.tools4cities.middleware.consumers.IntegerConsumer;
import ca.concordia.ngci.tools4cities.middleware.middleware.IProducer;

public class TestRandomData {

	@Test
	public void case1s1() {
		System.out.println("Case 1 - Scenario 1: Launch the producer first, then the consumer");
	
		try {
			final int listSize = 5;
			final IProducer<Integer> producer = new RandomNumberProducer(listSize);
			final Set<IProducer<Integer>> producers = new HashSet<IProducer<Integer>>();
			producers.add(producer);

			Thread.sleep(2000);
			
			final IntegerConsumer consumer = new IntegerConsumer(producers);
			List<Integer> randomNumbers = consumer.getResults();
			Assertions.assertNotEquals(0, randomNumbers.size());
			
		} catch (InterruptedException ex) {
			System.out.println(ex.getMessage());
		}
		
	}
	
	@Test
	public void case1s2() {
		System.out.println("Case 1 - Scenario 2: Launch the producer and the Consumer simultaneously");
			
		final int listSize = 5;
		final IProducer<Integer> producer = new RandomNumberProducer(listSize);
		final Set<IProducer<Integer>> producers = new HashSet<IProducer<Integer>>();
		producers.add(producer);

		final IntegerConsumer consumer = new IntegerConsumer(producers);
		List<Integer> randomNumbers = consumer.getResults();
		Assertions.assertNotEquals(0, randomNumbers.size());	
	}
	
    // Other Minette's test cases
	
    // System.out.println("Case 1 - Scenario 3: Launch the Consumer first, then the Producer");
    // this case is not possible, when you create a consumer you must pass a list of producers
	
    // System.out.println("Case 2 - Scenario 1: Launch the Producer first, then the second Consumer");
	// Here it would be equivalent to case 1, scenario 1	 	

    // System.out.println("Case 2 - Scenario 2: Launch the producer and the second consumer simultaneously");
	// Here it would be equivalent to case 1, scenario 2		 
		
    // System.out.println("Case 2 - Scenario 3: Launch the second consumer first, then the Producer");
	// this case is not possible, when you create a consumer you must pass a list of producers
	
	
}