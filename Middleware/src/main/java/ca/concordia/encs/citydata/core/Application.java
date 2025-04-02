package ca.concordia.encs.citydata.core;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

import ca.concordia.encs.citydata.datastores.DiskDatastore;
import ca.concordia.encs.citydata.datastores.InMemoryDataStore;
import ca.concordia.encs.citydata.datastores.MongoDataStore;

/***
 * This is the Spring Boot application entry point.
 * 
 * @Author: Gabriel C. Ullmann, Sikandar Ejaz, Rushin Makwana
 * @Date: 01/01/2025
 */

@SpringBootApplication
@ComponentScan(basePackages = "ca.concordia.encs.citydata.core.controllers")
public class Application {

	// initialize all datastore for later use
	InMemoryDataStore memoryStore = InMemoryDataStore.getInstance();
	DiskDatastore diskStore = DiskDatastore.getInstance();
	MongoDataStore mongoDataStore = new MongoDataStore();

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

}