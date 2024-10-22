package ca.concordia.ngci.tools4cities.middleware.test;

import java.util.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ca.concordia.ngci.tools4cities.middleware.producers.DelayEnergyConsumptionProducer;
import ca.concordia.ngci.tools4cities.middleware.consumers.DelayEnergyConsumptionConsumer;
import ca.concordia.ngci.tools4cities.middleware.middleware.IProducer;

public class TestDelayEnergyConsumption {

    @Test
    public void testDelayEnergyConsumption() throws Exception {
        final IProducer<String> producer1 = new DelayEnergyConsumptionProducer("./src/test/data/Mock_data_1.csv", null, 3);
        final IProducer<String> producer2 = new DelayEnergyConsumptionProducer("./src/test/data/Mock_data_2.csv", null, 3);
        final IProducer<String> producer3 = new DelayEnergyConsumptionProducer("./src/test/data/Mock_data_3.csv", null, 3);

        final Set<IProducer<String>> producers = new HashSet<>();
        producers.add(producer1);
        producers.add(producer2);
        producers.add(producer3);

     // Create the consumer
        final DelayEnergyConsumptionConsumer consumer = new DelayEnergyConsumptionConsumer(producers);

     // Trigger data fetch
        try {
            producer1.fetchData();
            producer2.fetchData();
            producer3.fetchData();
        } catch (Exception e) {
            e.printStackTrace();
        }

     // Add necessary waiting time to allow the processing to end before time is collected
        Thread.sleep(1000); 

        // Check the results
        List<String> results = consumer.getResults();
        
        System.out.println("Final results collected: " + results);
        
     // Ensure fetchData() was only called once for each producer
        long totalFetchingTime = ((DelayEnergyConsumptionProducer) producer1).getFetchTime() +
                                 ((DelayEnergyConsumptionProducer) producer2).getFetchTime() +
                                 ((DelayEnergyConsumptionProducer) producer3).getFetchTime();
        
        System.out.println("Total fetching time at the producer's for the 3 csv files: " + totalFetchingTime + "ms");

     // Check the total processing time calculated in the consumer
        long processingTime = consumer.getTotalProcessingTime();
        System.out.println("Total processing time at the consumer's: " + processingTime + "ms");
        
        long totalProcessingTime = consumer.getTotalProcessingTime() + totalFetchingTime;  

        // Display the total processing time, check if it is the sum of individual fetching time and processing time
        System.out.println("Total time calculated for all operations: " + totalProcessingTime + " ms");

        // Validate the sum of the values (update expectedSum based on your actual data)
        Double expectedSum = 19.0; // Always adjust based on the actual CSV data
        System.out.println("Calculated Sum at consumer's: " + consumer.getSum());
        
        Assertions.assertEquals(expectedSum, consumer.getSum(), "The sum of the values should be correct.");

        // Validate the results list
        List<String> expectedResults = Arrays.asList("3", "6", "10"); // Always adjust based on your expected results
        Assertions.assertEquals(expectedResults, results, "The results list should match the expected values.");
        Assertions.assertTrue(totalProcessingTime > 0, "Total processing time should be greater than zero.");
    }  
}
