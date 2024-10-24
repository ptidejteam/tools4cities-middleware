package ca.concordia.ngci.tools4cities.middleware.test;

import java.util.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import ca.concordia.ngci.tools4cities.middleware.producers.PeriodEnergyConsumptionProducer;
import ca.concordia.ngci.tools4cities.middleware.consumers.PeriodEnergyConsumptionConsumer;
import ca.concordia.ngci.tools4cities.middleware.middleware.IProducer;


public class TestPeriodEnergyConsumption {

	@Test
    public void testEnergyConsumptionForGivenMonths() {
		// Create producer to read energy consumption data for January (third column, index 2) and March (fifth column, index 4)
	    final IProducer<String> producer = new PeriodEnergyConsumptionProducer(
	        "./src/test/data/PeriodEnergyConsumptionData.csv", 
	        null, // Optional config, can remain null
	        1,    // Index for January (third column)
	        3     // Index for March (fifth column)
	    );
	    
	    final Set<IProducer<String>> producers = new HashSet<>();
	    producers.add(producer);

	    // Create the consumer
	    final PeriodEnergyConsumptionConsumer consumer = new PeriodEnergyConsumptionConsumer(producers);

	    // Trigger data fetch
	    try {
	        producer.fetchData();
	    } catch (Exception e) {
	        e.printStackTrace();
	    }

	    // Get the postal code energy consumption from the consumer
	    Map<String, Map<Integer, Double>> actualConsumption = consumer.getPostalCodeEnergyConsumption();

	    // Expected values for verification
	    Map<Integer, Double> expectedValuesJanuary = new HashMap<>();
	    expectedValuesJanuary.put(1, 22464.0); // Expected value for January (index 2)
	    Map<Integer, Double> expectedValuesMarch = new HashMap<>();
	    expectedValuesMarch.put(3, 15981.0); // Expected value for March (index 4)

	    // Assert for January
	    Double actualJanuarySum = actualConsumption.get("H1A") != null ? actualConsumption.get("H1A").get(1) : null;
	    Assertions.assertEquals(expectedValuesJanuary.get(1), actualJanuarySum, 
	        String.format("Assertion failed for H1A in January: expected %f but was %f", expectedValuesJanuary.get(1), actualJanuarySum));

	    // Assert for March
	    Double actualMarchSum = actualConsumption.get("H1A") != null ? actualConsumption.get("H1A").get(3) : null;
	    Assertions.assertEquals(expectedValuesMarch.get(3), actualMarchSum, 
	        String.format("Assertion failed for H1A in March: expected %f but was %f", expectedValuesMarch.get(3), actualMarchSum));

	    // Print actual consumption for verification
	    System.out.println("Actual Energy Consumption per Postal Code: " + actualConsumption);
    }
	
}
