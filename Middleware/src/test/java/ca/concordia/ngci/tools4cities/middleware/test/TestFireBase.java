import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.FirestoreOptions;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.auth.oauth2.GoogleCredentials;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TestFireBase {

    private static FirebaseDatabase database;

    @BeforeAll
    public static void setup() throws IOException {
        FileInputStream serviceAccount =
                new FileInputStream("./src/test/data/citylayer-middleware.json");

        FirebaseOptions options = new FirebaseOptions.Builder()
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