package ca.concordia.encs.citydata.producers;

import java.security.InvalidParameterException;

import ca.concordia.encs.citydata.core.AbstractProducer;
import ca.concordia.encs.citydata.core.IOperation;
import ca.concordia.encs.citydata.core.IProducer;
import ca.concordia.encs.citydata.core.IRunner;

public class EnergyConsumptionProducer extends AbstractProducer<String> implements IProducer<String> {
	private String city;
    private CSVProducer csvProducer;

    public void setCity(String city) {
        this.city = city;
        if (this.city != null) {
            csvProducer = new CSVProducer("./src/test/data/" + this.city + "_energy_consumption.csv", null);
        } else {
            throw new InvalidParameterException("Please provide a city name to the producer.");
        }
    }

    @Override
    public void setOperation(IOperation operation) {  // Removed the generic type parameter
        this.csvProducer.operation = operation;
    }

    @Override
    public void fetch() {
        this.csvProducer.fetch();
        this.applyOperation(); // App
    }

    @Override
    public void addObserver(final IRunner aConsumer) {
        this.csvProducer.addObserver(aConsumer);
    }

    @Override
    public boolean matchesQuery(String query) {
        // If no query provided, consider it a match
        if (query == null || query.trim().isEmpty()) {
            return true;
        }

        // First check if the city name matches
        if (city != null && city.toLowerCase().contains(query.toLowerCase())) {
            return true;
        }

        // If city doesn't match, delegate to the CSV producer's matching
        return csvProducer != null && csvProducer.matchesQuery(query);
    }
}
