package ca.concordia.ngci.tools4cities.middleware.middleware;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public abstract class AbstractOperation implements IOperation {
	private final List<IConsumer<?>> listOfConsumers = new ArrayList<>();

	public AbstractOperation(final Set<IProducer<?>> setOfProducers) {
		for (Iterator<IProducer<?>> iterator = setOfProducers
				.iterator(); iterator.hasNext();) {

			final IProducer<?> producer = iterator.next();
			producer.addObserver(this);
		}
	}

	@Override
	public final void addObserver(final IConsumer<?> aConsumer) {
		this.listOfConsumers.add(aConsumer);
	}

	public final void notifyObservers() {
		try {
			for (final Iterator<IConsumer<?>> iterator = this.listOfConsumers
					.iterator(); iterator.hasNext();) {

				final IConsumer<?> consumer = iterator.next();
				consumer.newDataAvailable(this);
			}
		}
		catch (final Exception e) {
			e.printStackTrace();
		}
	}
}
