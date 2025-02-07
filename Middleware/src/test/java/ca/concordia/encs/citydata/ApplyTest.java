package ca.concordia.encs.citydata;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.Matchers.anyOf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import ca.concordia.encs.citydata.core.AppConfig;

@SpringBootTest(classes = AppConfig.class)
@AutoConfigureMockMvc
@ComponentScan(basePackages = "ca.concordia.encs.citydata.core") // Explicitly scan the package containing
																	// ProducerController

public class ApplyTest {

	@Autowired
	private MockMvc mockMvc;

	private static String jsonPayload;

	@Autowired
	private WebApplicationContext webApplicationContext;

	@BeforeAll
	public static void setUp() {
		JsonObject payload = new JsonObject();
		payload.addProperty("use", "StringProducer");
		// Add "withParams" array
		JsonArray withParams = new JsonArray();
		JsonObject param1 = new JsonObject();
		param1.addProperty("name", "generationProcess");
		param1.addProperty("value", "random");
		withParams.add(param1);
		JsonObject param2 = new JsonObject();
		param2.addProperty("name", "stringLength");
		param2.addProperty("value", 10);
		withParams.add(param2);
		payload.add("withParams", withParams);
		// Add "apply" array
		JsonArray apply = new JsonArray();
		// First operation
		JsonObject operation1 = new JsonObject();
		operation1.addProperty("name", "StringReplaceOperation");
		JsonArray operation1Params = new JsonArray();
		JsonObject operation1Param1 = new JsonObject();
		operation1Param1.addProperty("name", "searchFor");
		operation1Param1.addProperty("value", "a");
		operation1Params.add(operation1Param1);
		JsonObject operation1Param2 = new JsonObject();
		operation1Param2.addProperty("name", "replaceBy");
		operation1Param2.addProperty("value", "b");
		operation1Params.add(operation1Param2);
		operation1.add("withParams", operation1Params);
		apply.add(operation1);
		// Second operation
		JsonObject operation2 = new JsonObject();
		operation2.addProperty("name", "StringReplaceOperation");
		JsonArray operation2Params = new JsonArray();
		JsonObject operation2Param1 = new JsonObject();
		operation2Param1.addProperty("name", "searchFor");
		operation2Param1.addProperty("value", "cxsffdf");
		operation2Params.add(operation2Param1);
		JsonObject operation2Param2 = new JsonObject();
		operation2Param2.addProperty("name", "replaceBy");
		operation2Param2.addProperty("value", "c");
		operation2Params.add(operation2Param2);
		operation2.add("withParams", operation2Params);
		apply.add(operation2);
		payload.add("apply", apply);
		// Convert the payload to a JSON string
		ApplyTest.jsonPayload = payload.toString();
	}

	 //Test for POST /async with valid JSON input
	@Test
	public void whenValidSteps_thenReturnSuccessMessage() throws Exception {
		// Create the JSON payload
		JsonObject payload = new JsonObject();
		payload.addProperty("use", "ca.concordia.encs.citydata.producers.StringProducer");

		// Add "withParams" array
		JsonArray withParams = new JsonArray();
		JsonObject param1 = new JsonObject();
		param1.addProperty("name", "generationProcess");
		param1.addProperty("value", "random");
		withParams.add(param1);

		JsonObject param2 = new JsonObject();
		param2.addProperty("name", "stringLength");
		param2.addProperty("value", 10);
		withParams.add(param2);

		payload.add("withParams", withParams);

		// Add "apply" array
		JsonArray apply = new JsonArray();

		// First operation
		JsonObject operation1 = new JsonObject();
		operation1.addProperty("name", "ca.concordia.encs.citydata.operations.StringReplaceOperation");

		JsonArray operation1Params = new JsonArray();
		JsonObject operation1Param1 = new JsonObject();
		operation1Param1.addProperty("name", "searchFor");
		operation1Param1.addProperty("value", "a");
		operation1Params.add(operation1Param1);

		JsonObject operation1Param2 = new JsonObject();
		operation1Param2.addProperty("name", "replaceBy");
		operation1Param2.addProperty("value", "b");
		operation1Params.add(operation1Param2);

		operation1.add("withParams", operation1Params);
		apply.add(operation1);

		// Second operation
		JsonObject operation2 = new JsonObject();
		operation2.addProperty("name", "ca.concordia.encs.citydata.operations.StringReplaceOperation");

		JsonArray operation2Params = new JsonArray();
		JsonObject operation2Param1 = new JsonObject();
		operation2Param1.addProperty("name", "searchFor");
		operation2Param1.addProperty("value", "cxsffdf");
		operation2Params.add(operation2Param1);

		JsonObject operation2Param2 = new JsonObject();
		operation2Param2.addProperty("name", "replaceBy");
		operation2Param2.addProperty("value", "c");
		operation2Params.add(operation2Param2);

		operation2.add("withParams", operation2Params);
		apply.add(operation2);

		payload.add("apply", apply);

		// Convert the payload to a JSON string
		String jsonPayload = payload.toString();
		System.out.println("Sending JSON payload: " + jsonPayload); // Debug: Log the JSON payload

		// Send the request and validate the response
		mockMvc.perform(post("/apply/async").contentType(MediaType.APPLICATION_JSON).content(jsonPayload))
				.andExpect(status().isOk()).andExpect(content().string(containsString("Hello! The runner")));
	}

	// Test to check /apply/async with invalid JSON input
	@Test
	public void whenInvalidReturnIdWrongInput() throws Exception {
		final String invalidSteps = "invalid-json";

		mockMvc.perform(post("/apply/async").contentType(MediaType.APPLICATION_JSON).content(invalidSteps))
				.andExpect(status().isOk()).andExpect(content().string(containsString(
						"{\"runnerError\":\"java.lang.IllegalStateException: Not a JSON Object: \\\"invalid-json\\\"\"}")));
	}

	// Test to check /apply/async with invalid and unexpected JSON input type
	@Test
	public void whenInvalidReturnIdWrongMediaType() throws Exception {
		final String invalidSteps = "invalid-json";
		mockMvc.perform(post("/apply/async").contentType(MediaType.APPLICATION_NDJSON_VALUE).content(invalidSteps))
				.andExpect(status().isOk()).andExpect(content().string(containsString(
						"{\"runnerError\":\"java.lang.IllegalStateException: Not a JSON Object: \\\"invalid-json\\\"\"}")));
	}

	// Test to check /apply/async with invalid JSON input and wrong route accessed
	@Test
	public void whenInvalidReturnIdWrongRouteAccess() throws Exception {
		final String invalidSteps = "invalid-json";

		mockMvc.perform(post("/apply/asyncXXX").contentType(MediaType.APPLICATION_JSON).content(invalidSteps))
				.andExpect(status().is4xxClientError());
	}

	// Test to check /apply/async with invalid JSON input and wrong route accessed
	@Test
	public void whenInvalidReturnId4() throws Exception {
		final String invalidSteps = "invalid-json";

		mockMvc.perform(post("/applyXXX/async").contentType(MediaType.APPLICATION_NDJSON).content(invalidSteps))
				.andExpect(status().is4xxClientError());
	}

	// Test for GET /async/{runnerId} with a valid runner ID
	@Test
	public void whenValidRunnerId_thenReturnResultOrNotReadyMessage() throws Exception {
		// Valid runnerId (UUID format)
		String runnerId = "d593c930-7fed-4c7b-ac52-fff946b78c32";

		// Call GET /async/{runnerId}
		mockMvc.perform(get("/apply/async/" + runnerId).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(content().string(anyOf(containsString("Sorry, your request result is not ready yet."),
						containsString("\"result\":"))));
	}

	// Test for GET /async/{runnerId} with an invalid runner ID
	@Test
	public void whenInvalidRunnerId_thenReturnNotReadyMessage() throws Exception {
		final String invalidRunnerId = "nonexistent-runner-id";

		mockMvc.perform(get("/apply/async/" + invalidRunnerId)).andExpect(status().isOk())
				.andExpect(content().string("Sorry, your request result is not ready yet. Please try again later."));
	}

	@Test
	public void testPingRoute() throws Exception {
		// Print all registered endpoints
		System.out.println("Registered endpoints: " + webApplicationContext.getBean("requestMappingHandlerMapping"));

		mockMvc.perform(get("/apply/ping")).andExpect(status().isOk()) // Verify HTTP 200 status
				.andExpect(content().string(org.hamcrest.Matchers.startsWith("pong -"))); // Verify response starts with
																							// "pong -"
	}

	// Test to check /apply/sync with valid input
	@Test
	public void testSync() throws Exception {
		JsonObject payload = new JsonObject();
		payload.addProperty("use", "ca.concordia.encs.citydata.producers.StringProducer");
		// Add "withParams" array
		JsonArray withParams = new JsonArray();
		JsonObject param1 = new JsonObject();
		param1.addProperty("name", "generationProcess");
		param1.addProperty("value", "random");
		withParams.add(param1);
		JsonObject param2 = new JsonObject();
		param2.addProperty("name", "stringLength");
		param2.addProperty("value", 10);
		withParams.add(param2);
		payload.add("withParams", withParams);
		// Add "apply" array
		JsonArray apply = new JsonArray();
		// First operation
		JsonObject operation1 = new JsonObject();
		operation1.addProperty("name", "ca.concordia.encs.citydata.operations.StringReplaceOperation");
		JsonArray operation1Params = new JsonArray();
		JsonObject operation1Param1 = new JsonObject();
		operation1Param1.addProperty("name", "searchFor");
		operation1Param1.addProperty("value", "a");
		operation1Params.add(operation1Param1);
		JsonObject operation1Param2 = new JsonObject();
		operation1Param2.addProperty("name", "replaceBy");
		operation1Param2.addProperty("value", "b");
		operation1Params.add(operation1Param2);
		operation1.add("withParams", operation1Params);
		apply.add(operation1);
		// Second operation
		JsonObject operation2 = new JsonObject();
		operation2.addProperty("name", "ca.concordia.encs.citydata.operations.StringReplaceOperation");
		JsonArray operation2Params = new JsonArray();
		JsonObject operation2Param1 = new JsonObject();
		operation2Param1.addProperty("name", "searchFor");
		operation2Param1.addProperty("value", "cxsffdf");
		operation2Params.add(operation2Param1);
		JsonObject operation2Param2 = new JsonObject();
		operation2Param2.addProperty("name", "replaceBy");
		operation2Param2.addProperty("value", "c");
		operation2Params.add(operation2Param2);
		operation2.add("withParams", operation2Params);
		apply.add(operation2);
		payload.add("apply", apply);
		// Convert the payload to a JSON string
		ApplyTest.jsonPayload = payload.toString();
		mockMvc.perform(post("/apply/sync").contentType("application/json").content(ApplyTest.jsonPayload))
				.andExpect(status().isOk()).andExpect(content().string(containsString("result")));
	}

	// Test to check /apply/sync with invalid input
	@Test
	public void testSyncWrongMediaTypeAccess() throws Exception {
		mockMvc.perform(post("/apply/sync").contentType("XXX").content(ApplyTest.jsonPayload))
				.andExpect(status().is4xxClientError());
	}

	// Test to check /apply/sync with incomplete input
	@Test
	public void testSyncWrongMediaType() throws Exception {
		mockMvc.perform(post("/apply/sync").contentType("application/XXX").content(ApplyTest.jsonPayload))
				.andExpect(status().is2xxSuccessful());
	}

}