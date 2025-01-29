package ca.concordia.encs.citydata.core;

/**
 *
 * The Producer entity is responsible for: - Fetching data - Applying a
 * operation on the result - Notify observers when both tasks are done
 * 
 */
public interface IProducer<E> {

	// 1 - prepare producer
	void addObserver(final IRunner aRunner);

	void setOperation(IOperation operation);

	// 2 - fetch data
	void fetch() throws Exception;

	// 3 - transform data and notify when done
	void applyOperation();

	void notifyObservers();

	// 4 - output data
	String getResultJSONString();

}