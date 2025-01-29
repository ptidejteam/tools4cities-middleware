package ca.concordia.encs.citydata.producers;

import java.util.ArrayList;
import java.util.List;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import ca.concordia.encs.citydata.core.AbstractProducer;
import ca.concordia.encs.citydata.core.RequestOptions;
import ca.concordia.encs.citydata.core.IProducer;

/**
 * This producer can load JSON from a file or remotely via an HTTP request.
 */
public class JSONProducer extends AbstractProducer<JsonObject> implements IProducer<JsonObject> {

	public JSONProducer(String filePath, RequestOptions fileOptions) {
		this.filePath = filePath;
		this.fileOptions = fileOptions;
	}

	@Override
	public void fetch() {
				
		final ArrayList<JsonObject> result = new ArrayList<JsonObject>();
		String jsonString = new String(this.fetchFromPath());
		
		// convert JSON string to object
		final JsonElement jsonElement = JsonParser.parseString(jsonString);
		
		JsonObject jsonObject = new JsonObject();
		if (jsonElement.isJsonArray()) {
			jsonObject.add("result", jsonElement);
		} else {
			jsonObject = jsonElement.getAsJsonObject();
		}
		
		result.add(jsonObject);
		this.result = result;
		this.notifyObservers();
		
	}

}