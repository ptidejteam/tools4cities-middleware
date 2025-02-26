package ca.concordia.encs.citydata.core;

import ca.concordia.encs.citydata.datastores.MongoDataStore;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import ca.concordia.encs.citydata.datastores.DiskDatastore;
import ca.concordia.encs.citydata.datastores.InMemoryDataStore;

/**
 *
 * This is the Spring Boot application entry point.
 */

@SpringBootApplication
public class Application {

	// initialize all datastore for later use
	InMemoryDataStore memoryStore = InMemoryDataStore.getInstance();
	DiskDatastore diskStore = DiskDatastore.getInstance();
	MongoDataStore mongoDataStore = new MongoDataStore();
	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

}