package ca.concordia.encs.citydata.core;

import ca.concordia.encs.citydata.datastores.InMemoryDataStore;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/producers")
public class DummyProducerController extends AbstractProducer<String> implements IProducer<String> {

private JsonObject queryJson;
    
    public DummyProducerController() {
        super();
        this.result = new ArrayList<>();
    }
    
    @PostMapping("/create")
    public String createProducer(@RequestBody Map<String, Object> producerConfig) {
        System.out.println("Received producerConfig: " + producerConfig);
        Gson gson = new Gson();
        JsonObject queryJson = gson.toJsonTree(producerConfig).getAsJsonObject();
        
        if (!queryJson.has("use")) {
            return "Error: Missing 'use' field in JSON request";
        }
        
        this.queryJson = queryJson;
        
        try {
            // Generate unique IDs for both producer and query
            UUID producerId = UUID.randomUUID();
            UUID queryId = UUID.randomUUID();
            
            // Store the producer
            InMemoryDataStore.getInstance().addProducer(producerId, this);
            
            // Store the query with reference to the producer
            InMemoryDataStore.getInstance().addQuery(queryId, queryJson.toString(), producerId);
            
            return "Producer created with ID: " + producerId + ", Query stored with ID: " + queryId;
        } catch (Exception e) {
            e.printStackTrace();
            return "Error creating producer: " + e.getMessage();
        }
    }
    
    @Override
    public void fetch() {
        // Clear previous results
        this.result.clear();
        
        // Add the queryJson to results
        if (this.queryJson != null) {
        	this.result.add(this.queryJson.toString());
        }
        
        // Apply any operations and notify observers
        this.applyOperation();
    }
    
    @Override
    public boolean matchesQuery(String query) {
        if (this.queryJson == null || query == null) {
            return false;
        }
        
        try {
            Gson gson = new Gson();
            JsonObject queryToMatch = gson.fromJson(query, JsonObject.class);
            return this.queryJson.equals(queryToMatch);
        } catch (Exception e) {
            return false;
        }
    }
    
    @Override
    public String getResultJSONString() {
        // Use the parent class implementation as it already handles JsonObject types
        return super.getResultJSONString();
    }
}

