package ca.concordia.encs.citydata.core;

import java.util.Iterator;
import java.util.Map;
import java.util.UUID;

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

@RestController
@RequestMapping("/exists")

public class ExistsController {
	private final InMemoryDataStore dataStore;
    private final Gson gson;

    public ExistsController() {
        super(); // Initialize MiddlewareEntity
        this.dataStore = InMemoryDataStore.getInstance();
        this.gson = new Gson();
    }

    // This is for producers, not queries. Just to test if it works
    @GetMapping("/{uuid}")
    public ResponseEntity<String> exists(@PathVariable String uuid) {
        try {
            final UUID id = UUID.fromString(uuid);
            final IProducer<?> producer = dataStore.getProducer(id);
            if (producer != null) {
                // Check if there are any queries associated with this producer
                Map<UUID, String> producerQueries = dataStore.getQueriesForProducer(id);
                return ResponseEntity.ok()
                    .body("Producer exists with " + producerQueries.size() + " associated queries");
            }
            
            return ResponseEntity.status(404)
                .body("No producer found with UUID: " + uuid);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(400)
                .body("Invalid UUID format: " + uuid);
        } catch (Exception e) {
            return ResponseEntity.status(500)
                .body("Internal server error while checking producer: " + e.getMessage());
        }
    }

    @PostMapping("/query")
    public ResponseEntity<String> bodyExists(@RequestBody String requestBody) {
        if (requestBody == null || requestBody.isEmpty()) {
            return ResponseEntity.status(400)
                .body("Query body cannot be empty");
        }

        try {
            JsonObject requestJson = gson.fromJson(requestBody, JsonObject.class);
            
            if (requestJson == null) {
                return ResponseEntity.status(400)
                    .body("Invalid JSON format");
            }

            // First check if this exact query already exists
            UUID existingQueryId = dataStore.findQueryByBody(requestJson.toString());
            if (existingQueryId != null) {
                return ResponseEntity.ok()
                    .body("Exact query already exists with ID: " + existingQueryId);
            }

            // If no exact match, try to find a producer that matches the query
            IProducer<?> matchingProducer = dataStore.filterProducerByQuery(requestJson.toString());
            if (matchingProducer != null) {
                UUID producerId = dataStore.getUUIDForProducer(matchingProducer);
                
                // Create a new query and associate it with the producer
                UUID newQueryId = UUID.randomUUID();
                dataStore.addQuery(newQueryId, requestJson.toString(), producerId);
                
                return ResponseEntity.ok()
                    .body("Matching producer found with ID: " + producerId + ". New query created with ID: " + newQueryId);
            }
            
            return ResponseEntity.status(404)
                .body("No matching producer found for the given query");

        } catch (JsonSyntaxException e) {
            return ResponseEntity.status(400)
                .body("Invalid JSON syntax: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(500)
                .body("Internal server error while processing query: " + e.getMessage());
        }
    }

    @GetMapping("/all")
    public ResponseEntity<?> getAllProducers() {
        try {
            Iterator<IProducer<?>> producers = dataStore.iteratorOnProducers();
            StringBuilder response = new StringBuilder();
            int count = 0;
            
            while (producers.hasNext()) {
                IProducer<?> producer = producers.next();
                UUID producerId = dataStore.getUUIDForProducer(producer);
                Map<UUID, String> queries = dataStore.getQueriesForProducer(producerId);
                
                response.append("Producer ").append(producerId)
                       .append(" (").append(queries.size()).append(" queries)\n");
                count++;
            }
            
            if (count == 0) {
                return ResponseEntity.ok("No producers found in the system");
            }
            
            response.insert(0, "Found " + count + " producers:\n");
            return ResponseEntity.ok(response.toString());
            
        } catch (Exception e) {
            return ResponseEntity.status(500)
                .body("Error retrieving producers: " + e.getMessage());
        }
    }

}