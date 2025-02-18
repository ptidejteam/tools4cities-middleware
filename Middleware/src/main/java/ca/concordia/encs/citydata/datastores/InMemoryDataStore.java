package ca.concordia.encs.citydata.datastores;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.UUID;

import ca.concordia.encs.citydata.core.IDataStore;
import ca.concordia.encs.citydata.core.IProducer;
import ca.concordia.encs.citydata.core.MiddlewareEntity;

/**
*
* A DataStore that stores information in RAM only rather than an actual database.
* There is no persistence! Once the application is killed, all data is lost.
*  
*/
public class InMemoryDataStore extends MiddlewareEntity implements IDataStore {

	private static final InMemoryDataStore storeInstance = new InMemoryDataStore();

    // Maps to store producers and queries
    private Map<UUID, IProducer<?>> producers = new HashMap<>();
    private Map<UUID, String> queries = new HashMap<>();
    private Map<UUID, UUID> queryProducerMap = new HashMap<>(); // Maps queryId to producerId

    private InMemoryDataStore() {}

    public static InMemoryDataStore getInstance() {
        return storeInstance;
    }

    @Override
    public void addProducer(final UUID producerId, final IProducer<?> producer) {
        producers.put(producerId, producer);
    }

    @Override
    public IProducer<?> getProducer(final UUID producerId) {
        return producers.get(producerId);
    }

    @Override
    public void deleteProducer(final UUID producerId) {
        producers.remove(producerId);
        // Remove all queries associated with this producer
        Iterator<Map.Entry<UUID, UUID>> iterator = queryProducerMap.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<UUID, UUID> entry = iterator.next();
            if (entry.getValue().equals(producerId)) {
                queries.remove(entry.getKey());
                iterator.remove();
            }
        }
    }

    @Override
    public UUID getUUIDForProducer(IProducer<?> producer) {
        for (Map.Entry<UUID, IProducer<?>> entry : producers.entrySet()) {
            if (entry.getValue().equals(producer)) {
                return entry.getKey();
            }
        }
        return null;
    }

    @Override
    public void addQuery(final UUID queryId, final String queryBody, final UUID producerId) {
        queries.put(queryId, queryBody);
        queryProducerMap.put(queryId, producerId);
    }

    @Override
    public String getQuery(final UUID queryId) {
        return queries.get(queryId);
    }

    @Override
    public void deleteQuery(final UUID queryId) {
        queries.remove(queryId);
        queryProducerMap.remove(queryId);
    }

    @Override
    public Map<UUID, String> getQueriesForProducer(UUID producerId) {
        Map<UUID, String> producerQueries = new HashMap<>();
        for (Map.Entry<UUID, UUID> entry : queryProducerMap.entrySet()) {
            if (entry.getValue().equals(producerId)) {
                UUID queryId = entry.getKey();
                producerQueries.put(queryId, queries.get(queryId));
            }
        }
        return producerQueries;
    }

    @Override
    public Iterator<IProducer<?>> iteratorOnProducers() {
        return producers.values().iterator();
    }

    @Override
    public Iterator<String> iteratorOnQueries() {
        return queries.values().iterator();
    }

    @Override
    public IProducer<?> filterProducer(String key, String value) {
        for (IProducer<?> producer : producers.values()) {
            if (producer.getResultJSONString().contains(value)) {
                return producer;
            }
        }
        return null;
    }

    @Override
    public String filterQuery(String key, String value) {
        for (String query : queries.values()) {
            if (query.contains(value)) {
                return query;
            }
        }
        return null;
    }

    @Override
    public UUID findQueryByBody(String queryBody) {
        for (Map.Entry<UUID, String> entry : queries.entrySet()) {
            if (entry.getValue().equals(queryBody)) {
                return entry.getKey();
            }
        }
        return null;
    }

    // Helper method for producer-query matching
    public IProducer<?> filterProducerByQuery(String query) {
        for (IProducer<?> producer : producers.values()) {
            if (producer.matchesQuery(query)) {
                return producer;
            }
        }
        return null;
    }
}

