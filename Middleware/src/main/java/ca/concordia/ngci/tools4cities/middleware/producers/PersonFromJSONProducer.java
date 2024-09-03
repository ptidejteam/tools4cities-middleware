package ca.concordia.ngci.tools4cities.middleware.producers;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import ca.concordia.ngci.tools4cities.middleware.middleware.AbstractProducer;
import ca.concordia.ngci.tools4cities.middleware.middleware.IProducer;
import ca.concordia.ngci.tools4cities.middleware.middleware.Person;

// TODO This Particular JSON producer should be a subclass of a general JSON producer
public class PersonFromJSONProducer extends AbstractProducer<Person>
		implements IProducer<Person> {

	private final Gson gson = new Gson();

	public PersonFromJSONProducer() {
	}

	@Override
	public void fetchData() throws IOException {
		final String json = Files
				.readString(Paths.get("src/main/resources/Data/person.json"));
		final List<Person> data = gson.fromJson(json,
				new TypeToken<List<Person>>() {
				}.getType());
		this.notifyObservers(data);
	}

}