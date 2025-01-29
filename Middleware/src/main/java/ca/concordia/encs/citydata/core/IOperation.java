package ca.concordia.encs.citydata.core;

import java.util.ArrayList;

/**
 *
 * The Operation entity is responsible for: - Applying a transformation to an
 * ArrayList of a type - Notify observers when the transformation is completed
 * 
 */
public interface IOperation<E> {

	// 1 - prepare producer
	void addObserver(final IRunner aRunner);

	// 2 - perform operation
	ArrayList<E> apply(ArrayList<E> input);

	// 3 - notify when done
	void notifyObservers();
}
