package ca.concordia.ngci.tools4cities.middleware.operations;

import java.util.Set;

public class StringReplaceOperation {

	private String searchFor = "";
	private String replaceBy = "";
	public StringReplaceOperation() {
	}
	public StringReplaceOperation(String searchFor, String replaceBy) {
		this.searchFor = searchFor;
		this.replaceBy = replaceBy;
	}

	public void setSearchFor(String searchFor) {
		this.searchFor = searchFor;
	}

	public void setReplaceBy(String replaceBy) {
		this.replaceBy = replaceBy;
	}
	
	public void apply() {
		System.out.println("The operation was applied succesfully!");
	}

}
