package ca.concordia.encs.citydata.core;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

/* This route check whether a the input query is already related to one of the producers stored 
 * in the middleware's DataStore. If so, it returns the list of producers with that match the query,
 * along with their generation timestamps.
 * 
 * 	TODO: implement route
 * 	- In the /exists route, get all Producers from datastore
 * 	- From each producer, get its query using getMetadata('query')
 * 	- Compare queryObject with query using .equals from JsonObject (do not compare strings)
 * 
 * Author: Minette
 * Date: 21-02-2025
 */

@RestController
@RequestMapping("/exists")
public class ExistsController {

	@RequestMapping(value = "/", method = RequestMethod.GET)
	public ResponseEntity<String> sync(@RequestBody String query) {

		try {
			JsonObject queryObject = JsonParser.parseString(query).getAsJsonObject();
			System.out.println("Recevied: " + queryObject);
		} catch (Exception e) {
			return ResponseEntity.status(500).body(e.getMessage());
		}

		return ResponseEntity.status(200).body("Not yet implemented.");

	}

}