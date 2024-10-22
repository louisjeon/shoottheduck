package kr.jbnu.se.std;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.Firestore;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.cloud.FirestoreClient;

import java.io.FileInputStream;
import java.io.IOException;

public class FirebaseConfig {
    public static Firestore initialize() throws IOException {
        FileInputStream serviceAccount =
                new FileInputStream("src/main/resources/frog-s-adventure-firebase-adminsdk-w6le9-57488f941c.json");
        FirebaseOptions options = new FirebaseOptions.Builder()
                .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                .setDatabaseUrl("https://frog-s-adventure-default-rtdb.firebaseio.com")
                .build();

        FirebaseApp.initializeApp(options);

        Firestore db = FirestoreClient.getFirestore();
        System.out.println("AAA");
        return db;
    }
}