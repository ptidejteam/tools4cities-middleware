package ca.concordia.encs.citydata.core;

import ca.concordia.encs.citydata.core.controllers.MongoDataStore;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/***
 * This is the Spring Boot base configuration file.
 * 
 * @Author: Gabriel C. Ullmann, Sikandar Ejaz
 * @Date: 01/01/2025
 */

@Configuration
public class AppConfig {
//
//	@Bean
//	public MongoDataStore mongoDataStore() {
//		return new MongoDataStore();
//	}
	@Bean
	public String exampleBean() {
		return "Hello, Spring Boot!";
	}


}