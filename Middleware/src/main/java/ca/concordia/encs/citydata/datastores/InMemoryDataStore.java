package ca.concordia.encs.citydata.datastores;

import java.util.HashMap;

import com.google.gson.JsonObject;

import ca.concordia.encs.citydata.core.IDataStore;
import ca.concordia.encs.citydata.core.IProducer;
import ca.concordia.encs.citydata.core.IRunner;
import ca.concordia.encs.citydata.core.MiddlewareEntity;
import ca.concordia.encs.citydata.runners.*;
import ca.concordia.encs.citydata.producers.*;

/**
 *
 * A DataStore that stores information in RAM only rather than an actual
 * database. There is no persistence! Once the application is killed, all data
 * is lost.
 * 
 */
public class InMemoryDataStore extends MiddlewareEntity implements IDataStore {

    private HashMap<String, Object> map = new HashMap<>();
    private static final InMemoryDataStore storeInstance = new InMemoryDataStore();

    private InMemoryDataStore() {
        this.setMetadata("role", "datastore");
    }

    public static InMemoryDataStore getInstance() {
        return storeInstance;
    }

    @Override
    public void set(String key, IProducer<?> value) {
        System.out.println("Storing producer with key: " + key + ", type: " + value.getClass().getName());
        map.put(key, value);
    }
    
    // Add a method to store runners
    public void setRunner(String key, IRunner value) {
        System.out.println("Storing runner with key: " + key + ", type: " + value.getClass().getName());
        map.put(key, value);
    }
    
    // Add a generic set method for any object
    public void setObject(String key, Object value) {
        System.out.println("Storing object with key: " + key + ", type: " + value.getClass().getName());
        map.put(key, value);
    }

    @Override
    public IProducer<?> get(String key) {
        System.out.println("🔍 Retrieving producer for ID: " + key);
        
        Object value = map.get(key);
        if (value == null) {
            System.out.println("❌ No object found for ID: " + key);
            return null;
        }
        
        if (value instanceof IProducer) {
            System.out.println("✅ Found producer: " + key + ", Type: " + value.getClass().getName());
            return (IProducer<?>) value;
        } else {
            System.out.println("⚠️ Object found with key " + key + " is not an IProducer, it's: " + value.getClass().getName());
            return null;
        }
    }
    
    // Add a method to retrieve runners
    public IRunner getRunner(String key) {
        System.out.println("🔍 Retrieving runner for ID: " + key);
        
        Object value = map.get(key);
        if (value == null) {
            System.out.println("❌ No object found for ID: " + key);
            return null;
        }
        
        if (value instanceof IRunner) {
            System.out.println("✅ Found runner: " + key + ", Type: " + value.getClass().getName());
            return (IRunner) value;
        } else {
            System.out.println("⚠️ Object found with key " + key + " is not an IRunner, it's: " + value.getClass().getName());
            return null;
        }
    }
    
    // Add a generic get method
    public Object getObject(String key) {
        return map.get(key);
    }

    @Override
    public void delete(String key) {
        map.remove(key);
    }

    public void truncate() {
        this.map.clear();
    }

    // Add query metadata to the store. Stores query details in the same map
    @Override
    public void addQuery(String producerId, String queryBody) {
        System.out.println("Adding query for producerId: " + producerId);
        
        // Calculate a unique query ID
        String queryId = producerId + "_" + Math.abs(queryBody.hashCode());
        String metadataKey = "query_" + queryId;
        
        // Store query in the map with proper metadata key
        map.put(metadataKey, queryBody);
        
        // Initialize query count (uses the metadata from MiddlewareEntity)
        if (getMetadata("query_count_" + queryId) == null) {
            this.setMetadata("query_count_" + queryId, 0);
        }
        
        System.out.println("Query added with key: " + metadataKey);
    }

    // Execute query for a specific producer
    @Override
    public void executeQuery(String producerId, String queryBody) {
        System.out.println("Executing query for producerId: " + producerId);
        
        // Get the producer
        IProducer<?> producer = get(producerId);
        if (producer == null) {
            System.out.println("Cannot execute query: Producer not found with ID: " + producerId);
            return;
        }
        
        // Generate queryId using the same logic as in addQuery
        String queryId = producerId + "_" + Math.abs(queryBody.hashCode());
        
        // Check if producer matches query
        if (producer.matchesQuery(queryBody)) {
            System.out.println("Producer matches query. Incrementing count for queryId: " + queryId);
            // Use the incrementQueryCount method from MiddlewareEntity
            this.incrementQueryCount(queryId);
        } else {
            System.out.println("Producer does not match query: " + queryId);
        }
    }

    // Get the count of queries associated with a producer and query body
    @Override
    public int getQueryCount(String producerId, String queryBody) {
        String queryId = producerId + "_" + Math.abs(queryBody.hashCode());
        String countKey = "query_count_" + queryId;

        Integer count = (Integer) getMetadata(countKey);
        return count != null ? count : 0;
    }
    
    public HashMap<String, Integer> getQueriesForProducer(String producerId) {
        HashMap<String, Integer> queries = new HashMap<>();
        
        // Look through all keys in the map for queries related to this producer
        for (String key : map.keySet()) {
            if (key.startsWith("query_" + producerId + "_")) {
                // Extract queryId from the key (remove "query_" prefix)
                String queryId = key.substring(6);
                
                // Get the query body from the map
                String queryBody = (String) map.get(key);
                
                // Get the count from metadata
                Integer count = (Integer) getMetadata("query_count_" + queryId);
                if (count == null) count = 0;
                
                queries.put(queryBody, count);
            }
        }
        
        return queries;
    }
    
    public boolean producerExists(String producerId) {
        return map.containsKey(producerId);
    }
}