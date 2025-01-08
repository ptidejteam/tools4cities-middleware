package com.concordia.middleware.core;

import java.util.List;

public interface IOperation {

	<E> List<E> perform(List<E> inputs) throws Exception;

}
