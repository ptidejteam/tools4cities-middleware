package ca.concordia.encs.citydata.core;

import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Set;

import ca.concordia.encs.citydata.runners.SequentialRunner;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
;

@RestController
@RequestMapping("/apply")
public class ProducerController {

	@RequestMapping(value = "/sync", method = RequestMethod.POST)
	public String sync(@RequestBody String steps) {
		JsonObject errorLog = new JsonObject();
		JsonObject stepsObject = JsonParser.parseString(steps).getAsJsonObject();
		SequentialRunner deckard = null;

		try {
			deckard = ReflectionUtils.createSequentialRunner(stepsObject);

			Thread runnerTask = getThread(deckard, errorLog);
			runnerTask.join();

			if (ReflectionUtils.hasErrors(errorLog)) {
				return errorLog.toString();
			}

			IProducer<?> producer = ReflectionUtils.getProducer(ReflectionUtils.getRunnerId(deckard));
			return ReflectionUtils.getResultJSONString(producer);
		} catch (Exception e) {
			e.printStackTrace();
			return "Error: " + e.getMessage();
		}
	}

	private static Thread getThread(SequentialRunner deckard, JsonObject errorLog) {
		return new Thread(() -> {
			try {
				ReflectionUtils.runSteps(deckard);

				while (!ReflectionUtils.isRunnerDone(deckard)) {
					System.out.println("Busy waiting!");
				}
			} catch (Exception e) {
				errorLog.addProperty("runnerError", e.getMessage());
			}
		});
	}

	@RequestMapping(value = "/async", method = RequestMethod.POST)
	public String async(@RequestBody String steps) {
		JsonObject errorLog = new JsonObject();
		JsonObject stepsObject = JsonParser.parseString(steps).getAsJsonObject();
		SequentialRunner aRunner = null;

		try {
			aRunner = ReflectionUtils.createSequentialRunner(stepsObject);
			ReflectionUtils.runSteps(aRunner);

			if (ReflectionUtils.hasErrors(errorLog)) {
				return errorLog.toString();
			}

			String runnerId = ReflectionUtils.getRunnerId(aRunner);
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
			IProducer<?> producer = ReflectionUtils.getProducer(runnerId);
			if (producer != null) {
				return ReflectionUtils.getResultJSONString(producer);
			}
			return "Sorry, your request result is not ready yet. Please try again later.";
		} catch (Exception e) {
			e.printStackTrace();
			return "Error: " + e.getMessage();
		}
	}

	@RequestMapping(value = "/ping", method = RequestMethod.GET)
	public String ping() {
		Date timeObject = Calendar.getInstance().getTime();
		String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(timeObject);
		return "pong - " + timeStamp;
	}
}



class ReflectionUtils {

	public static SequentialRunner createSequentialRunner(JsonObject stepsObject) throws Exception {
		Class<?> sequentialRunnerClass = Class.forName("ca.concordia.encs.citydata.runners.SequentialRunner");
		return (SequentialRunner) sequentialRunnerClass.getDeclaredConstructor(JsonObject.class).newInstance(stepsObject);
	}

	public static void runSteps(SequentialRunner runner) throws Exception {
		Method runStepsMethod = runner.getClass().getDeclaredMethod("runSteps");
		runStepsMethod.invoke(runner);
	}

	public static boolean isRunnerDone(SequentialRunner runner) throws Exception {
		Method isDoneMethod = runner.getClass().getMethod("isDone");
		return (Boolean) isDoneMethod.invoke(runner);
	}

	public static String getRunnerId(SequentialRunner runner) throws Exception {
		Method getMetadataMethod = runner.getClass().getMethod("getMetadata", String.class);
		return (String) getMetadataMethod.invoke(runner, "id");
	}

	public static IProducer<?> getProducer(String runnerId) throws Exception {
		Class<?> dataStoreClass = Class.forName("ca.concordia.encs.citydata.datastores.InMemoryDataStore");
		Method getInstanceMethod = dataStoreClass.getDeclaredMethod("getInstance");
		IDataStore store = (IDataStore) getInstanceMethod.invoke(null);

		Method getMethod = dataStoreClass.getDeclaredMethod("get", String.class);
		return (IProducer<?>) getMethod.invoke(store, runnerId);
	}

	public static String getResultJSONString(IProducer<?> producer) throws Exception {
		Method getResultJSONStringMethod = producer.getClass().getMethod("getResultJSONString");
		return (String) getResultJSONStringMethod.invoke(producer);
	}

	public static boolean hasErrors(JsonObject errorLog) throws Exception {
		Method keySetMethod = errorLog.getClass().getDeclaredMethod("keySet");
		Set<?> keySet = (Set<?>) keySetMethod.invoke(errorLog);
		return !keySet.isEmpty();
	}
}