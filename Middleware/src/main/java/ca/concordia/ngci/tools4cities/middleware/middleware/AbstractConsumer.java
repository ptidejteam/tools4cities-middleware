package ca.concordia.ngci.tools4cities.middleware.middleware;

import java.util.Iterator;
import java.util.List;
import java.util.Set;

// TODO Fix the Java generics warning
public abstract class AbstractConsumer<E> implements IConsumer<E> {

	public AbstractConsumer(final Set<IProducer<E>> setOfProducers) {
		for (Iterator<IProducer<E>> iterator = setOfProducers.iterator(); iterator.hasNext();) {

			final IProducer<E> producer = iterator.next();
			producer.addObserver(this);
			try {
				producer.fetchData();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	@Override
	public void newDataAvailable(List<?> data) {
		System.out.println("Implement it");
	}

}
