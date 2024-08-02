package ca.concordia.ngci.tools4cities.middleware.middleware;

import java.util.List;

public interface IConsumer<E> {
	// TODO Rename for a better name. more in line with the Observer DP 
	void newDataAvailable(List<?> data);
	
	List<E> getResults();


}
