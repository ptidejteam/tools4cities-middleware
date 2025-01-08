package ca.concordia.ngci.tools4cities.middleware.middleware;

import java.util.List;

public interface IOperation {

	<E> List<E> perform(List<E> inputs) throws Exception;

}
