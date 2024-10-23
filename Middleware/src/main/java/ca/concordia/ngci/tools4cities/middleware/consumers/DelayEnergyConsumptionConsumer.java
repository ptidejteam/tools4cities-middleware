package ca.concordia.ngci.tools4cities.middleware.consumers;

import java.util.*;

import ca.concordia.ngci.tools4cities.middleware.middleware.AbstractConsumer;
import ca.concordia.ngci.tools4cities.middleware.middleware.IConsumer;
import ca.concordia.ngci.tools4cities.middleware.middleware.IProducer;

public class DelayEnergyConsumptionConsumer extends AbstractConsumer<String> implements IConsumer<String> {

	private List<String> results;
    private Double sum;
    private long totalProcessingTime;

    public DelayEnergyConsumptionConsumer(Set<IProducer<String>> producers) {
        super(producers);
        this.results = new ArrayList<>();  // Initialize the results list to avoid null pointers
        this.sum = 0.0;
        this.totalProcessingTime = 0;
    }

    @Override
    public List<String> getResults() {
        return results;
    }

    @Override
    public void newDataAvailable(List<String> data) {
        if (data == null) {
            System.err.println("Received null data from producer");
            return; // Early return if data is null
        }

        results.addAll(data); // Store received data for reference

        long processStartTime = System.currentTimeMillis(); // Start timing the processing
        
     // Simulate a processing delay (for testing purposes)
        try {
            Thread.sleep(10);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        for (String value : data) {
            try {
                String trimmedValue = value.trim();
                if (!trimmedValue.isEmpty()) {
                    sum += Double.parseDouble(trimmedValue); // Accumulate the sum
                } else {
                    System.err.println("Skipping empty value");
                }
            } catch (NumberFormatException e) {
                System.err.println("Error parsing value: " + value + " - " + e.getMessage());
            }
        }

        long processEndTime = System.currentTimeMillis(); // End timing the processing
        long processingTime = processEndTime - processStartTime; // Calculate the processing time
        System.out.println("Total processing time for this batch: " + processingTime + " ms");

        totalProcessingTime += processingTime; // Accumulate total processing time across batches
    }

    public Double getSum() {
        return sum;
    }

    public long getTotalProcessingTime() {
        return totalProcessingTime; // Getter for total processing time
    }
    
}