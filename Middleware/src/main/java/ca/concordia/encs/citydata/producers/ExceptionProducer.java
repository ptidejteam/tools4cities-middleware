package ca.concordia.encs.citydata.producers;

import java.util.ArrayList;

import ca.concordia.encs.citydata.core.AbstractProducer;
import ca.concordia.encs.citydata.core.IProducer;

/**
 * This producer was created for the sole purpose of returning Exceptions when
 * it is not possible to throw them in Runners (e.g. when it is enclosed in a
 * run() method within a Thread)
 */
public class ExceptionProducer extends AbstractProducer<String> implements IProducer<String> {

	public ExceptionProducer(Exception e) {
		ArrayList<String> result = new ArrayList<>();
		result.add(e.getMessage());
		this.result = result;
	}

	@Override
	public void fetch() {
		System.out.println("The fetch method is unimplemented in the ExceptionProducer and shall not be used!");
	}

}
