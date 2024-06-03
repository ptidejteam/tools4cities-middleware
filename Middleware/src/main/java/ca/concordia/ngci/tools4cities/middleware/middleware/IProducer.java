package ca.concordia.ngci.tools4cities.middleware.middleware;

public interface IProducer<E> {
	E fetchData() throws Exception;

	void addObserver(final IConsumer<E> aConsumer);

	void addObserver(final IOperation anOperation);
}
