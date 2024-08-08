package ca.concordia.ngci.tools4cities.middleware.consumers;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.Map.Entry;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import ca.concordia.ngci.tools4cities.middleware.middleware.AbstractConsumer;
import ca.concordia.ngci.tools4cities.middleware.middleware.IConsumer;
import ca.concordia.ngci.tools4cities.middleware.middleware.IProducer;
import ca.concordia.ngci.tools4cities.middleware.operations.AverageOperation;

/**
 * TODO
 */
public class RetrofitResultsConsumer extends AbstractConsumer<JsonObject> implements IConsumer<JsonObject> {

	private ArrayList<JsonObject> results;

	public RetrofitResultsConsumer(final Set<IProducer<JsonObject>> setOfProducers) {
		super(setOfProducers);
	}

	@Override
	public List<JsonObject> getResults() {
		return results;
	}

	@Override
	public final void newDataAvailable(List<?> data) {
		this.results = new ArrayList<JsonObject>();
		ArrayList<JsonArray> coolDemandBuildings = new ArrayList<JsonArray>();

		JsonObject results = ((List<JsonObject>) data).get(0).getAsJsonObject().get("results").getAsJsonObject();
		JsonArray currentStatus = results.get("current status").getAsJsonArray();
		for (JsonElement building : currentStatus) {
			JsonObject inselMEB = building.getAsJsonObject().get("insel meb").getAsJsonObject();
			JsonArray monthlyCoolingDemand = inselMEB.get("monthly_cooling_demand").getAsJsonArray();
			coolDemandBuildings.add(monthlyCoolingDemand);
		}

		AverageOperation avg = new AverageOperation();
		try {
			ArrayList<JsonArray> averages = (ArrayList<JsonArray>) avg.perform(coolDemandBuildings);
			JsonObject building = new JsonObject();
			for (int i = 0; i < coolDemandBuildings.size(); i++) {
				building.add(currentStatus.get(i).getAsJsonObject().get("building").getAsString(), averages.get(i).get(0));
			}
			this.results.add(building);
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
