package ca.concordia.encs.citydata.producers;

import java.util.ArrayList;
import java.util.Random;

import ca.concordia.encs.citydata.core.AbstractProducer;
import ca.concordia.encs.citydata.core.IProducer;

public class OccupancyProducer extends AbstractProducer<String> implements IProducer<String> {
	private int listSize;

	public void setListSize(int listSize) {
		this.listSize = listSize;
	}

	@Override
	public void fetch() {
		int changeCount = 0;
		String previousData = "";
		Random random = new Random();
		// if this is running for the first time, fetch
		// otherwise, just apply next operation on top of previous result
		if (this.result == null) {
			final ArrayList<String> randomOccupancy = new ArrayList<String>();
			for (int i = 0; i < this.listSize; i++) {
				String occupancyValue = random.nextBoolean() ? "Occupied" : "Vacant";
				randomOccupancy.add(occupancyValue);
				if (previousData == null || !previousData.equals(occupancyValue)) {
					changeCount++;
					System.out.println("Change: " + changeCount);
				}
				previousData = occupancyValue;
			}
			this.result = randomOccupancy;
		}
		this.applyOperation();
	}

	// Fix the error here

	/*
	 * public List<String> getResult() { return this.result; }
	 */

}
