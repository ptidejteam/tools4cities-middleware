package ca.concordia.ngci.tools4cities.middleware.consumers;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.google.gson.JsonObject;

import ca.concordia.ngci.tools4cities.middleware.middleware.AbstractConsumer;
import ca.concordia.ngci.tools4cities.middleware.middleware.IConsumer;
import ca.concordia.ngci.tools4cities.middleware.middleware.IProducer;

/**
 * The JSONConsumer consumes JsonObject producers and merge results from all of them in the same ArrayList
 */
public class JSONConsumer extends AbstractConsumer<JsonObject> implements IConsumer<JsonObject> {

	private ArrayList<JsonObject> results;

	public JSONConsumer(final Set<IProducer<JsonObject>> setOfProducers) {
		super(setOfProducers);
	}

	@Override
	public List<JsonObject> getResults() {
		return results;
	}

	@Override
	public final void newDataAvailable(List<?> data) {
		this.results = new ArrayList<JsonObject>();
		this.results.addAll((ArrayList<JsonObject>) data);
	}

}
