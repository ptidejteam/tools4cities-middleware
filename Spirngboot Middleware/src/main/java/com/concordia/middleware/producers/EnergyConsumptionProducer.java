package com.concordia.middleware.producers;

import com.concordia.middleware.core.AbstractProducer;
import com.concordia.middleware.core.IProducer;
import com.concordia.middleware.core.IConsumer;

public class EnergyConsumptionProducer extends AbstractProducer<String> implements IProducer<String> {
	// TODO: how to work with different data sets?
	private String city = "";
	private CSVProducer csvProducer = new CSVProducer("./src/test/data/montreal_energy_consumption.csv", null);
	
	public EnergyConsumptionProducer(String city) {
		this.city = city;
	}
	
	@Override
	public void fetchData() throws Exception {
//		String geometryPath;
//		if (city.equalsIgnoreCase("montreal")) {
//			geometryPath = "./src/test/data/montreal_geometries.json";
//		} else {
//			throw new Exception("City \"" + city + "\" is not supported.");
//		}
		
//		this.jsonProducer = new JSONProducer(geometryPath, null);
		this.csvProducer.fetchData();
	}
	
	@Override 
	public void addObserver(final IConsumer<String> aConsumer) {
		this.csvProducer.addObserver(aConsumer);
	}

}
