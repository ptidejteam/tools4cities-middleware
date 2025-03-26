package ca.concordia.encs.citydata.core;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import ca.concordia.encs.citydata.core.exceptions.MiddlewareException;

/**
 *
 * This implements features common to all Operations, such as notifying Runners
 * 
 */
public abstract class AbstractOperation<E> extends MiddlewareEntity implements IOperation<E> {

	private Set<IRunner> runners = new HashSet<>();

	public AbstractOperation() {
		this.setMetadata("role", "operation");
	}

	@Override
	public void addObserver(final IRunner aRunner) {
		this.runners.add(aRunner);
	}

	/*
	 * @Override public ArrayList<E> apply(ArrayList<E> input) { System.out.
	 * println("Unimplemented method! This method must be implemented by a subclass."
	 * ); return null; }
	 */

	@Override
	public void notifyObservers() {

		for (final Iterator<IRunner> iterator = this.runners.iterator(); iterator.hasNext();) {
			final IRunner runner = iterator.next();
			runner.newOperationApplied(this);
		}
	}

	@Override
	public ArrayList<E> apply(ArrayList<E> input) {
		if (input == null || input.isEmpty()) {
			throw new MiddlewareException.InvalidOperationParameterException(
					"Input data is null or empty. Cannot perform the operation.");
		}
		System.out.println("Unimplemented method! This method must be implemented by a subclass.");
		return null;
	}
}
