package ca.concordia.ngci.tools4cities.middleware.test;

import java.util.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ca.concordia.ngci.tools4cities.middleware.producers.CSVProducer;
import ca.concordia.ngci.tools4cities.middleware.consumers.SortedEnergyConsumptionConsumer;
import ca.concordia.ngci.tools4cities.middleware.middleware.IProducer;

public class TestSortedEnergyConsumption {

    @Test
    public void testMonthlyEnergyConsumptionSorting() {
        // Create a producer to read energy consumption data from the CSV file
        final IProducer<String> producer = new CSVProducer(
            "./src/test/data/H1A_Monthly_Consumption.csv", 
            null 
        );
        
        final Set<IProducer<String>> producers = new HashSet<>();
        producers.add(producer);

        // create the consumer
        final SortedEnergyConsumptionConsumer consumer = new SortedEnergyConsumptionConsumer(producers);

        try {
            producer.fetchData(); // This triggers newDataAvailable() in the consumer
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        consumer.printSortedMonthlyConsumption();

        Map<Integer, Double> sortedConsumption = consumer.getMonthlyConsumption(); 

        List<Double> values = new ArrayList<>(sortedConsumption.values());
        for (int i = 1; i < values.size(); i++) {
            Assertions.assertTrue(values.get(i) >= values.get(i - 1), 
                "Monthly consumption is not sorted in ascending order.");
        }
    }

}
