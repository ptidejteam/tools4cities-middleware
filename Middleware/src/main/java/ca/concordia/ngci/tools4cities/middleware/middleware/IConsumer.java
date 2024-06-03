package ca.concordia.ngci.tools4cities.middleware.middleware;

public interface IConsumer<E> {
	// TODO Rename for a better name. more in lign with the Observer DP 
	void newDataAvailable(final IProducer<?> aProducer) throws Exception;

	void newDataAvailable(final IOperation anOperation) throws Exception;

	E[] getResults();
}
