package ca.concordia.ngci.tools4cities.middleware.middleware;

import java.util.List;

public interface IConsumer<E> {

	void newDataAvailable(List<E> data);
	
	List<E> getResults();
	
}
