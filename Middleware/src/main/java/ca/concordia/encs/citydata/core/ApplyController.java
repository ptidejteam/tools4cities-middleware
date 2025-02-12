package ca.concordia.encs.citydata.core;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;

import ca.concordia.encs.citydata.datastores.InMemoryDataStore;
import ca.concordia.encs.citydata.runners.SequentialRunner;

// TODO: rename to ApplyController
@RestController
@RequestMapping("/apply")
public class ApplyController {

	@RequestMapping(value = "/sync", method = RequestMethod.POST)
	public String sync(@RequestBody String steps) {
		String runnerId = "";
		JsonObject errorLog = new JsonObject();

		try {
			JsonObject stepsObject = JsonParser.parseString(steps).getAsJsonObject();
			SequentialRunner deckard = new SequentialRunner(stepsObject);
			runnerId = deckard.getMetadata("id").toString();
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
		} catch (IllegalStateException | JsonParseException e) {
			String detailedMessage = e.getCause() != null ? e.getCause().getMessage() : e.getMessage();
			errorLog.addProperty("threadError", "Your query is not a valid JSON file. Details: " + detailedMessage);
		} catch (Exception e) {
			errorLog.addProperty("threadError", e.getClass().getName() + ": " + e.getMessage());
		}

		// if there are execution errors, return an error message
		if (errorLog.keySet().size() > 0) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, errorLog.toString());
		}

		// else, return the data
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
			deckard.runSteps();
		} catch (IllegalStateException | JsonParseException e) {
			String detailedMessage = e.getCause() != null ? e.getCause().getMessage() : e.getMessage();
			errorLog.addProperty("threadError", "Your query is not a valid JSON file. Details: " + detailedMessage);
		} catch (Exception e) {
			errorLog.addProperty("threadError", e.getClass().getName() + ": " + e.getMessage());
		}

		// if there are execution errors, return an error message
		if (errorLog.keySet().size() > 0) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, errorLog.toString());
		}

		return "Hello! The runner " + runnerId
				+ " is currently working on your request. Please make a GET request to /apply/async/ " + runnerId
				+ " to retrieve request results.";
	}

	@RequestMapping(value = "/async/{runnerId}", method = RequestMethod.GET)
	public String asyncId(@PathVariable("runnerId") String runnerId) {
		InMemoryDataStore store = InMemoryDataStore.getInstance();
		Object storeResult = store.get(runnerId);

		if (storeResult != null) {
			return store.get(runnerId).getResultJSONString();
		}
		return "Sorry, your request result is not ready yet. Please try again later.";
	}

	@RequestMapping(value = "/ping", method = RequestMethod.GET)
	public String ping() {
		Date timeObject = Calendar.getInstance().getTime();
		String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(timeObject);
		return "pong at " + timeStamp;
	}
}