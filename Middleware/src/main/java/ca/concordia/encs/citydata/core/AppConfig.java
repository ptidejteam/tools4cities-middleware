package ca.concordia.encs.citydata.core;

import ca.concordia.encs.citydata.datastores.MongoDataStore;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.context.annotation.Bean;

@SpringBootConfiguration
public class AppConfig {

	@Bean
	public String exampleBean() {
		return "Hello, Spring Boot!";
	}
	@Bean
	public MongoDataStore mongoDataStore() {
		return new MongoDataStore();
	}
}