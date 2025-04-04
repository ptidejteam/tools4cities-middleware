package ca.concordia.encs.citydata;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertEquals;

import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;

import ca.concordia.encs.citydata.core.AppConfig;

@SpringBootTest(classes = AppConfig.class)
@AutoConfigureMockMvc
@ComponentScan(basePackages = "ca.concordia.encs.citydata.core")
public class ExpTest {

	// Test for valid steps
	@Test
	public void whenValidSteps_thenReturnSuccessMessage() throws Exception {
		String sensitive = System.getenv("CITYDATA_BLA");
		String nonSensitive = System.getenv("CITYDATA_NOT_SENSITIVE");

		assertEquals(nonSensitive, "abc123");
		assertThat(sensitive, CoreMatchers.containsString("p"));

	}

}
