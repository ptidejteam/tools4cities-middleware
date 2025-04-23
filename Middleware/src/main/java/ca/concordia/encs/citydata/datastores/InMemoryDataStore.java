package ca.concordia.encs.citydata.datastores;

import java.util.HashMap;
import java.util.Iterator;

import ca.concordia.encs.citydata.core.contracts.IDataStore;
import ca.concordia.encs.citydata.core.contracts.IProducer;
import ca.concordia.encs.citydata.core.implementations.AbstractEntity;

/**
 *
 * A DataStore that stores information in RAM only rather than an actual
 * database. ATTENTION: There is no persistence! Once the application stops
 * running, all data is lost.
 * 
 * @author Gabriel C. Ullmann
 * @date 2024-12-01
 */
public class InMemoryDataStore extends AbstractEntity implements IDataStore<IProducer<?>> {

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
