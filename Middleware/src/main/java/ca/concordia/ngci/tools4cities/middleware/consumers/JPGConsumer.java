package ca.concordia.ngci.tools4cities.middleware.consumers;

import java.awt.Image;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.google.gson.JsonObject;

import ca.concordia.ngci.tools4cities.middleware.middleware.AbstractConsumer;
import ca.concordia.ngci.tools4cities.middleware.middleware.IConsumer;
import ca.concordia.ngci.tools4cities.middleware.middleware.IProducer;
import ca.concordia.ngci.tools4cities.middleware.operations.AverageOperation;
import ca.concordia.ngci.tools4cities.middleware.operations.RotationOperation;

public class JPGConsumer extends AbstractConsumer<Image> implements IConsumer<Image> {

	private ArrayList<Image> results;

	public JPGConsumer(final Set<IProducer<Image>> setOfProducers) {
		super(setOfProducers);
	}

	@Override
	public List<Image> getResults() {
		return results;
	}

	@Override
	public final void newDataAvailable(List<Image> data) {
		this.results = new ArrayList<Image>();
		
		RotationOperation rotation = new RotationOperation();
		try {
			this.results.addAll(rotation.perform(data));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
