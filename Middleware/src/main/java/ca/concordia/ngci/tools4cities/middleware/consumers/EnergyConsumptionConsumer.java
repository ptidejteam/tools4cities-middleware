package ca.concordia.ngci.tools4cities.middleware.consumers;

import java.util.*;

import ca.concordia.ngci.tools4cities.middleware.middleware.AbstractConsumer;
import ca.concordia.ngci.tools4cities.middleware.middleware.IConsumer;
import ca.concordia.ngci.tools4cities.middleware.middleware.IProducer;

public class EnergyConsumptionConsumer extends AbstractConsumer<String> implements IConsumer<String> {

	private List<String> results;
    private double sum;

    public EnergyConsumptionConsumer(Set<IProducer<String>> setOfProducers) {
        super(setOfProducers);
        sum = 0.0;
    }

    @Override
    public List<String> getResults() {
        return results;
    }

    @Override
    public void newDataAvailable(List<String> data) {
        this.results = new ArrayList<>(data);
        calculateSum(); // Triggers sum calculation after receiving data
    }

    private void calculateSum() {
        for (String value : results) {
            try {
                sum += Double.parseDouble(value);
            } catch (NumberFormatException e) {
                System.err.println("Invalid number format: " + value); // Skip invalid values
            }
        }
    }

    public Double getSum() {
        return sum;
    }
}
