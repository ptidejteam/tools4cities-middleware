package ca.concordia.encs.citydata.producers;

import java.security.InvalidParameterException;

import ca.concordia.encs.citydata.core.AbstractProducer;
import ca.concordia.encs.citydata.core.IProducer;
import ca.concordia.encs.citydata.core.IRunner;

public class EnergyConsumptionProducer extends AbstractProducer<String> implements IProducer<String> {
	// TODO: how to work with different data sets?
	private String city;
	private CSVProducer csvProducer;

	public EnergyConsumptionProducer(String city) {
		this.city = city;
		if (this.city != null) {
			csvProducer = new CSVProducer("./src/test/data/" + this.city + "_energy_consumption.csv", null);
		} else {
			throw new InvalidParameterException("Please provide a city name to the producer.");
		}
	}

	@Override
	public void fetch() {
		this.csvProducer.fetch();
	}

	@Override
	public void addObserver(final IRunner aConsumer) {
		this.csvProducer.addObserver(aConsumer);
	}

}
