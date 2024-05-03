package ca.concordia.ngci.tools4cities.middleware.consumer;

import java.util.List;

public interface IConsumer<E> {
	void receiveData(final List<E> data);

	E[] getResults();
}
