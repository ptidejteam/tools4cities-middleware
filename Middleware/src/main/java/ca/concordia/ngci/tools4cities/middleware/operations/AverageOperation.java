package ca.concordia.ngci.tools4cities.middleware.operations;

import java.util.ArrayList;
import java.util.List;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;

import ca.concordia.ngci.tools4cities.middleware.middleware.IOperation;

/**
 * This operation computes the average (arithmetic mean) of a list of JsonArray
 */
public class AverageOperation implements IOperation {

	@Override
	public <E> List<E> perform(List<E> inputs) throws Exception {
		float sum = 0;
		float elementCount = 0;
		List<E> result = new ArrayList<E>();
		List<JsonArray> jsonArrayList = (List<JsonArray>) inputs;

		for (JsonElement jsonArray : jsonArrayList) {
			sum = 0;
			elementCount = jsonArray.getAsJsonArray().size();
			for (JsonElement jsonElement : jsonArray.getAsJsonArray()) {
				float value = jsonElement.getAsFloat();
				sum += value;
			}
			JsonArray resultArray = new JsonArray();
			resultArray.add(sum / elementCount);
			result.add((E) resultArray);
		}
		return result;
	}

}
