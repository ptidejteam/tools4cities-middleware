package ca.concordia.ngci.tools4cities.middleware.producers;

import java.util.Random;
import java.util.ArrayList;
import java.util.List;

import ca.concordia.ngci.tools4cities.middleware.middleware.AbstractProducer;
import ca.concordia.ngci.tools4cities.middleware.middleware.IProducer;
import ca.concordia.ngci.tools4cities.middleware.middleware.RequestOptions;

/**
 * This producer generates random integers with a fixed delay between the generation of one number and the next.
 * This can be used to simulate a sensor that continuously outputs data.
 */
public class RandomNumberProducer extends AbstractProducer<Integer> implements IProducer<Integer> {
	
	private int listSize;
	private int generationDelay;

	public RandomNumberProducer(int listSize, int generationDelay) {
		this.listSize = listSize;
		this.generationDelay = generationDelay;
	}

	
	@Override
	public void fetchData() throws Exception {
		Random random = new Random();
		final List<Integer> randomNumbers = new ArrayList<Integer>();
		for (int i = 0; i < this.listSize; i++) {
			randomNumbers.add(random.nextInt(100));
			this.notifyObservers(randomNumbers);
			Thread.sleep(this.generationDelay);
		}
	}

}
