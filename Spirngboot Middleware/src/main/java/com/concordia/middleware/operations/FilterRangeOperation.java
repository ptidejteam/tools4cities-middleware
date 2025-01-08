package com.concordia.middleware.operations;

import java.util.ArrayList;
import java.util.List;

import com.concordia.middleware.core.IOperation;

public class FilterRangeOperation implements IOperation {
	String filterByStart = "";
	String filterByEnd = "";
	
	public FilterRangeOperation(String filterByStart, String filterByEnd) {
		this.filterByStart = filterByStart;
		this.filterByEnd = filterByEnd;
	}

	@Override
	public <String> List<String> perform(List<String> inputs) throws Exception {
		List<String> filteredList = inputs;
		int startOffset = -1;
		int endOffset = -1;
		int inputSize = inputs.size();

		for (int i = 0; i < inputSize; i++) {
			String input = inputs.get(i);
			if (input.toString().contains(filterByStart)) {
				startOffset = i;
			}	
			if (input.toString().contains(filterByEnd)) {
				endOffset = i;
				break;
			}
		}
		
		if (startOffset > -1 && endOffset > -1) {
			return new ArrayList<>(filteredList.subList(startOffset, endOffset));
		} else if (startOffset > -1) {
			return new ArrayList<>(filteredList.subList(startOffset, inputSize - 1));
		}
		
		return filteredList;
	}
	


}
