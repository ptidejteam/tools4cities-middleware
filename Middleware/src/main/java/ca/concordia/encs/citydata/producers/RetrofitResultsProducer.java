package ca.concordia.encs.citydata.producers;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import ca.concordia.encs.citydata.core.implementations.AbstractProducer;
import ca.concordia.encs.citydata.core.contracts.IOperation;
import ca.concordia.encs.citydata.core.contracts.IProducer;
import ca.concordia.encs.citydata.core.contracts.IRunner;
import ca.concordia.encs.citydata.core.utils.RequestOptions;
import ca.concordia.encs.citydata.datastores.InMemoryDataStore;
import ca.concordia.encs.citydata.runners.SingleStepRunner;

public class RetrofitResultsProducer extends AbstractProducer<JsonObject> implements IProducer<JsonObject> {

	private JsonArray buildingIds;
	private JSONProducer jsonProducer;
	private IOperation<JsonObject> jsonProducerOperation;
	private IRunner runnerObserver;

	final Path path = Paths.get("env.json").toAbsolutePath();
	private String HUB_START_URL = "https://ngci.encs.concordia.ca/api/v1.4/session/start";
	private String HUB_RETROFIT_URL = "https://ngci.encs.concordia.ca/api/v1.4/persistence/full-retrofit-results";

	public void setBuildingIds(JsonArray buildingIds) {
		this.buildingIds = buildingIds;
	}

	@Override
	public void setOperation(IOperation operation) {
		this.jsonProducerOperation = operation;
	}

	private String getBody() {
		final JsonObject wrapper = new JsonObject();
		final JsonArray scenarioArray = new JsonArray();
		final JsonObject scenarioObject = new JsonObject();
		final List<String> scenarioNames = List.of("current status", "skin retrofit", "system retrofit and pv",
				"skin and system retrofit with pv");

		for (String scenarioName : scenarioNames) {
			scenarioObject.add(scenarioName, this.buildingIds);
		}
		scenarioArray.add(scenarioObject);
		wrapper.add("scenarios", scenarioArray);
		return wrapper.toString();
	}

	private JsonObject getEnvVariables() {
		final Path path = Paths.get("env.json").toAbsolutePath();
		String envVariables = "";
		try {
			envVariables = new String(Files.readAllBytes(path));
		} catch (IOException e) {
			// do not throw this error to users
			System.out.println(e.getMessage());
		}
		final JsonElement jsonElement = JsonParser.parseString(envVariables);
		return jsonElement.getAsJsonObject();
	}

	private RequestOptions getStartOptions() {
		JsonObject jsonEnv = getEnvVariables();
		RequestOptions startOptions = new RequestOptions();
		startOptions.returnHeaders = true;
		startOptions.addToHeaders("Username", jsonEnv.get("REACT_APP_HUB_API_USERNAME").getAsString());
		startOptions.addToHeaders("Password", jsonEnv.get("REACT_APP_HUB_API_PASSWORD").getAsString());
		startOptions.addToHeaders("Application-Uuid", jsonEnv.get("REACT_APP_HUB_API_APPLICATION_UUID").getAsString());
		startOptions.addToHeaders("accept", "application/json");
		startOptions.addToHeaders("content-type", "application/json");
		startOptions.method = "PUT";
		return startOptions;
	}

	private RequestOptions getRetrofitOptions(JsonObject startResponseHeaders) {
		JsonObject jsonEnv = getEnvVariables();
		RequestOptions retrofitOptions = new RequestOptions();
		retrofitOptions.requestBody = getBody();
		retrofitOptions.addToHeaders("token", startResponseHeaders.get("token").getAsString());
		retrofitOptions.addToHeaders("session-id", startResponseHeaders.get("session_id").getAsString());
		retrofitOptions.addToHeaders("Application-Uuid",
				jsonEnv.get("REACT_APP_HUB_API_APPLICATION_UUID").getAsString());
		retrofitOptions.addToHeaders("accept", "application/json");
		retrofitOptions.addToHeaders("content-type", "application/json");
		retrofitOptions.method = "POST";
		return retrofitOptions;
	}

	private JsonObject startHubSession() {
		try {
			final RequestOptions startOptions = this.getStartOptions();
			final JSONProducer startProducer = new JSONProducer(HUB_START_URL, startOptions);
			final SingleStepRunner deckard = new SingleStepRunner(startProducer);
			final Thread runnerTask = new Thread() {
				public void run() {
					try {
						deckard.runSteps();
						while (!deckard.isDone()) {
							System.out.println("Busy waiting!");
						}
					} catch (Exception e) {
						deckard.setAsDone();
						System.out.println(e.getMessage());
					}

				}
			};
			runnerTask.start();
			runnerTask.join();

			final InMemoryDataStore memoryStore = InMemoryDataStore.getInstance();
			final String runnerId = deckard.getMetadata("id").toString();
			final IProducer<?> storeResult = memoryStore.get(runnerId);
			if (storeResult != null) {
				final ArrayList<JsonObject> retrofitResults = (ArrayList<JsonObject>) memoryStore.get(runnerId)
						.getResult();
				if (retrofitResults != null && retrofitResults.size() > 0) {
					return retrofitResults.get(0);
				}

			}
		} catch (InterruptedException e) {
			ArrayList<JsonObject> errorMessageList = new ArrayList<>();
			JsonObject errorMessage = new JsonObject();
			errorMessage.addProperty("error", e.getMessage());
			errorMessageList.add(errorMessage);
			this.result = errorMessageList;
		}
		return new JsonObject();
	}

	@Override
	public void fetch() {

		final JsonObject errorObject = new JsonObject();

		if (this.buildingIds != null && buildingIds.size() > 0) {
			// start: get session token
			JsonObject startResponseHeaders = this.startHubSession();

			// get retrofit result
			RequestOptions requestOptions = this.getRetrofitOptions(startResponseHeaders);
			this.jsonProducer = new JSONProducer(HUB_RETROFIT_URL, requestOptions);
			this.jsonProducer.operation = this.jsonProducerOperation;
			this.jsonProducer.addObserver(this.runnerObserver);
			this.jsonProducer.fetch();
		} else {
			errorObject.addProperty("error",
					"No buildingIds informed. Please use the 'buildingIds' parameter to specify buildingIds.");
			this.result = new ArrayList<>();
			this.result.add(errorObject);
			super.addObserver(this.runnerObserver);
			super.operation = this.jsonProducerOperation;
		}

		this.applyOperation();
	}

	@Override
	public void addObserver(final IRunner aRunner) {
		this.runnerObserver = aRunner;
	}

}
