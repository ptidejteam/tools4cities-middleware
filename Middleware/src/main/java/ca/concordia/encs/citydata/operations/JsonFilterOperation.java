package ca.concordia.encs.citydata.operations;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.google.gson.JsonObject;

import ca.concordia.encs.citydata.core.AbstractOperation;
import ca.concordia.encs.citydata.core.IOperation;

public class JsonFilterOperation extends AbstractOperation<JsonObject> implements IOperation<JsonObject> {
	String filterBy = "";
	Boolean isExactlyEqual = false;

	public void setFilterBy(String filterBy) {
		this.filterBy = filterBy;
	}

	public void setIsExactlyEqual(Boolean isExactlyEqual) {
		this.isExactlyEqual = isExactlyEqual;
	}

	@Override
	public ArrayList<JsonObject> apply(ArrayList<JsonObject> inputs) {
		List<JsonObject> filteredList = inputs;
		if (!isExactlyEqual) {
			filteredList = inputs.stream().filter(s -> s.toString().contains(filterBy)).collect(Collectors.toList());
		} else {
			filteredList = inputs.stream().filter(s -> s.toString().equalsIgnoreCase(filterBy))
					.collect(Collectors.toList());
		}
		return new ArrayList<JsonObject>(filteredList);
	}
}
