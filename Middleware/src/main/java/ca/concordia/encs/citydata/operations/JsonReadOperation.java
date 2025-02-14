package ca.concordia.encs.citydata.operations;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import ca.concordia.encs.citydata.core.AbstractOperation;
import ca.concordia.encs.citydata.core.IOperation;

public class JsonReadOperation extends AbstractOperation<JsonObject> implements IOperation<JsonObject> {
	private String path = "";
	private JsonObject currentObject;
	final Pattern pattern = Pattern.compile("\\[(\\d+)\\]");

	public void setPath(String path) {
		this.path = path;
	}

	@Override
	public ArrayList<JsonObject> apply(ArrayList<JsonObject> inputs) {

		final ArrayList<JsonObject> matchingJsonObjects = new ArrayList<>();
		final String[] pathParts = this.path.split(".");

		for (JsonObject input : inputs) {

			this.currentObject = input;
			for (String key : pathParts) {

				// check if key contains []
				final Matcher matcher = this.pattern.matcher(key);

				// if so, go to key and array position
				if (matcher.find()) {
					final JsonArray jsonArray = this.currentObject.get(key).getAsJsonArray();
					final String arrayPositionString = matcher.group(1);
					final int arrayPosition = Integer.parseInt(arrayPositionString);
					this.currentObject = jsonArray.get(arrayPosition).getAsJsonObject();
				} else {
					this.currentObject = this.currentObject.get(key).getAsJsonObject();
				}

			}
			matchingJsonObjects.add(this.currentObject);
		}

		return matchingJsonObjects;
	}
}
