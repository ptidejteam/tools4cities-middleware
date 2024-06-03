package ca.concordia.ngci.tools4cities.middleware.consumer;

import java.util.List;

public class OccupancyConsumer implements IConsumer<String> {
	
	private int changeCount;

    public void consumeData(String data, int changeCount) {
        System.out.println("Received data from sensor: " + data);
        this.changeCount = changeCount;
        System.out.println("Number of times the value changed: " + changeCount);
    }

	@Override
	public void receiveData(List<String> data) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String[] getResults() {
		// TODO Auto-generated method stub
		return null;
	}

}
