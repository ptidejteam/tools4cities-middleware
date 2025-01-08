package com.concordia.middleware.producers;

import com.concordia.middleware.core.AbstractProducer;
import com.concordia.middleware.core.IConsumer;
import com.concordia.middleware.core.IProducer;

public class GeometryProducer extends AbstractProducer<String> implements IProducer<String> {
	// TODO: how to work with different data sets?
	private String city = "";
	private JSONProducer jsonProducer = new JSONProducer("./src/test/data/montreal_geometries.json", null);
	
	public GeometryProducer(String city) {
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
		this.jsonProducer.fetchData();
	}
	
	@Override 
	public void addObserver(final IConsumer<String> aConsumer) {
		this.jsonProducer.addObserver(aConsumer);
	}

}
