package ca.concordia.encs.citydata;

import static org.hamcrest.Matchers.anyOf;
import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import ca.concordia.encs.citydata.core.ReflectionUtils;
import org.hamcrest.CoreMatchers;
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

import java.lang.reflect.Method;

@SpringBootTest(classes = AppConfig.class)
@AutoConfigureMockMvc
@ComponentScan(basePackages = "ca.concordia.encs.citydata.core")
public class ApplyTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private WebApplicationContext webApplicationContext;

	private String createJsonPayload() {
		JsonObject payload = new JsonObject();
		payload.addProperty("use", "ca.concordia.encs.citydata.producers.StringProducer");

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

		JsonArray apply = new JsonArray();
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

		return payload.toString();
	}

	private void performPostRequest(String url, String contentType, String content) throws Exception {
		mockMvc.perform(post(url).contentType(contentType).content(content))
				.andExpect(status().isOk())
				.andExpect(content().string(CoreMatchers.containsString("result")));
	}

	// Test for valid steps
	@Test
	public void whenValidSteps_thenReturnSuccessMessage() throws Exception {
		String jsonPayload = createJsonPayload();
		mockMvc.perform(post("/apply/async").contentType(MediaType.APPLICATION_JSON).content(jsonPayload))
				.andExpect(status().isOk()).andExpect(content().string(containsString("Hello! The runner")));
	}

	// Test for valid runner ID
	@Test
	public void whenValidRunnerId_thenReturnResultOrNotReadyMessage() throws Exception {
		String runnerId = "d593c930-7fed-4c7b-ac52-fff946b78c32";
		mockMvc.perform(get("/apply/async/" + runnerId).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(content().string(anyOf(containsString("Sorry, your request result is not ready yet."),
						containsString("\"result\":"))));
	}

	// Test for invalid runner ID
	@Test
	public void whenInvalidRunnerId_thenReturnNotReadyMessage() throws Exception {
		String invalidRunnerId = "nonexistent-runner-id";
		mockMvc.perform(get("/apply/async/" + invalidRunnerId))
				.andExpect(status().isOk())
				.andExpect(content().string("Sorry, your request result is not ready yet. Please try again later."));
	}

	// Test for ping route
	@Test
	public void testPingRoute() throws Exception {
		System.out.println("Registered endpoints: " + webApplicationContext.getBean("requestMappingHandlerMapping"));
		mockMvc.perform(get("/apply/ping"))
				.andExpect(status().isOk())
				.andExpect(content().string(org.hamcrest.Matchers.startsWith("pong -")));
	}

	// Test for sync with valid payload
	@Test
	public void testSync() throws Exception {
		String jsonPayload = createJsonPayload();
		performPostRequest("/apply/sync", MediaType.APPLICATION_JSON_VALUE, jsonPayload);
	}

	// Test for sync with wrong media type access
	@Test
	public void testSyncWrongMediaTypeAccess() throws Exception {
		String jsonPayload = createJsonPayload();
		mockMvc.perform(post("/apply/sync").contentType("XXX").content(jsonPayload))
				.andExpect(status().is4xxClientError());
	}

	// Test for sync with wrong media type
	@Test
	public void testSyncWrongMediaType() throws Exception {
		String jsonPayload = createJsonPayload();
		mockMvc.perform(post("/apply/sync").contentType("application/XXX").content(jsonPayload))
				.andExpect(status().is2xxSuccessful());
	} // Test for broken JSON query
	@Test
	public void whenBrokenJsonQuery_thenReturnError() throws Exception {
		String brokenJson = "{ \"use\": \"ca.concordia.encs.citydata.producers.StringProducer\", \"withParams\": [ { \"name\": \"generationProcess\", \"value\": \"random\" } ";

		mockMvc.perform(post("/apply/sync")
						.contentType(MediaType.APPLICATION_JSON)
						.content(brokenJson))
				.andExpect(status().isOk())
				.andExpect(content().string(containsString("EOF")));
	}

	// Test for missing "use" field
	@Test
	public void whenMissingUseField_thenReturnError() throws Exception {
		String missingUse = "{ \"withParams\": [ { \"name\": \"generationProcess\", \"value\": \"random\" } ] }";

		mockMvc.perform(post("/apply/sync")
						.contentType(MediaType.APPLICATION_JSON)
						.content(missingUse))
				.andExpect(status().isOk())
				.andExpect(content().string(containsString("Missing 'use' field")));
	}

	// Test for missing "withParams" field
	@Test
	public void whenMissingWithParamsField_thenReturnError() throws Exception {
		String missingWithParams = "{ \"use\": \"ca.concordia.encs.citydata.producers.StringProducer\" }";

		mockMvc.perform(post("/apply/sync")
						.contentType(MediaType.APPLICATION_JSON)
						.content(missingWithParams))
				.andExpect(content().string(containsString("Missing 'withParams' field")));
	}

	// Test for non-existent param in Producer/Operation
	@Test
	public void whenNonExistentParam_thenReturnError() throws Exception {
		String nonExistentParam = "{ \"use\": \"ca.concordia.encs.citydata.producers.StringProducer\", \"withParams\": [ { \"name\": \"nonExistentParam\", \"value\": \"value\" } ] }";

		mockMvc.perform(post("/apply/sync")
						.contentType(MediaType.APPLICATION_JSON)
						.content(nonExistentParam))
				.andExpect(content().string(containsString("NoSuchMethodException")));
	}

	// Test for missing params in Operation (valid case for operations that take no params)
	@Test
	public void whenMissingParamsForOperation_thenReturnSuccess() throws Exception {
		String missingParamsForOperation = "{ \"use\": \"ca.concordia.encs.citydata.producers.StringProducer\", \"withParams\": [ { \"name\": \"generationProcess\", \"value\": \"random\" } ], \"apply\": [ { \"name\": \"ca.concordia.encs.citydata.operations.JsonFilterOperation\" } ] }";

		mockMvc.perform(post("/apply/sync")
						.contentType(MediaType.APPLICATION_JSON)
						.content(missingParamsForOperation))
				.andExpect(status().isOk())
				.andExpect(content().string(containsString("IllegalArgumentException")));
	}
	@Test
	public void testGetRequiredField() {
		JsonObject jsonObject = new JsonObject();
		jsonObject.addProperty("testField", "testValue");

		assertEquals("testValue", ReflectionUtils.getRequiredField(jsonObject, "testField").getAsString());
	}

	@Test
	public void testGetRequiredFieldMissing() {
		JsonObject jsonObject = new JsonObject();

		Exception exception = assertThrows(IllegalArgumentException.class, () -> {
			ReflectionUtils.getRequiredField(jsonObject, "missingField");
		});

		assertTrue(exception.getMessage().contains("Missing 'missingField' field"));
	}

	@Test
	public void testInstantiateClass() throws Exception {
		Object instance = ReflectionUtils.instantiateClass("java.lang.String");
		assertTrue(instance instanceof String);
	}

	@Test
	public void testSetParameters() throws Exception {
		JsonObject param1 = new JsonObject();
		param1.addProperty("name", "length");
		param1.addProperty("value", 5);

		JsonArray params = new JsonArray();
		params.add(param1);

		StringBuilder instance = new StringBuilder();
		ReflectionUtils.setParameters(instance, params);

		assertEquals(5, instance.length());
	}

	@Test
	public void testFindSetterMethod() throws Exception {
		Method method = ReflectionUtils.findSetterMethod(StringBuilder.class, "length", new JsonObject());
		assertNotNull(method);
		assertEquals("setLength", method.getName());
	}


	@Test
	public void testCapitalize() {
		assertEquals("Test", ReflectionUtils.capitalize("test"));
		assertEquals("Test", ReflectionUtils.capitalize("Test"));
		assertEquals("", ReflectionUtils.capitalize(""));
		assertNull(ReflectionUtils.capitalize(null));
	}
}