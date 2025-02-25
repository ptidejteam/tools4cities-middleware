package ca.concordia.encs.citydata.core;

import ca.concordia.encs.citydata.datastores.InMemoryDataStore;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/producers")
public class DummyProducerController extends AbstractProducer<String> implements IProducer<String> {

	private JsonObject queryJson;
    private final InMemoryDataStore dataStore;
    private final Gson gson;
    
    public DummyProducerController() {
        super();
        this.result = new ArrayList<>();
        this.dataStore = InMemoryDataStore.getInstance();
        this.gson = new Gson();
    }
    
    @Override
    public ArrayList<String> getResult() {
        return this.result;
    }
    
    @PostMapping("/create")
    public ResponseEntity<String> createProducer(@RequestBody Map<String, Object> producerConfig) {
        System.out.println("Received producerConfig: " + producerConfig);
        JsonObject queryJson = gson.toJsonTree(producerConfig).getAsJsonObject();
        
        if (!queryJson.has("use")) {
            return ResponseEntity.badRequest().body("Error: Missing 'use' field in JSON request");
        }
        
        this.queryJson = queryJson;
        
        try {
            // Generate producer ID
            String producerId = "producer_" + System.currentTimeMillis();
            
            // Store the producer
            dataStore.set(producerId, this);
            
            // Store the query
            String queryBody = queryJson.toString();
            dataStore.addQuery(producerId, queryBody);
            
            return ResponseEntity.ok("Producer created with ID: " + producerId);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body("Error creating producer: " + e.getMessage());
        }
    }
    
    @Override
    public void fetch() {
        this.result.clear();
        if (this.queryJson != null) {
            this.result.add(this.queryJson.toString());
        }
        this.applyOperation();
    }
    
    @Override
    public boolean matchesQuery(String query) {
        if (this.queryJson == null || query == null) {
            return false;
        }
        
        try {
            JsonObject queryToMatch = gson.fromJson(query, JsonObject.class);
            return this.queryJson.equals(queryToMatch);
        } catch (Exception e) {
            return false;
        }
    }
}

