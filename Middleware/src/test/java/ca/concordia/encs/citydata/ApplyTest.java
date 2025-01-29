package ca.concordia.encs.citydata;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import ca.concordia.encs.citydata.core.AppConfig;

@SpringBootTest(classes = AppConfig.class)
public class ApplyTest {

	@Test
	void contextLoads() {
		System.out.println("this is test");
	}
}
