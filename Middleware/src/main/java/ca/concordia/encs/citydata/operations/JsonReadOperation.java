package ca.concordia.encs.citydata.operations;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import ca.concordia.encs.citydata.core.AbstractOperation;
import ca.concordia.encs.citydata.core.IOperation;

public class JsonReadOperation extends AbstractOperation<JsonObject> implements IOperation<JsonObject> {
	private String path = "";
	private JsonElement currentObject;
	final Pattern containsArrayAccess = Pattern.compile("(.+)\\[(\\d+)\\]");;

	public void setPath(String path) {
		this.path = path;
	}

	@Override
	public ArrayList<JsonObject> apply(ArrayList<JsonObject> inputs) {

		final ArrayList<JsonObject> matchingJsonObjects = new ArrayList<>();
		final String[] pathParts = this.path.split("\\.");

		for (JsonObject input : inputs) {

			this.currentObject = input;
			for (String key : pathParts) {

				// check if key contains []
				final Matcher arrayAccessMatcher = this.containsArrayAccess.matcher(key);

				// if so, go to key and array position
				if (arrayAccessMatcher.matches()) {
					final String arrayKey = arrayAccessMatcher.group(1);
					final String arrayPositionString = arrayAccessMatcher.group(2);

					// before reading array position, make sure you are reading from a JsonArray
					if (this.currentObject.isJsonObject()) {
						final JsonArray jsonArray = ((JsonObject) this.currentObject).get(arrayKey).getAsJsonArray();
						final int arrayPosition = Integer.parseInt(arrayPositionString);
						final JsonElement currentElement = jsonArray.get(arrayPosition);
						if (currentElement.isJsonArray()) {
							this.currentObject = currentElement.getAsJsonArray();
						} else {
							this.currentObject = currentElement.getAsJsonObject();
						}
					} else {
						// if not, halt immediately
						matchingJsonObjects.add(this.currentObject.getAsJsonObject());
						return matchingJsonObjects;
					}

				} else {

					if (this.currentObject.isJsonObject()) {
						final JsonElement currentElement = ((JsonObject) this.currentObject).get(key);
						if (currentElement.isJsonObject()) {
							this.currentObject = currentElement.getAsJsonObject();
						} else {
							this.currentObject = currentElement.getAsJsonArray();
						}
					}

				}

			}
			matchingJsonObjects.add(this.currentObject.getAsJsonObject());
		}

		return matchingJsonObjects;
	}
}
