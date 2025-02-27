package ca.concordia.encs.citydata.core;

import java.util.Iterator;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import ca.concordia.encs.citydata.datastores.InMemoryDataStore;

/* This route check whether a the input query is already related to one of the producers stored 
 * in the middleware's DataStore. If so, it returns the list of producers with that match the query,
 * along with their generation timestamps.
 * 
 * TODO: report all producers, not only the first one found
 * 
 * Author: Minette
 * Date: 21-02-2025
 */

@RestController
@RequestMapping("/exists")
public class ExistsController {

	@RequestMapping(value = "/", method = RequestMethod.POST) 
    public ResponseEntity<String> sync(@RequestBody String query) {

        try {
            // Parse the user query
            JsonObject queryFromUserRequest = JsonParser.parseString(query).getAsJsonObject();
            String queryId = queryFromUserRequest.get("id").getAsString();  // assuming the query contains an "id"

            // Initialize the DataStore and retrieve all producers
            InMemoryDataStore store = InMemoryDataStore.getInstance();
            Iterator<IProducer<?>> storedProducers = store.getValues();
            
            // Create a JsonObject to hold the matching producer data
            JsonObject results = new JsonObject();
            
            while (storedProducers.hasNext()) {
                AbstractProducer producer = (AbstractProducer) storedProducers.next();
                String producerId = (String) producer.getMetadata("id");
                String timestamp = (String) producer.getMetadata("timestamp");

                // If the producer's id matches the query id, add it to the results
                if (producerId != null && producerId.equals(queryId)) {
                    JsonObject producerDetails = new JsonObject();
                    producerDetails.addProperty("timestamp", timestamp);
                    results.add(producerId, producerDetails);  // Add the producer to the results
                }
            }

            // Log the results for debugging
            System.out.println("Results: " + results.toString());

            if (results.size() > 0) {
                return ResponseEntity.status(200)
                        .body("Exists! Matching producers: " + results.toString());
            } else {
                // Return 404 when no producers are found
                return ResponseEntity.status(404).body("Does not exist.");
            }

        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error: " + e.getMessage());
        }
    }
}