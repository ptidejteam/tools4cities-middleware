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
 * Author: Minette
 * Date: 26-02-2025
 */

@RestController
@RequestMapping("/exists")
public class ExistsController {

    	@RequestMapping(value = "/", method = RequestMethod.GET)
    public ResponseEntity<String> sync(@RequestBody String query) {
        try {
            // Parse the query
            JsonObject queryFromUserRequest = JsonParser.parseString(query).getAsJsonObject();
            InMemoryDataStore store = InMemoryDataStore.getInstance();
            Iterator<IProducer<?>> storedProducers = store.getValues();

            // Create a list to store matching producers
            List<String> matchingProducerMessages = new ArrayList<>();

            while (storedProducers.hasNext()) {
                AbstractProducer producer = (AbstractProducer) storedProducers.next();
                JsonObject queryInProducer = (JsonObject) producer.getMetadata("query");
                String timestamp = (String) producer.getMetadata("timestamp");

                if (queryInProducer != null && queryInProducer.equals(queryFromUserRequest)) {
                    // Add message for each matching producer
                    matchingProducerMessages.add("Last producer with this query finished running at " + timestamp);
                }
            }

            // Check if any producers were found
            if (!matchingProducerMessages.isEmpty()) {
                // Combine all matching producer messages
                String responseBody = "Exists! " + String.join("; ", matchingProducerMessages);
                return ResponseEntity.status(200).body(responseBody);
            }

            return ResponseEntity.status(404).body("Does not exist.");

        } catch (Exception e) {
            return ResponseEntity.status(500).body(e.getMessage());
        }
    }

}
