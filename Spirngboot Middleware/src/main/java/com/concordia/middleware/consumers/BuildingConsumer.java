package com.concordia.middleware.consumers;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.concordia.middleware.core.AbstractConsumer;
import com.concordia.middleware.core.IConsumer;
import com.concordia.middleware.core.IOperation;
import com.concordia.middleware.core.IProducer;

public class BuildingConsumer extends AbstractConsumer<String> implements IConsumer<String> {

	private ArrayList<String> mergedResults;
	private ArrayList<String> energyConsumptionResults;
	private ArrayList<String> geometryResults;
	
	public BuildingConsumer(final Set<IProducer<String>> setOfProducers, final Set<IOperation> setOfOperations) {
		super(setOfProducers, setOfOperations);
	}

	@Override
	public List<String> getResults() {
		JsonArray timeseriesArray = new JsonArray();
		JsonObject resultObject = new JsonObject();
		for (String energyConsumptionResult : this.energyConsumptionResults) {
			JsonObject energyConsumptionJsonObj = new JsonObject();
			String[] splitConsumptionString = energyConsumptionResult.split(",");
	
			// consumption data
			energyConsumptionJsonObj.addProperty("timestamp", splitConsumptionString[1]);
			energyConsumptionJsonObj.addProperty("consumption", splitConsumptionString[2].replaceAll("\r", ""));
			timeseriesArray.add(energyConsumptionJsonObj);	

			// geometry data
			for (String geomResult : geometryResults) {
				if (geomResult.contains(splitConsumptionString[0])) {
					final JsonObject geometryJsonObject = JsonParser.parseString(geomResult).getAsJsonObject();
					resultObject.addProperty("postalCode", splitConsumptionString[0]);
					resultObject.add("geometry", geometryJsonObject.get("geometry"));
					break;
				}
			}
		}
		
		resultObject.addProperty("timeseriesConsumptionUnit", "kWh");
		resultObject.add("timeseries", timeseriesArray);
		
		mergedResults = new ArrayList<>();
		mergedResults.add(resultObject.toString());
		return mergedResults;
	}

	@Override
	public final void newDataAvailable(List<String> data) {
		try {
			Boolean isJson = data.get(0).contains("[");
			if (data != null && isJson) {
				this.geometryResults = new ArrayList<String>();
				this.geometryResults.addAll((ArrayList<String>) data);
				System.out.println("populate geometry data");
			} else {
				this.energyConsumptionResults = new ArrayList<String>();
				this.energyConsumptionResults.addAll((ArrayList<String>) data);
				System.out.println("populate energy data");
			}
			
			
			for (IOperation operation : this.setOfOperations) { 
				if (operation.getClass().getSimpleName().equals("FilterRangeOperation") && !isJson) {
					this.energyConsumptionResults = (ArrayList<String>) operation.perform(this.energyConsumptionResults);	
					System.out.println("apply operation on energy data");
				} else if (operation.getClass().getSimpleName().equals("FilterOperation") && isJson) {
					this.geometryResults = (ArrayList<String>) operation.perform(this.geometryResults);
					System.out.println("apply operation on geometry data");
				}
		    }
		} catch (Exception e) {
			e.printStackTrace();
		}	
	}

}