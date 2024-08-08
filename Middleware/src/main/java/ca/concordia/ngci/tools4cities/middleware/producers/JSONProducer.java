package ca.concordia.ngci.tools4cities.middleware.producers;

import java.util.ArrayList;
import java.util.List;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import ca.concordia.ngci.tools4cities.middleware.middleware.AbstractProducer;
import ca.concordia.ngci.tools4cities.middleware.middleware.RequestOptions;
import ca.concordia.ngci.tools4cities.middleware.middleware.IProducer;

/**
 * This producer can load JSON from a file or remotely via an HTTP request
 */
public class JSONProducer extends AbstractProducer<JsonObject> implements IProducer<JsonObject> {

	public JSONProducer(String filePath, RequestOptions fileOptions) {
		this.filePath = filePath;
		this.fileOptions = fileOptions;
	}

	@Override
	public void fetchData() throws Exception {
		try {
			final List<JsonObject> jsonObjects = new ArrayList<JsonObject>();
			String jsonString = this.fetchFromPath();
			
			// convert JSON string to object
			final JsonElement jsonElement = JsonParser.parseString(jsonString);
			final JsonObject jsonObject = jsonElement.getAsJsonObject();
			jsonObjects.add(jsonObject);
			this.notifyObservers(jsonObjects);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
