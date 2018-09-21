package mvc.controller.DataBaseControl;

import com.google.api.core.ApiFuture;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.cloud.FirestoreClient;
import mvc.controller.DataReader;
import mvc.controller.LocationReader;
import mvc.model.Bus;
import mvc.model.Location;
import mvc.model.Route;
import mvc.model.Stop;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

/**
 * this class retrieves SaveState objects in the form of Firestore nested HashMaps. This data is used to reinstantiate
 * the BusArray with the most recent information.
 */
public class StateRetriever {

    private static Firestore db;

    /**
     * Cloud FireStore can only be instantiated once in an application, so if there is an IllegalStateException caused
     * by multiple instantiations, db will be assigned in that respective catch clause.
     * @return the ArrayList of Buses, similar in nature to that returned by the DataReader class.
     */
    public static ArrayList<Bus> retrieveData() {
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
            db = FirestoreClient.getFirestore();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (IllegalStateException e) {
            db = GenerateSaveState.getDb();
        }
        ApiFuture<QuerySnapshot> query = db.collection("SaveStates").get();
        ArrayList<Bus> buses = new ArrayList<>();
        try {
            QuerySnapshot querySnapshot = query.get();
            List<QueryDocumentSnapshot> documents = querySnapshot.getDocuments();
            for (QueryDocumentSnapshot document: documents) {
                //for each SaveState, the fields id, route, currentStop, and NextStop are retrieved from the
                //saveStateData map, a map accessed by the corresponding id key that was used to upload the SaveState
                //to Cloud Firestore in GenerateSaveState. The above-mentioned fields are also in HashMaps that have
                // to be nested further to retrieve the instance variables necessary to reinstate the classes.
                Map<String, Object> idData = document.getData();
                String id = document.getId().substring(14);
                //use of unchecked casting is necessary here because there is no way for Google to return a custom
                //Java object for the compiler to check against.
                Map<String, Object> saveStateData = (Map<String, Object>) idData.get(id);
                Map<String, Object> routeData = (Map<String, Object>) saveStateData.get("route");
                Map<String, Object> currentStopData = (Map<String, Object>) saveStateData.get("currentStop");
                Map<String, Object> nextStopData = (Map<String, Object>) saveStateData.get("nextStop");
                //this block is the data necessary to instantiate the currentStop field of a Bus object
                Number currentStopId = (Number) currentStopData.get("id");
                Number currentStopLat = (Number) currentStopData.get("latitude");
                Number currentStopLong = (Number) currentStopData.get("longitude");
                String currentStopName = (String) currentStopData.get("name");
                //this block is the data necessary to instantiate the nextStop field of a Bus object
                Number nextStopId = (Number) nextStopData.get("id");
                Number nextStopLat = (Number) nextStopData.get("latitude");
                Number nextStopLong = (Number) nextStopData.get("longitude");
                String nextStopName = (String) nextStopData.get("name");
                //because speed and riders are primitives, they do not need to be accessed by way of HashMap
                Number speed = (Number) saveStateData.get("speed");
                Number riders = (Number) saveStateData.get("riders");
                //this block serves to find the Route object from the ArrayList of Routes from DataReader by comparing
                //Route ids. this is a much simpler and more efficient way of determining the route due to the route
                //class's complicated nature and constructors. The correct route is assigned to constructorRoute.
                ArrayList<Route> routes = DataReader.getRouteArray();
                Route constructorRoute = new Route();
                int routeId = ((Number) routeData.get("id")).intValue();
                for (Route route : routes) {
                    if (route.getId() == routeId) {
                        constructorRoute = route;
                    }
                }
                Stop currentStop = new Stop(currentStopId.intValue(), currentStopName,
                        currentStopLat.doubleValue(), currentStopLong.doubleValue());
                //bus is instantiated using the above gathered information.
                Bus bus = new Bus(Integer.parseInt(document.getId().substring(14)),
                        constructorRoute,
                        new Location(currentStop), riders.intValue(),
                        speed.intValue(),
                        new Stop(nextStopId.intValue(),nextStopName, nextStopLat.doubleValue(),
                                nextStopLong.doubleValue())
                );
                //this block instantiates reader, locates the indexPointer's position based on information from the
                //currentStop object, and assigned the reader to the bus, before adding the bus to the busArray.
                LocationReader reader = new LocationReader(bus, constructorRoute, currentStop);
                ArrayList<Stop> stops = bus.getRoute().getStops();
                int indexLocation = 0;
                int counter = 0;
                for (Stop stop : stops) {
                    if (stop.equals(currentStop)) {
                        indexLocation = counter;
                    }
                    counter++;
                }
                reader.setIndexPointer(indexLocation);
                bus.setReader(reader);
                buses.add(bus);
            }
            return buses;
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return null;
    }
}
