package ca.concordia.encs.citydata.core;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import ca.concordia.encs.citydata.datastores.DiskDatastore;
import ca.concordia.encs.citydata.datastores.InMemoryDataStore;
import org.springframework.context.annotation.ComponentScan;

/**
 *
 * This is the Spring Boot application entry point.
 */

@SpringBootApplication
@ComponentScan(basePackages = "ca.concordia.encs.citydata.core.controllers")
public class Application {

	// initialize all datastore for later use
	InMemoryDataStore memoryStore = InMemoryDataStore.getInstance();
	DiskDatastore diskStore = DiskDatastore.getInstance();

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

}