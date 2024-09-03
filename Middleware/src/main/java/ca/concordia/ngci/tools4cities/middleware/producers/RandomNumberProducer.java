package ca.concordia.ngci.tools4cities.middleware.producers;

import java.util.Random;
import java.util.ArrayList;
import java.util.List;

import ca.concordia.ngci.tools4cities.middleware.middleware.AbstractProducer;
import ca.concordia.ngci.tools4cities.middleware.middleware.IProducer;
import ca.concordia.ngci.tools4cities.middleware.middleware.RequestOptions;

public class RandomNumberProducer extends AbstractProducer<Integer> implements IProducer<Integer> {
	
	private int listSize;

	public RandomNumberProducer(int listSize) {
		this.listSize = listSize;
	}

	
	@Override
	public void fetchData() throws Exception {
		int sleepTime = 5000;
		Random random = new Random();
		
		final List<Integer> randomNumbers = new ArrayList<Integer>();
		for (int i = 0; i < this.listSize; i++) {
			randomNumbers.add(random.nextInt(100));
			this.notifyObservers(randomNumbers);
			Thread.sleep(sleepTime);
		}
	}

}
