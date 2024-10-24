package ca.concordia.ngci.tools4cities.middleware.consumers;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.google.gson.JsonObject;

import ca.concordia.ngci.tools4cities.middleware.middleware.AbstractConsumer;
import ca.concordia.ngci.tools4cities.middleware.middleware.IConsumer;
import ca.concordia.ngci.tools4cities.middleware.middleware.IProducer;

/**
 * The FirebaseConsumer consumes JsonObject producers and merges results from all of them in the same ArrayList.
 */
public class FirebaseConsumer extends AbstractConsumer<JsonObject> implements IConsumer<JsonObject> {

    private ArrayList<JsonObject> results;

    public FirebaseConsumer(final Set<IProducer<JsonObject>> setOfProducers) {
        super(setOfProducers);
        this.results = new ArrayList<>();
    }

    @Override
    public List<JsonObject> getResults() {
        return results;
    }

    @Override
    public final void newDataAvailable(List<JsonObject> data) {
        // Clear the previous results and add the new data
        this.results.clear();
        this.results.addAll(data);
    }
}