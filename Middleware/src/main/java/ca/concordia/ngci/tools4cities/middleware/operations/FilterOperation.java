package ca.concordia.ngci.tools4cities.middleware.operations;

import java.util.List;
import java.util.stream.Collectors;

import ca.concordia.ngci.tools4cities.middleware.middleware.IOperation;

public class FilterOperation implements IOperation {
	String filterBy = "";
	Boolean exactlyEqual = false;
	
	public FilterOperation(String filterBy, Boolean exactlyEqual) {
		this.filterBy = filterBy;
		this.exactlyEqual = exactlyEqual;
	}

	@Override
	public <String> List<String> perform(List<String> inputs) throws Exception {
		//System.out.println(inputs);
		List<String> filteredList = inputs;
		if (!exactlyEqual) {
			filteredList = inputs.stream()
	                .filter(s -> s.toString().contains(filterBy))
	                .collect(Collectors.toList());
		} else {
			 filteredList = inputs.stream()
	                .filter(s -> s.toString().equalsIgnoreCase(filterBy))
	                .collect(Collectors.toList());
		}
		return filteredList;
	}

}
