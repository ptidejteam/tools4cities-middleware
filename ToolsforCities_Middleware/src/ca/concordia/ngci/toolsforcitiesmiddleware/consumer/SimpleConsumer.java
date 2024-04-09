package ca.concordia.ngci.toolsforcitiesmiddleware.consumer;

import java.util.List;

public class SimpleConsumer implements IConsumer {
	@Override
	public void receiveData(final List<String> data) {
		for (String datum : data) {
			System.out.println("Received data: " + datum);
		}
	}
}
