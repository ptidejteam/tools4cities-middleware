package ca.concordia.encs.citydata.producers;

import java.util.ArrayList;
import java.util.Random;

import ca.concordia.encs.citydata.core.contracts.IOperation;
import ca.concordia.encs.citydata.core.contracts.IProducer;
import ca.concordia.encs.citydata.core.implementations.AbstractProducer;

/**
 *
 * This Producer outputs random strings. For test only.
 * 
 * @author Gabriel C. Ullmann, Minette Zongo
 * @date 2025-05-28
 */
public class RandomStringProducer extends AbstractProducer<String> implements IProducer<String> {

	private int stringLength = 10;
	private String generationProcess;
	private String inputString;

	public void setInputString(String inputString) {
		this.inputString = inputString;
	}

	public void setStringLength(Integer stringLength) {
		if (stringLength != null) {
			this.stringLength = stringLength;
		}
	}

	public void setGenerationProcess(String generationProcess) {
		this.generationProcess = generationProcess;
	}

	@Override
	public void setOperation(IOperation operation) {
		this.operation = operation;
	}

	@Override
	public void fetch() {
		ArrayList<String> resultSet = new ArrayList<>();
		if (this.result.isEmpty()) {
			String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
			Random random = new Random();
			StringBuilder randomString = new StringBuilder();
			for (int i = 0; i < this.stringLength; i++) {
				int index = random.nextInt(characters.length());
				randomString.append(characters.charAt(index));
			}
			resultSet.add(randomString.toString());
			this.result = resultSet;
		}
		this.applyOperation();
	}
}