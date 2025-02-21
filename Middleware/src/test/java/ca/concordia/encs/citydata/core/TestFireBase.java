package ca.concordia.encs.citydata.core;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.database.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.FileInputStream;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class TestFireBase {

    private static FirebaseDatabase database;

    @BeforeAll
    public static void setup() throws IOException {
        FileInputStream serviceAccount =
                new FileInputStream("./src/test/data/citylayer-middleware.json");

        // Create FirebaseOptions using the static method
        FirebaseOptions options = FirebaseOptions.builder()
                .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                .setDatabaseUrl("https://citylayer-middleware-default-rtdb.firebaseio.com/")
                .build();

        FirebaseApp.initializeApp(options);
        database = FirebaseDatabase.getInstance();
    }

    @Test
    public void testFetchDataFromFirebase() {
        DatabaseReference ref = database.getReference("/");
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Assuming you expect some JSON-like structure in your database
                System.out.println(dataSnapshot.getValue());
                assertNotNull(dataSnapshot.getValue(), "Data should not be null");
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle possible errors
                System.out.println("The read failed: " + databaseError.getMessage());
            }
        });

        // Add a sleep or wait mechanism if necessary, since Firebase operations are asynchronous
        try {
            Thread.sleep(5000); // Adjust time as necessary
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
