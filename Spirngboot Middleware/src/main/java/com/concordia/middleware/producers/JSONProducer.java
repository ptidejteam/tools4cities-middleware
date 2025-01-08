package com.concordia.middleware.producers;

import java.util.ArrayList;
import java.util.List;

import com.concordia.middleware.core.AbstractProducer;
import com.concordia.middleware.core.IProducer;
import com.concordia.middleware.core.RequestOptions;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;


public class JSONProducer extends AbstractProducer<String> implements IProducer<String> {

	public JSONProducer(String filePath, RequestOptions fileOptions) {
		this.filePath = filePath;
		this.fileOptions = fileOptions;
	}

	@Override
	public void fetchData() throws Exception {
		try {
			final List<String> jsonObjects = new ArrayList<String>();
			String jsonString = new String(this.fetchFromPath());
			
			// convert JSON string to object
			final JsonElement jsonElement = JsonParser.parseString(jsonString);
			final JsonArray elementAsArray = jsonElement.getAsJsonArray();
			
			if (elementAsArray != null && elementAsArray.size() > 0) {
				for (JsonElement el : elementAsArray) {
					jsonObjects.add(el.toString());
				}
			} else {
				jsonObjects.add(jsonElement.toString());
			}
			
			this.notifyObservers(jsonObjects);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}