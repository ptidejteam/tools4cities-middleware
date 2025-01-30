package ca.concordia.encs.citydata;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import ca.concordia.encs.citydata.core.AppConfig;

@SpringBootTest(classes = AppConfig.class)
@AutoConfigureMockMvc
@ComponentScan(basePackages = "ca.concordia.encs.citydata.core") // Explicitly scan the package containing
																	// ProducerController

public class ApplyTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private WebApplicationContext webApplicationContext;

	@Test
	public void testPingRoute() throws Exception {
		// Print all registered endpoints
		System.out.println("Registered endpoints: " + webApplicationContext.getBean("requestMappingHandlerMapping"));

		mockMvc.perform(get("/apply/ping")).andExpect(status().isOk()) // Verify HTTP 200 status
				.andExpect(content().string(org.hamcrest.Matchers.startsWith("pong -"))); // Verify response starts with
																							// "pong -"
	}
}