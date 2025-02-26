package ca.concordia.encs.citydata.datastores;

import java.util.HashMap;
import java.util.Iterator;

import ca.concordia.encs.citydata.core.IDataStore;
import ca.concordia.encs.citydata.core.IProducer;
import ca.concordia.encs.citydata.core.MiddlewareEntity;

/**
 *
 * A DataStore that stores information in RAM only rather than an actual
 * database. There is no persistence! Once the application is killed, all data
 * is lost.
 * 
 */
public class InMemoryDataStore extends MiddlewareEntity implements IDataStore<IProducer<?>> {

	private HashMap<String, IProducer<?>> map = new HashMap<>();

	private static final InMemoryDataStore storeInstance = new InMemoryDataStore();

	// Private constructor prevents instantiation (this is a singleton)
	private InMemoryDataStore() {
		this.setMetadata("role", "datastore");
	}

	// Public method to provide access to the instance
	public static InMemoryDataStore getInstance() {
		return storeInstance;
	}

	@Override
	public void set(String key, IProducer<?> value) {
		map.put(key, value);
	}

	@Override
	public IProducer<?> get(String key) {
		return map.get(key);
	}

	@Override
	public Iterator<IProducer<?>> getValues() {
		return map.values().iterator();
	}

	@Override
	public void delete(String key) {
		map.remove(key);
	}

	public void truncate() {
		this.map = new HashMap<>();
	}

}
