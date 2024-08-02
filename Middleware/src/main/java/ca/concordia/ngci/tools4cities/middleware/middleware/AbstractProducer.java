package ca.concordia.ngci.tools4cities.middleware.middleware;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public abstract class AbstractProducer<E> implements IProducer<E> {
	private final List<IConsumer<?>> listOfConsumers = new ArrayList<>();

	@Override
	public final void addObserver(final IConsumer<E> aConsumer) {
		this.listOfConsumers.add(aConsumer);
	}


	@Override
	public final void notifyObservers(final List<E> results) {
		try {
			for (final Iterator<IConsumer<?>> iterator = this.listOfConsumers
					.iterator(); iterator.hasNext();) {

				final IConsumer<?> consumer = iterator.next();
				consumer.newDataAvailable(results);
			}

		}
		catch (final Exception e) {
			e.printStackTrace();
		}
	}
}
