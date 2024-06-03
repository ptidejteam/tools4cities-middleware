package ca.concordia.ngci.tools4cities.middleware.middleware;

import java.util.Iterator;
import java.util.Set;

// TODO Fix the Java generics warning
public abstract class AbstractConsumer implements IConsumer {
	public AbstractConsumer(final Set<IProducer<?>> setOfProducers) {
		for (Iterator<IProducer<?>> iterator = setOfProducers
				.iterator(); iterator.hasNext();) {

			final IProducer<?> producer = iterator.next();
			producer.addObserver(this);
		}
	}
}
