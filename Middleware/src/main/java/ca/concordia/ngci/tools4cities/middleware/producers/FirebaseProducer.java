package ca.concordia.ngci.tools4cities.middleware.producers;

import java.util.ArrayList;
import java.util.List;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import ca.concordia.ngci.tools4cities.middleware.middleware.AbstractProducer;
import ca.concordia.ngci.tools4cities.middleware.middleware.RequestOptions;
import ca.concordia.ngci.tools4cities.middleware.middleware.IProducer;

/**
 * This producer fetches data from Firebase Realtime Database.
 */
public class FirebaseProducer extends AbstractProducer<JsonObject> implements IProducer<JsonObject> {

    private final String databaseURL;
    private final String nodePath; // Path to the Firebase node to fetch data from
    private final RequestOptions requestOptions; // Any request options if needed

    public FirebaseProducer(String databaseURL, String nodePath, RequestOptions requestOptions) {
        this.databaseURL = databaseURL;
        this.nodePath = nodePath;
        this.requestOptions = requestOptions;

        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
    }

    @Override
    public void fetchData() throws Exception {
        final List<JsonObject> jsonObjects = new ArrayList<>();

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference(nodePath);

        // Adding a ValueEventListener to fetch data
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Retrieve JSON string from dataSnapshot
                String jsonString = dataSnapshot.getValue(String.class);

                if (jsonString != null) {
                    // Convert JSON string to object
                    final JsonElement jsonElement = JsonParser.parseString(jsonString);
                    final JsonObject jsonObject = jsonElement.getAsJsonObject();
                    jsonObjects.add(jsonObject);
                    notifyObservers(jsonObjects);
                    System.out.println("Data fetched from Firebase: " + jsonObjects);
                } else {
                    System.err.println("No data found at the specified node path.");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.err.println("Error fetching data: " + databaseError.getMessage());
            }
        });
    }
}