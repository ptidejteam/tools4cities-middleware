package ca.concordia.encs.citydata.producers;

import java.util.ArrayList;
import java.util.Random;

import ca.concordia.encs.citydata.core.contracts.IProducer;
import ca.concordia.encs.citydata.core.implementations.AbstractProducer;

public class RandomNumberProducer extends AbstractProducer<Integer> implements IProducer<Integer> {
	private int listSize;
	private int generationDelay;

	public void setListSize(int listSize) {
		this.listSize = listSize;
	}

	public void setGenerationDelay(int generationDelay) {
		this.generationDelay = generationDelay;
	}

	@Override
	public void fetch() {
		try {
			// if this is running for the first time, fetch
			// otherwise, just apply next operation on top of previous result
			if (this.result.isEmpty()) {
				Random random = new Random();
				final ArrayList<Integer> randomNumbers = new ArrayList<Integer>();
				for (int i = 0; i < this.listSize; i++) {
					randomNumbers.add(random.nextInt(100));
					if (this.generationDelay > 0) {
						Thread.sleep(this.generationDelay);
					}
				}
				this.result = randomNumbers;
			}
			this.applyOperation();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
