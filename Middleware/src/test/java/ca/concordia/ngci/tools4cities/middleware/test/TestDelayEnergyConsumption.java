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
        
        final DelayEnergyConsumptionConsumer consumer = new DelayEnergyConsumptionConsumer(producers);

        try {
            producer1.fetchData();
            producer2.fetchData();
            producer3.fetchData();
        } catch (Exception e) {
            e.printStackTrace();
        }

        Thread.sleep(1000); 

        List<String> results = consumer.getResults();
        
        System.out.println("Final results collected: " + results);
        
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

        Double expectedSum = 19.0; // Always adjust based on the actual CSV data
        System.out.println("Calculated Sum at consumer's: " + consumer.getSum());
        
        Assertions.assertEquals(expectedSum, consumer.getSum(), "The sum of the values should be correct.");
        
        List<String> expectedResults = Arrays.asList("3", "6", "10"); 
        Assertions.assertEquals(expectedResults, results, "The results list should match the expected values.");
    }  
}
