package kr.jbnu.se.std;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;

import java.io.FileInputStream;
import java.io.IOException;

public class FirebaseConfig {
    private static boolean initialized = false;

    public static void initialize() throws IOException {
        if (initialized) return;

        FileInputStream serviceAccount = new FileInputStream("src/main/resources/frog-s-adventure-firebase-adminsdk-w6le9-57488f941c.json");

        FirebaseOptions options = new FirebaseOptions.Builder()
                .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                .setDatabaseUrl("https://frog-s-adventure-default-rtdb.firebaseio.com/")
                .build();

        if (FirebaseApp.getApps().isEmpty()) {
            FirebaseApp.initializeApp(options);
            initialized = true;
            System.out.println("Firebase initialized successfully.");
        }
    }
}