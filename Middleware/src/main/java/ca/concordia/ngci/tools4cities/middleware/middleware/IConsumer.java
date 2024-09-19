package ca.concordia.ngci.tools4cities.middleware.middleware;

import java.util.List;

public interface IConsumer<E> {

	void newDataAvailable(List<E> data);
	
	List<E> getResults();
	
	// New method to get an integer result
    //Integer getIntegerResult();
	
}
