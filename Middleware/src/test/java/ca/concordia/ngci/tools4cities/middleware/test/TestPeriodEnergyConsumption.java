package ca.concordia.ngci.tools4cities.middleware.test;

import java.util.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import ca.concordia.ngci.tools4cities.middleware.producers.EnergyConsumptionProducer;
import ca.concordia.ngci.tools4cities.middleware.consumers.EnergyConsumptionConsumer;
import ca.concordia.ngci.tools4cities.middleware.middleware.IProducer;


public class TestPeriodEnergyConsumption {

	@Test
    public void testEnergyConsumptionForGivenMonths() {
		// Create producer to read energy consumption data for January (third column, index 2) and March (fifth column, index 4)
	    final IProducer<String> producer = new EnergyConsumptionProducer(
	        "/home/zongo/Documents/ConcordiaLabs/City-Layer-Data/Conso_Moy_RTA_SCIAN_Energir_RES.csv", 
	        null, // Optional config, can remain null
	        1,    // Index for January (third column)
	        3     // Index for March (fifth column)
	    );

	    //IOp o=new Colu,nSelector(producer, 1)
	    
	    final Set<IProducer<String>> producers = new HashSet<>();
	    producers.add(producer);

	    // Create the consumer
	    final EnergyConsumptionConsumer consumer = new EnergyConsumptionConsumer(producers);

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
	    expectedValuesJanuary.put(1, 14705.0); // Expected value for January (index 2)
	    Map<Integer, Double> expectedValuesMarch = new HashMap<>();
	    expectedValuesMarch.put(3, 11286.0); // Expected value for March (index 4)

	    // Assert for January
	    Double actualJanuarySum = actualConsumption.get("G2A") != null ? actualConsumption.get("G2A").get(1) : null;
	    Assertions.assertEquals(expectedValuesJanuary.get(1), actualJanuarySum, 
	        String.format("Assertion failed for G2A in January: expected %f but was %f", expectedValuesJanuary.get(1), actualJanuarySum));

	    // Assert for March
	    Double actualMarchSum = actualConsumption.get("G2A") != null ? actualConsumption.get("G2A").get(3) : null;
	    Assertions.assertEquals(expectedValuesMarch.get(3), actualMarchSum, 
	        String.format("Assertion failed for G2A in March: expected %f but was %f", expectedValuesMarch.get(3), actualMarchSum));

	    // Print actual consumption for verification
	    System.out.println("Actual Energy Consumption per Postal Code: " + actualConsumption);
    }
	
}
