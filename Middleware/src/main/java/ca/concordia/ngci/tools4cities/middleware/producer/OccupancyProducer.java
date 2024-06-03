package ca.concordia.ngci.tools4cities.middleware.producer;

import java.util.Random;

import ca.concordia.ngci.tools4cities.middleware.consumer.OccupancyConsumer;

public class OccupancyProducer{
	
	 private OccupancyConsumer consumer;
	 private Random random;
	    private String previousData;
	    private int changeCount;
	    
	    public OccupancyProducer(OccupancyConsumer consumer) {
	        this.consumer = consumer;
	        this.random = new Random();
	        this.previousData = null;
	        this.changeCount = 0;
	    }
	    
	    public void produceData() {
	        String data = random.nextBoolean() ? "Occupied" : "Vacant";
	        if (previousData == null || !previousData.equals(data)) {
	            changeCount++;
	        }
	        consumer.consumeData(data, changeCount);
	        previousData = data;
	    }
}
