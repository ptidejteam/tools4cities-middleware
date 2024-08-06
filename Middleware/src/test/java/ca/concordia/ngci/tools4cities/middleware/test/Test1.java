package ca.concordia.ngci.tools4cities.middleware.test;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import ca.concordia.ngci.tools4cities.middleware.producers.JSONProducer;
import ca.concordia.ngci.tools4cities.middleware.producers.CSVProducer;
import ca.concordia.ngci.tools4cities.middleware.consumers.CSVFilterConsumer;
import ca.concordia.ngci.tools4cities.middleware.consumers.JSONConsumer;
import ca.concordia.ngci.tools4cities.middleware.middleware.RequestOptions;
import ca.concordia.ngci.tools4cities.middleware.middleware.IConsumer;
import ca.concordia.ngci.tools4cities.middleware.middleware.IProducer;

public class Test1 {

	@Test
	public void testCSVFilesystem() {
		// produce energy consumption data
		final IProducer<String> producer = new CSVProducer("./src/test/data/example.csv", null);
		final Set<IProducer<String>> producers = new HashSet<IProducer<String>>();
		producers.add(producer);

		// consume energy consumption records and filter them
		final CSVFilterConsumer consumer = new CSVFilterConsumer(producers);
		List<String> filteredCSV = consumer.getResults();

		// there should be 7 records in the CSV file with this prefix
		Assertions.assertEquals(7, filteredCSV.size());
	}

//	@Test
//	public void testCSVThreads() {
//		final String postalCodePrefix = "H1B";
//		final Object[] results = new Object[2];
//
//		// same as testCSVFilesystem, but with two concurrent calls dealing with different CSV files 
//		Thread thread1 = new Thread(() -> {
//			final IProducer<String> producer1 = new CSVProducer("./src/test/data/example.csv", null);
//			final Set<IProducer<String>> producers1 = new HashSet<IProducer<String>>();
//			producers1.add(producer1);
//			final IConsumer<String> consumer1 = new CSVFilterConsumer(producers1, postalCodePrefix);
//			results[0] = consumer1;
//		});
//
//		Thread thread2 = new Thread(() -> {
//			final IProducer<String> producer2 = new CSVProducer("./src/test/data/larger.csv", null);
//			final Set<IProducer<String>> producers2 = new HashSet<IProducer<String>>();
//			producers2.add(producer2);
//			final IConsumer<String> consumer2 = new CSVFilterConsumer(producers2, postalCodePrefix);
//			results[1] = consumer2;
//		});
//
//		thread1.start();
//		thread2.start();
//
//		try {
//			thread1.join();
//			thread2.join();
//			Assertions.assertEquals(7, ((CSVFilterConsumer) results[0]).getResults().size());
//			Assertions.assertEquals(0, ((CSVFilterConsumer) results[1]).getResults().size());
//		} catch (InterruptedException e) {
//			e.printStackTrace();
//		}
//
//	}
//
//	@Test
//	public void testJSON() {
//		final RequestOptions requestOptions = new RequestOptions();
//		requestOptions.method = "GET";
//		requestOptions.contentType = "application/json";
//
//		String limitRecordCount = "100";
//		String url = "https://www.donneesquebec.ca/recherche/api/3/action/datastore_search";
//		String queryString = "?resource_id=05deae93-d9fc-4acb-9779-e0942b5e962f&limit=" + limitRecordCount;
//
//		// produce road collision data from Donées Québec
//		final IProducer<JsonObject> producer = new JSONProducer(url + queryString, requestOptions);
//		final Set<IProducer<JsonObject>> producers = new HashSet<IProducer<JsonObject>>();
//		producers.add(producer);
//
//		// consume road collision data
//		final IConsumer<JsonObject> consumer = new JSONConsumer(producers);
//		List<JsonObject> roadCollisionJSON = consumer.getResults();
//
//		// access the "records" array inside the resulting object
//		JsonObject result = roadCollisionJSON.get(0).get("result").getAsJsonObject();
//		JsonArray records = result.get("records").getAsJsonArray();
//
//		// given limitRecordCount=100, there should be 100 records
//		System.out.println(records.get(1).getAsJsonObject().get("NB_VEH_IMPLIQUES_ACCDN"));
//		Assertions.assertEquals(100, records.size());
//	}

}
