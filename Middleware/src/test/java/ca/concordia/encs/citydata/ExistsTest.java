package ca.concordia.encs.citydata;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import ca.concordia.encs.citydata.core.AppConfig;

/* ExistsController tests
 * Author: Minette
 * Date: 2025-02-26
 */

@SpringBootTest(classes = AppConfig.class)
@AutoConfigureMockMvc
@ComponentScan(basePackages = "ca.concordia.encs.citydata.core")
public class ExistsTest {

	@Autowired
	private MockMvc mockMvc;

	@BeforeEach
	void setUp() throws Exception {
		// TODO: there is no need to setup
		// to obtain a producer, call /apply/sync passing a query from inside the test
		// (see example testQueryExistsFollowedByAsync)
		// next, call /exists with the same query
	}

	private String createQueryPayload(String producerId) {
		// FIXME: delete this method, use the PayloadFactory class instead
		// (see examples: testQueryNotExists, testBrokenJsonQuery)
		return "";
	}

	@Test
	// TODO: this test will be re-implemented soon
	void testQueryExists() throws Exception {
		String jsonPayload = createQueryPayload("randomProducerId");
	}

	@Test
	void testQueryNotExists() throws Exception {
		String jsonPayload = PayloadFactory.getExampleQuery("ckanMetadataProducerListDatasets");
		mockMvc.perform(get("/exists/").contentType(MediaType.APPLICATION_JSON).content(jsonPayload))
				.andExpect(status().isNotFound()).andExpect(content().string(containsString("[]")));
		;
	}

	@Test
	void testBrokenJsonQuery() throws Exception {
		String jsonPayload = PayloadFactory.getInvalidJson();
		mockMvc.perform(get("/exists/").contentType(MediaType.APPLICATION_JSON).content(jsonPayload))
				.andExpect(status().isInternalServerError());
	}

	@Test
	// TODO: this test will be re-implemented soon
	void testQueryExistsFollowedByAsync() throws Exception {
		// String runnerId = "";
		// String jsonPayload =
		// PayloadFactory.getExampleQuery("ckanProducerWithReplace");
		//
		// MvcResult asyncRequestResult = mockMvc
		// .perform(post("/apply/async").contentType(MediaType.APPLICATION_JSON).content(jsonPayload))
		// .andExpect(status().isOk()).andReturn();
		//
		// String text = asyncRequestResult.getResponse().getContentAsString();
		// String uuidRegex =
		// "\\b[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}\\b";
		// Pattern pattern = Pattern.compile(uuidRegex);
		// Matcher matcher = pattern.matcher(text);
		//
		// while (matcher.find()) {
		// runnerId = matcher.group();
		// break;
		// }

		// FIXME: test /exists instead of apply/async
		// mockMvc.perform(get("/apply/async/" + runnerId)).andExpect(status().isOk())
		// .andExpect(content().string(containsString("MiddlewareCollection")));

	}
}