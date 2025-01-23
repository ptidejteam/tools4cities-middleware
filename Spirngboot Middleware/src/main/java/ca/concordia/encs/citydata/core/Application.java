package ca.concordia.encs.citydata.core;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import ca.concordia.encs.citydata.datastores.InMemoryDataStore;


/**
 *
 * This is the Micronaut application entrypoint
 * 
 */
@SpringBootApplication
public class Application {

	IDataStore store = InMemoryDataStore.getInstance();

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

}