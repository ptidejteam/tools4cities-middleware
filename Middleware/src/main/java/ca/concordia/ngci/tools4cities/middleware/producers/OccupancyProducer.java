package ca.concordia.ngci.tools4cities.middleware.producers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import ca.concordia.ngci.tools4cities.middleware.middleware.AbstractProducer;
import ca.concordia.ngci.tools4cities.middleware.middleware.IProducer;

/**
 * This producer generates random sequences of occupancy strings, either "occupied" or "vacant".
 * It simulates an occupancy sensor that, for example, would tell you whether there is a person in a room or not.
 */
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