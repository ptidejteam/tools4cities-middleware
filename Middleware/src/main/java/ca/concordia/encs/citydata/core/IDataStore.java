package ca.concordia.encs.citydata.core;

import java.util.Iterator;
import java.util.Map;
import java.util.UUID;

/**
 *
 * The DataStore entity is responsible for: - Create and Update records in a
 * key/value database (set method) - Read records from a key/value database (get
 * method) - Delete records in a key/value database (delete method)
 * 
 */
public interface IDataStore {

	// Producer management
    void addProducer(final UUID producerId, final IProducer<?> producer);
    IProducer<?> getProducer(final UUID producerId);
    void deleteProducer(final UUID producerId);
    UUID getUUIDForProducer(IProducer<?> producer);
    
    // Query management
    void addQuery(final UUID queryId, final String queryBody, final UUID producerId);
    String getQuery(final UUID queryId);
    void deleteQuery(final UUID queryId);
    Map<UUID, String> getQueriesForProducer(UUID producerId);       
    
 // Iterators and filters
    Iterator<IProducer<?>> iteratorOnProducers();
    Iterator<String> iteratorOnQueries();
    IProducer<?> filterProducer(String key, String value);
    String filterQuery(String key, String value);
    UUID findQueryByBody(String queryBody);
    
    
    // Get the associated UUID for a Producer
   // UUID getUUIDForProducer(IProducer<?> aProducer);

    // Iterate through stored producers
   // Iterator<IProducer<?>> iteratorOnProducers();

    // Filter a producer based on a key and value
   // IProducer<?> filterProducer(String key, String value);

    // Iterate over all stored queries
    //Iterator<String> iteratorOnQueries();
    
    // Filter queries based on a key and value
    //String filterQuery(String key, String value);

}
