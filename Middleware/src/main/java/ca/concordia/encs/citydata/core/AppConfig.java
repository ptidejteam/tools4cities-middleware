package ca.concordia.encs.citydata.core;

import org.springframework.boot.SpringBootConfiguration;
import org.springframework.context.annotation.Bean;

/***
 * This is the Spring Boot base configuration file.
 * 
 * @Author: Gabriel C. Ullmann, Sikandar Ejaz
 * @Date: 01/01/2025
 */

@SpringBootConfiguration
public class AppConfig {

	@Bean
	public String exampleBean() {
		return "Hello, Spring Boot!";
	}

}