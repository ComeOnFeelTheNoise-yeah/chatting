package res;

import com.google.api.core.ApiFuture;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.FileInputStream;

public class Main {
   public static void main(String[] args) {
        try {
            FileInputStream serviceAccount = new FileInputStream("C:\\Program Files\\Java\\blossom-40039-firebase-adminsdk-heiuo-4006940f30.json");

            FirebaseOptions options = new FirebaseOptions.Builder()
                .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                .setDatabaseUrl("https://blossom-40039-default-rtdb.firebaseio.com/")
                .build();

            if (FirebaseApp.getApps().isEmpty()) {
                FirebaseApp.initializeApp(options);
                System.out.println("Firebase successfully initialized");
            } else {
                System.out.println("Firebase already initialized");
            }

            // Get a reference to the database
            FirebaseDatabase database = FirebaseDatabase.getInstance();

            // Get a reference to the "message" child in the database
            DatabaseReference ref = database.getReference("message");

            // Set the value of "message" to "Hello, World!"
            
       ApiFuture<Void> future = ref.setValueAsync("hhh");

       // Block on a task and get the result synchronously.
       future.get();
        } catch (Exception e) {
          System.out.println("Unexpected error occurred: " + e.getMessage());
          e.printStackTrace();
        }
    }
}
     
     
   