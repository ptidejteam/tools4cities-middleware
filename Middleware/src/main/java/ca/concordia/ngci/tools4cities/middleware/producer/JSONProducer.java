package ca.concordia.ngci.tools4cities.middleware.producer;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import ca.concordia.ngci.tools4cities.middleware.consumer.JSONConsumer;
import ca.concordia.ngci.tools4cities.middleware.middleware.Person;


public class JSONProducer {
	private JSONConsumer consumer;
	private List<Person> data;
	
	public JSONProducer(JSONConsumer consumer) {
		this.consumer = consumer;
		loadData();
	}    

    private void loadData() {
        try {
            String json = new String(Files.readAllBytes(Paths.get("src/main/resources/Data/person.json")));
            Gson gson = new Gson();
            data = gson.fromJson(json, new TypeToken<List<Person>>() {}.getType());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    
    public void sendData() {
        for (Person person : data) {
            consumer.consumeData(person);
        }
    }
}
