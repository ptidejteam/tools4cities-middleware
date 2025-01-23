package ca.concordia.encs.citydata.producers;

import java.security.InvalidParameterException;

import ca.concordia.encs.citydata.core.AbstractProducer;
import ca.concordia.encs.citydata.core.IProducer;
import ca.concordia.encs.citydata.core.IRunner;

public class GeometryProducer extends AbstractProducer<String> implements IProducer<String> {
	private String city;
	private JSONProducer jsonProducer;

	public GeometryProducer(String city) {
		this.city = city;
		if (this.city != null) {
			jsonProducer = new JSONProducer("./src/test/data/" + this.city + "_geometries.json", null);
		} else {
			throw new InvalidParameterException("Please provide a city name to the producer.");
		}
	}

	@Override
	public void fetch() {
		this.jsonProducer.fetch();
	}

	@Override
	public void addObserver(final IRunner aRunner) {
		this.jsonProducer.addObserver(aRunner);
	}

}