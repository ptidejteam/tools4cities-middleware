package ca.concordia.ngci.tools4cities.middleware.producers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import ca.concordia.ngci.tools4cities.middleware.middleware.AbstractProducer;
import ca.concordia.ngci.tools4cities.middleware.middleware.IProducer;

public class OccupancyProducer extends AbstractProducer<String>
		implements IProducer<String> {

	private int listSize;
	private String previousData;
	private int changeCount;

	public OccupancyProducer(int listSize) {
		this.listSize = listSize;
		this.previousData = null;
		this.changeCount = 0;
	}

	@Override
	public void fetchData() throws IOException {

		Random random = new Random();
		
		final List<String> randomOccupancy = new ArrayList<String>();
		for (int i = 0; i < this.listSize; i++) {
			String occupancyValue = random.nextBoolean() ? "Occupied" : "Vacant";
			randomOccupancy.add(occupancyValue);
			if (previousData == null || !previousData.equals(occupancyValue)) {
				changeCount++;
			}
			this.previousData = occupancyValue;
			this.notifyObservers(randomOccupancy);
		}
	}
}