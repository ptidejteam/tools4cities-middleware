package ca.concordia.ngci.tools4cities.middleware.producers;

import java.io.IOException;
import java.util.Random;

import ca.concordia.ngci.tools4cities.middleware.middleware.AbstractProducer;
import ca.concordia.ngci.tools4cities.middleware.middleware.IProducer;

public class OccupancyProducer extends AbstractProducer<Object[]>
		implements IProducer<Object[]> {

	private Random random;
	private String previousData;
	private int changeCount;

	public OccupancyProducer() {
		this.random = new Random();
		this.previousData = null;
		this.changeCount = 0;
	}

	@Override
	public Object[] fetchData() throws IOException {
		String data = random.nextBoolean() ? "Occupied" : "Vacant";
		if (previousData == null || !previousData.equals(data)) {
			changeCount++;
		}
		// consumer.consumeData(data, changeCount);
		previousData = data;
		return new Object[] { data, changeCount };
	}
}
