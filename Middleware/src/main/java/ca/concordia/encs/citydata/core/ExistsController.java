package ca.concordia.encs.citydata.core;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import ca.concordia.encs.citydata.datastores.InMemoryDataStore;
import ca.concordia.encs.citydata.runners.SequentialRunner;

@RestController
@RequestMapping("/exists")
public class ExistsController {
    
    private final InMemoryDataStore dataStore;
    private final Gson gson;
    
    public ExistsController() {
        this.dataStore = InMemoryDataStore.getInstance();
        this.gson = new Gson();
    }
    
    public void applyQuery(String producerId, String queryBody) {
        IProducer<?> producer = dataStore.get(producerId); // Get producer by ID
        
        if (producer == null) {
            System.out.println("Producer not found for ID: " + producerId);
            return;
        }
        
        dataStore.addQuery(producerId, queryBody);
        dataStore.executeQuery(producerId, queryBody);
    }
    
    // checks whether either a producer or a runner exists in the dataStore and returns information about it
    @GetMapping("/{producerId}")
    public ResponseEntity<?> exists(@PathVariable String producerId) {
        try {
            System.out.println("🔍 Checking existence of producer: " + producerId);
            
            // Check for producer
            IProducer<?> producer = dataStore.get(producerId);
            if (producer != null) {
                String role = producer.getClass().getSimpleName();
                System.out.println("✅ Producer found! ID: " + producerId + ", Type: " + role);
                return ResponseEntity.ok()
                        .body("Producer exists - Type: " + role + ", ID: " + producerId);
            }
            
            // Check for runner
            IRunner runner = dataStore.getRunner(producerId);
            if (runner != null) {
                String role = runner.getClass().getSimpleName();
                System.out.println("✅ Runner found! ID: " + producerId + ", Type: " + role);
                return ResponseEntity.ok()
                        .body("Runner exists - Type: " + role + ", ID: " + producerId);
            }
            
            System.out.println("❌ No entity found with ID: " + producerId);
            return ResponseEntity.status(404).body("No entity found with ID: " + producerId);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Internal server error: " + e.getMessage());
        }
    }
    
	/*
	 * Processes an incoming query in the form of a JSON request body and tries to associate it with a SequentialRunner
	 * (or IProducer acting as one) in the dataStore. If a match is found, it adds the query to the corresponding entity
	 *  ==== doesn't work properly yet ====.
	 */ 
    @PostMapping("/query")
    public ResponseEntity<?> bodyExists(@RequestBody String requestBody) {
        try {
            JsonObject requestJson = gson.fromJson(requestBody, JsonObject.class);
            if (requestJson == null) {
                return ResponseEntity.badRequest().body("Invalid JSON format");
            }
            
            System.out.println("Processing query: " + requestJson.toString());
            
            // Using updated logic to find SequentialRunners
            for (String key : dataStore.getMetadataKeySet()) {
                // First try to get as runner (which is more appropriate for SequentialRunner)
                IRunner runner = dataStore.getRunner(key);
                if (runner instanceof SequentialRunner) {
                    dataStore.addQuery(key, requestJson.toString());
                    return ResponseEntity.ok()
                            .body("Found matching SequentialRunner with ID: " + key);
                }
                
                // Fallback to check if it's stored as a producer
                IProducer<?> producer = dataStore.get(key);
                if (producer instanceof SequentialRunner) {
                    dataStore.addQuery(key, requestJson.toString());
                    return ResponseEntity.ok()
                            .body("Found matching SequentialRunner with ID: " + key);
                }
            }
            
            return ResponseEntity.status(404)
                    .body("No matching SequentialRunner found for the given query");
            
        } catch (JsonSyntaxException e) {
            return ResponseEntity.status(400).body("Invalid JSON syntax: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(500)
                    .body("Internal server error while processing query: " + e.getMessage());
        }
    }
    
    // checks if a producer exists and then fetches and returns the queries associated with that producer
    @GetMapping("/queries/{producerId}")
    public ResponseEntity<?> getQueriesForProducer(@PathVariable String producerId) {
        try {
            if (!dataStore.producerExists(producerId)) {
                return ResponseEntity.status(404).body("No producer found with ID: " + producerId);
            }
            
            HashMap<String, Integer> queries = dataStore.getQueriesForProducer(producerId);
            
            if (queries.isEmpty()) {
                return ResponseEntity.ok("No queries found for producer: " + producerId);
            }
            
            return ResponseEntity.ok(gson.toJson(queries));
        } catch (Exception e) {
            return ResponseEntity.status(500)
                    .body("Error retrieving queries: " + e.getMessage());
        }
    }
    
	/*
	 * retrieves the count of a specific query associated with a producer by combining the producerId and queryHash. It looks for the
	 * count in the metadata store and returns it.
	 * ====== Doesn't work properly yet ======.
	 */
    @GetMapping("/query/count/{producerId}/{queryHash}")
    public ResponseEntity<?> getQueryCount(@PathVariable String producerId, @PathVariable String queryHash) {
        try {
            String queryId = producerId + "_" + queryHash;
            String countKey = "query_count_" + queryId;
            
            Object countObj = dataStore.getMetadata(countKey);
            int count = (countObj != null) ? (Integer)countObj : 0;
            
            Map<String, Object> response = new HashMap<>();
            response.put("producerId", producerId);
            response.put("queryHash", queryHash);
            response.put("count", count);
            
            return ResponseEntity.ok(gson.toJson(response));
        } catch (Exception e) {
            return ResponseEntity.status(500)
                    .body("Error retrieving query count: " + e.getMessage());
        }
    }
    
    @GetMapping("/all")
    public ResponseEntity<?> getAllEntities() {
        try {
            Map<String, Map<String, String>> result = new HashMap<>();
            Map<String, String> producers = new HashMap<>();
            Map<String, String> runners = new HashMap<>();
            
            // Get all metadata keys which could be entity IDs
            Set<String> allKeys = dataStore.getMetadataKeySet();
            
            // Check each key to see if it corresponds to a producer or runner
            for (String key : allKeys) {
                // Skip metadata keys that are obviously not entity IDs
                if (key.startsWith("query_") || key.contains("_count_")) {
                    continue;
                }
                
                // Try to retrieve as producer
                IProducer<?> producer = dataStore.get(key);
                if (producer != null) {
                    producers.put(key, producer.getClass().getSimpleName());
                    continue;
                }
                
                // Try to retrieve as runner
                IRunner runner = dataStore.getRunner(key);
                if (runner != null) {
                    runners.put(key, runner.getClass().getSimpleName());
                }
            }
            
            // Combine results
            result.put("producers", producers);
            result.put("runners", runners);
            
            // Count entities
            int totalEntities = producers.size() + runners.size();
            
            // Create response with detailed information
            Map<String, Object> response = new HashMap<>();
            response.put("totalEntities", totalEntities);
            response.put("producerCount", producers.size());
            response.put("runnerCount", runners.size());
            response.put("entities", result);
            
            if (totalEntities == 0) {
                return ResponseEntity.ok("No entities found in the system");
            }
            
            return ResponseEntity.ok(gson.toJson(response));
        } catch (Exception e) {
            return ResponseEntity.status(500)
                .body("Error retrieving entities: " + e.getMessage());
        }
    }
}