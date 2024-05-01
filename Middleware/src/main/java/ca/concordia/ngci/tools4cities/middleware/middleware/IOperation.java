package ca.concordia.ngci.tools4cities.middleware.middleware;

import java.util.Set;

import ca.concordia.ngci.tools4cities.middleware.producer.IProducer;

public interface IOperation<T> {
	IProducer fetchdata(Set<IProducer> p);
}
