package ca.concordia.encs.citydata.core;

import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Set;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import ca.concordia.encs.citydata.runners.SequentialRunner;

@RestController
@RequestMapping("/apply")
public class ProducerController {

	@RequestMapping(value = "/sync", method = RequestMethod.POST)
	public String sync(@RequestBody String steps) {
		JsonObject errorLog = new JsonObject();
		JsonObject stepsObject = JsonParser.parseString(steps).getAsJsonObject();
		SequentialRunner deckard = null;

		try {
			// Instantiate SequentialRunner using reflection
			Class<?> sequentialRunnerClass = Class.forName("ca.concordia.encs.citydata.runners.SequentialRunner");
			deckard = (SequentialRunner) sequentialRunnerClass.getDeclaredConstructor(JsonObject.class)
					.newInstance(stepsObject);

			Thread runnerTask = getThread(deckard, sequentialRunnerClass, errorLog);
			runnerTask.join();

			Method keySetMethod = errorLog.getClass().getDeclaredMethod("keySet");
			Set<?> keySet = (Set<?>) keySetMethod.invoke(errorLog);
			if (!keySet.isEmpty()) {
				return errorLog.toString();
			}

			Class<?> dataStoreClass = Class.forName("ca.concordia.encs.citydata.datastores.InMemoryDataStore");
			Method getInstanceMethod = dataStoreClass.getDeclaredMethod("getInstance");
			IDataStore store = (IDataStore) getInstanceMethod.invoke(null);

			Method getMetadataMethod = sequentialRunnerClass.getMethod("getMetadata", String.class);
			String runnerId = (String) getMetadataMethod.invoke(deckard, "id");

			Method getMethod = dataStoreClass.getDeclaredMethod("get", String.class);
			IProducer<?> producer = (IProducer<?>) getMethod.invoke(store, runnerId);
			Method getResultJSONStringMethod = producer.getClass().getMethod("getResultJSONString");
			return (String) getResultJSONStringMethod.invoke(producer);
		} catch (Exception e) {
			e.printStackTrace();
			return "Error: " + e.getMessage();
		}
	}

	private static Thread getThread(SequentialRunner deckard, Class<?> sequentialRunnerClass, JsonObject errorLog) {
		SequentialRunner finalDeckard = deckard;
		Thread runnerTask = new Thread() {
			public void run() {
				try {
					// Call runSteps method using reflection
					Method runStepsMethod = sequentialRunnerClass.getDeclaredMethod("runSteps");
					runStepsMethod.invoke(finalDeckard);

					// Check if the runner is done using reflection
					Method isDoneMethod = sequentialRunnerClass.getMethod("isDone");
					while (!(Boolean) isDoneMethod.invoke(finalDeckard)) {
						System.out.println("Busy waiting!");
					}
				} catch (Exception e) {
					errorLog.addProperty("runnerError", e.getMessage());
				}
			}
		};
		runnerTask.start();
		return runnerTask;
	}

	@RequestMapping(value = "/async", method = RequestMethod.POST)
	public String async(@RequestBody String steps) {
		JsonObject errorLog = new JsonObject();
		JsonObject stepsObject = JsonParser.parseString(steps).getAsJsonObject();
		SequentialRunner aRunner = null;

		try {
			Class<?> sequentialRunnerClass = Class.forName("ca.concordia.encs.citydata.runners.SequentialRunner");
			aRunner = (SequentialRunner) sequentialRunnerClass.getDeclaredConstructor(JsonObject.class)
					.newInstance(stepsObject);

			Method runStepsMethod = sequentialRunnerClass.getDeclaredMethod("runSteps");
			runStepsMethod.invoke(aRunner);

			Method keySetMethod = errorLog.getClass().getDeclaredMethod("keySet");
			Set<?> keySet = (Set<?>) keySetMethod.invoke(errorLog);
			if (!keySet.isEmpty()) {
				return errorLog.toString();
			}

			Method getMetadataMethod = sequentialRunnerClass.getMethod("getMetadata", String.class);
			String runnerId = (String) getMetadataMethod.invoke(aRunner, "id");

			return "Hello! The runner " + runnerId
					+ " is currently working on your request. Please make a GET request to /apply/async/" + runnerId
					+ " to find out your request status.";
		} catch (Exception e) {
			e.printStackTrace();
			return "Error: " + e.getMessage();
		}
	}

	@RequestMapping(value = "/async/{runnerId}", method = RequestMethod.GET)
	public String asyncId(@PathVariable String runnerId) {
		try {
			Class<?> dataStoreClass = Class.forName("ca.concordia.encs.citydata.datastores.InMemoryDataStore");
			Method getInstanceMethod = dataStoreClass.getDeclaredMethod("getInstance");
			IDataStore store = (IDataStore) getInstanceMethod.invoke(null);

			Method getMethod = dataStoreClass.getDeclaredMethod("get", String.class);
			IProducer<?> producer = (IProducer<?>) getMethod.invoke(store, runnerId);
			if (producer != null) {
				Method getResultJSONStringMethod = producer.getClass().getMethod("getResultJSONString");
				return (String) getResultJSONStringMethod.invoke(producer);
			}
			return "Sorry, your request result is not ready yet. Please try again later.";
		} catch (Exception e) {
			e.printStackTrace();
			return "Error: " + e.getMessage();
		}
	}

	/*
	 * @GetMapping("/ping") public String ping() { return "pong - " +
	 * Instant.now().toString(); }
	 */

	@RequestMapping(value = "/ping", method = RequestMethod.GET)
	public String ping() {
		Date timeObject = Calendar.getInstance().getTime();
		String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(timeObject);
		return "pong - " + timeStamp;
	}
}