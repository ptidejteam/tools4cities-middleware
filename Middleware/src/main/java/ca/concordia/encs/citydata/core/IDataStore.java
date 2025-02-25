package ca.concordia.encs.citydata.core;

/**
 *
 * The DataStore entity is responsible for: - Create and Update records in a
 * key/value database (set method) - Read records from a key/value database (get
 * method) - Delete records in a key/value database (delete method)
 * 
 */
public interface IDataStore {

	void set(String key, IProducer<?> value);

	IProducer<?> get(String key);

	void delete(String key);
	
	// New methods for query management
    void addQuery(String producerId, String queryBody);
    void executeQuery(String producerId, String queryBody);
    int getQueryCount(String producerId, String queryBody);

}
