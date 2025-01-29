package ca.concordia.encs.citydata.core;

import org.springframework.boot.SpringBootConfiguration;
import org.springframework.context.annotation.Bean;

@SpringBootConfiguration
public class AppConfig {

	@Bean
	public String exampleBean() {
		return "Hello, Spring Boot!";
	}
}