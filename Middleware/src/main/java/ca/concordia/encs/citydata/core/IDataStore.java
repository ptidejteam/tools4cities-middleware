package ca.concordia.encs.citydata.core;

/**
 *
 * The DataStore entity is responsible for: - Create and Update records in a
 * key/value database (set method) - Read records from a key/value database (get
 * method) - Delete records in a key/value database (delete method)
 * 
 */
public interface IDataStore<E> {

	void set(String key, E value);

	E get(String key);

	void delete(String key);

}
