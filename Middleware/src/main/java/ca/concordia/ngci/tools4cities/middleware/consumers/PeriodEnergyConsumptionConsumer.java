package ca.concordia.ngci.tools4cities.middleware.consumers;

import java.util.*;

import ca.concordia.ngci.tools4cities.middleware.middleware.AbstractConsumer;
import ca.concordia.ngci.tools4cities.middleware.middleware.IConsumer;
import ca.concordia.ngci.tools4cities.middleware.middleware.IProducer;


public class PeriodEnergyConsumptionConsumer extends AbstractConsumer<String> implements IConsumer<String> {
	
	private List<String> results;
    private Map<String, Map<Integer, Double>> postalCodeEnergyConsumption; // To store postal codes and their total consumption per month

    public PeriodEnergyConsumptionConsumer(Set<IProducer<String>> setOfProducers) {
        super(setOfProducers);
        postalCodeEnergyConsumption = new HashMap<>();
    }

    @Override
    public List<String> getResults() {
        return results;
    }

    @Override
    public void newDataAvailable(List<String> data) {
        this.results = new ArrayList<>(data);
        calculateEnergyConsumption(); // Trigger energy consumption calculation after receiving data
    }

    private void calculateEnergyConsumption() {
    	for (String entry : results) {
            try {
                String[] parts = entry.split(":"); // Split the postal code, month, and consumption value
                String postalCode = parts[0].trim();
                int month = Integer.parseInt(parts[1].trim()); // Extract month as an integer (1 for January, 3 for March)
                Double consumption = Double.parseDouble(parts[2].trim()); // Extract consumption

                // Always initialize, got a Nullpointer error because of missing initialization
                if (postalCodeEnergyConsumption == null) {
                    postalCodeEnergyConsumption = new HashMap<>();
                }
                // Only consider postal code H1B for months January (1) and March (3)
                if ("H1A".equals(postalCode) && (month == 1 || month == 3)) {
                    postalCodeEnergyConsumption
                        .computeIfAbsent(postalCode, k -> new HashMap<>())
                        .merge(month, consumption, Double::sum);
                }
            } catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
                System.err.println("Invalid input format: " + entry);
            }
        }
    }
    
    public Map<String, Map<Integer, Double>> getPostalCodeEnergyConsumption() {
        return postalCodeEnergyConsumption; // Return the energy consumption per postal code
    }

}
