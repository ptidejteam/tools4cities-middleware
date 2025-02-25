package ca.concordia.encs.citydata.core;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import ca.concordia.encs.citydata.datastores.InMemoryDataStore;
import ca.concordia.encs.citydata.runners.SequentialRunner;

// TODO: rename to ApplyController
@RestController
@RequestMapping("/apply")
public class ApplyController {
    
    private InMemoryDataStore dataStore = InMemoryDataStore.getInstance();
    
    @RequestMapping(value = "/applyQuery", method = RequestMethod.POST)
    public void applyQuery(@RequestBody String body) {
        JsonObject request = JsonParser.parseString(body).getAsJsonObject();
        String producerId = request.get("producerId").getAsString();
        String queryBody = request.get("query").getAsString();

        // Store the query
        dataStore.addQuery(producerId, queryBody);

        // Execute the query
        dataStore.executeQuery(producerId, queryBody);

        // Check if the producer exists after storing the query and results
        boolean exists = dataStore.producerExists(producerId);
        int queryCount = dataStore.getQueryCount(producerId, queryBody);
        System.out.println("Query count for producer " + producerId + ": " + queryCount);
        System.out.println("Producer exists: " + exists);
    }

    @RequestMapping(value = "/sync", method = RequestMethod.POST)
    public String sync(@RequestBody String steps) {
        String runnerId = "";
        JsonObject errorLog = new JsonObject();

        try {
            JsonObject stepsObject = JsonParser.parseString(steps).getAsJsonObject();
            SequentialRunner deckard = new SequentialRunner(stepsObject);
            runnerId = deckard.getMetadata("id").toString();
            
            // Store as a runner instead of trying to cast to IProducer
            System.out.println("Storing runner with ID: " + runnerId);
            dataStore.setRunner(runnerId, deckard);
            
            Thread runnerTask = new Thread() {
                public void run() {
                    try {
                        deckard.runSteps();
                        while (!deckard.isDone()) {
                            System.out.println("Busy waiting!");
                        }
                    } catch (Exception e) {
                        errorLog.addProperty("runnerError", e.getClass().getName() + ": " + e.getMessage());
                    }
                }
            };
            runnerTask.start();
            runnerTask.join();
        } catch (Exception e) {
            errorLog.addProperty("threadError", e.getClass().getName() + ": " + e.getMessage());
        }

        // if there are execution errors, return an error message
        if (errorLog.keySet().size() > 0) {
            return errorLog.toString();
        }

        // Get result from the producer stored by the SequentialRunner's storeResults method
        IDataStore store = InMemoryDataStore.getInstance();
        return store.get(runnerId).getResultJSONString();
    }

    @RequestMapping(value = "/async", method = RequestMethod.POST)
    public String async(@RequestBody String steps) {
        String runnerId = "";
        JsonObject errorLog = new JsonObject();
        try {
            JsonObject stepsObject = JsonParser.parseString(steps).getAsJsonObject();
            SequentialRunner deckard = new SequentialRunner(stepsObject);
            runnerId = deckard.getMetadata("id").toString();
            
            // Store as a runner instead of trying to cast to IProducer
            System.out.println("Storing runner with ID: " + runnerId);
            dataStore.setRunner(runnerId, deckard);
            
            deckard.runSteps();
        } catch (Exception e) {
            errorLog.addProperty("runnerError", e.getClass().getName() + ": " + e.getMessage());
        }

        // if there are execution errors, return an error message
        if (errorLog.keySet().size() > 0) {
            return errorLog.toString();
        }

        return "Hello! The runner " + runnerId
                + " is currently working on your request. Please make a GET request to /apply/async/ " + runnerId
                + " to find out your request status.";
    }

    @RequestMapping(value = "/async/{runnerId}", method = RequestMethod.GET)
    public String asyncId(@PathVariable("runnerId") String runnerId) {
        InMemoryDataStore store = InMemoryDataStore.getInstance();
        
        // Try to get as producer first (this is what the runner would have stored)
        IProducer<?> producer = store.get(runnerId);
        if (producer != null) {
            return producer.getResultJSONString();
        }
        
        // Check if we have a runner
        IRunner runner = store.getRunner(runnerId);
        if (runner != null) {
            if (runner.isDone()) {
                // If done, try to get the producer that should have been stored
                producer = store.get(runnerId);
                if (producer != null) {
                    return producer.getResultJSONString();
                }
                return "Runner completed but no results found.";
            } else {
                return "Runner is still processing your request. Please try again later.";
            }
        }
        
        return "Sorry, your request result is not ready yet. Please try again later.";
    }

    @RequestMapping(value = "/ping", method = RequestMethod.GET)
    public String ping() {
        Date timeObject = Calendar.getInstance().getTime();
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(timeObject);
        return "pong - " + timeStamp;
    }
}