package ca.concordia.ngci.tools4cities.middleware.consumer;

import java.util.List;

public class StringConsumer implements IConsumer<String> {
	@Override
	public void receiveData(final List<String> data) {
		for (String datum : data) {
			System.out.println("Received data: " + datum);
		}
	}

	@Override
	public String[] getResults() {
		// TODO Auto-generated method stub
		return null;
	}
}
