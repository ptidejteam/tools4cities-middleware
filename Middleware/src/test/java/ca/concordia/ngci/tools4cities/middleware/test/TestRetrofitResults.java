package ca.concordia.ngci.tools4cities.middleware.test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import ca.concordia.ngci.tools4cities.middleware.producers.JSONProducer;
import ca.concordia.ngci.tools4cities.middleware.consumers.JSONConsumer;
import ca.concordia.ngci.tools4cities.middleware.consumers.RetrofitResultsConsumer;
import ca.concordia.ngci.tools4cities.middleware.middleware.IProducer;
import ca.concordia.ngci.tools4cities.middleware.middleware.RequestOptions;

// This test shows how would you connect to the Hub API and fetch data from it via middleware
// IMPORTANT: in order for this test to pass, you must inform valid Hub API credentials in an env.json file.
public class TestRetrofitResults {

	ArrayList<JsonObject> credentials = null;
	ArrayList<JsonObject> retrofitResults = null;

	@Test
	public void testRetrofit1() {
		JsonObject jsonEnv = null;
		Path path = Paths.get("env.json").toAbsolutePath();
		
		// load CityLayers env.json file from the project root
		// it contains credentials to the hub api
		try {
			String envVariables = new String(Files.readAllBytes(path));
			final JsonElement jsonElement = JsonParser.parseString(envVariables);
			jsonEnv = jsonElement.getAsJsonObject();
			System.out.println(envVariables);
		} catch (IOException e) {
			System.out.println("CityLayers env file is missing from Middleware folder");
		}

		String startURL = "https://ngci.encs.concordia.ca/api/v1.4/session/start";
		String retrofitURL = "https://ngci.encs.concordia.ca/api/v1.4/persistence/full-retrofit-results";

		RequestOptions startOptions = new RequestOptions();
		startOptions.method = "PUT";
		startOptions.returnHeaders = true;
		startOptions.addToHeaders("Username", jsonEnv.get("REACT_APP_HUB_API_USERNAME").getAsString());
		startOptions.addToHeaders("Password", jsonEnv.get("REACT_APP_HUB_API_PASSWORD").getAsString());
		startOptions.addToHeaders("Application-Uuid", jsonEnv.get("REACT_APP_HUB_API_APPLICATION_UUID").getAsString());
		startOptions.addToHeaders("Content-Type", "application/json");

		RequestOptions retrofitOptions = new RequestOptions();
		retrofitOptions.method = "POST";
		// requesting retrofit results for buildings 01090793, 01090794, 01090795
		retrofitOptions.requestBody = "{\"scenarios\": [{  \"current status\": [ \"01090793\",  \"01090794\", \"01090795\" ],"
				+ "	 \"skin retrofit\": [\"01090793\", \"01090794\", \"01090795\" ],"
				+ "  \"system retrofit and pv\": [\"01090793\", \"01090794\", \"01090795\" ],"
				+ "  \"skin and system retrofit with pv\": [\"01090793\", \"01090794\", \"01090795\"]" + "}]}";
		retrofitOptions.addToHeaders("Application-Uuid",
				jsonEnv.get("REACT_APP_HUB_API_APPLICATION_UUID").getAsString());
		retrofitOptions.addToHeaders("accept", "application/json");
		retrofitOptions.addToHeaders("content-type", "application/json");

		// produce retrofit data
		try {
			Thread thread1 = new Thread(() -> {
				final IProducer<JsonObject> producer = new JSONProducer(startURL, startOptions);
				final Set<IProducer<JsonObject>> producers = new HashSet<IProducer<JsonObject>>();
				producers.add(producer);

				final JSONConsumer consumer = new JSONConsumer(producers);
				this.credentials = (ArrayList<JsonObject>) consumer.getResults();
			});
			thread1.start();
			thread1.join();

			// add token and session id returned from session opening
			JsonObject startResponseHeaders = this.credentials.get(0);
			retrofitOptions.addToHeaders("token", startResponseHeaders.get("token").getAsString());
			retrofitOptions.addToHeaders("session-id", startResponseHeaders.get("session_id").getAsString());
			System.out.println(startResponseHeaders.get("token").getAsString());

			final IProducer<JsonObject> producer = new JSONProducer(retrofitURL, retrofitOptions);
			final Set<IProducer<JsonObject>> producers = new HashSet<IProducer<JsonObject>>();
			producers.add(producer);

			final RetrofitResultsConsumer consumer = new RetrofitResultsConsumer(producers);
			this.retrofitResults = (ArrayList<JsonObject>) consumer.getResults();

		} catch (Exception ex) {
			System.out.println(ex.getMessage());
		}
		float buildingAvgConsumption = this.retrofitResults.get(0).getAsJsonObject().get("01090794").getAsFloat();
		Assertions.assertEquals(3, this.retrofitResults.get(0).getAsJsonObject().keySet().size());
		Assertions.assertEquals(2147483647, Math.round(buildingAvgConsumption));
	}

}
