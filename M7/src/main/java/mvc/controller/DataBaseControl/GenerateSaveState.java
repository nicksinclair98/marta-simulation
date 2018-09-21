package mvc.controller.DataBaseControl;

import com.google.api.core.ApiFuture;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.*;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.cloud.FirestoreClient;
import mvc.model.Bus;
import mvc.model.SaveState;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class GenerateSaveState {
    private static Firestore db;

    /**
     * When the user chooses to save data, this object loops through all active buses and creates SaveState objects out
     * of them. These SaveState objects are then uploaded to Cloud Firestore using HashMaps that correspond to variable
     * fields. The uses of nested maps is necessary for maps that corresponds
     * to (String, Object), as Google will only
     * take in primitives to their data stores in the form of instance
     * variables.
     *
     * @param buses the ArrayList of buses that the method will loop through.
     */
    public static void generate(ArrayList<Bus> buses) {
        String path = System.getProperty("user.dir");
        try {
            InputStream serviceAccount = new FileInputStream(
                    path + "/src/main/java/json"
                            + "/marta-simulation-system-firebase-adminsdk-597h3-c3547f744f.json");
            GoogleCredentials credentials = GoogleCredentials.fromStream(serviceAccount);
            FirebaseOptions options = new FirebaseOptions.Builder()
                    .setCredentials(credentials)
                    .build();
            FirebaseApp.initializeApp(options);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (IllegalStateException e) {
            //db has already been instantiated somewhere, nothing happens
        }

        for (Bus bus : buses) {
            db = FirestoreClient.getFirestore();
            System.out.println(bus.getReader());
            SaveState state = new SaveState(bus.getRoute(),bus.getLocation().getCurrentStop(),
                    bus.getReader().getNextStop(bus.getReader().getIndexPointer() + 1),
                    bus.getTimer().getUntilNextTime(), bus.getRiders(), bus.getSpeed());
            Map<String, Map<String, Object>> data = new HashMap<>();
            data.put("" + bus.getId(), state.createSaveMap());
            DocumentReference docRef = db.collection("SaveStates").document("SaveState ID: " + bus.getId());
            ApiFuture<WriteResult> result = docRef.set(data);
            try {
                System.out.println("Update time : " + result.get().getUpdateTime());
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }
    }

    public static Firestore getDb() {
        return db;
    }

    public static void deleteCollection(CollectionReference collection, int batchSize) {
        try {
            // retrieve a small batch of documents to avoid out-of-memory errors
            ApiFuture<QuerySnapshot> future = collection.limit(batchSize).get();
            int deleted = 0;
            // future.get() blocks on document retrieval
            List<QueryDocumentSnapshot> documents = future.get().getDocuments();
            for (QueryDocumentSnapshot document : documents) {
                document.getReference().delete();
                ++deleted;
            }
            if (deleted >= batchSize) {
                // retrieve and delete another batch
                deleteCollection(collection, batchSize);
            }
        } catch (Exception e) {
            System.err.println("Error deleting collection : " + e.getMessage());
        }
    }
}
