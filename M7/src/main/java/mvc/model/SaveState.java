package mvc.model;

import java.util.Map;
import java.util.HashMap;

/**
 * SaveState is the way bus information is uploaded to Cloud Firestore. When a SaveState object is instantiated, it
 * can easily be turned into a readable map via the createSaveMap() method. All information needed upon startup
 * to create new Bus, Route, and LocationReader classes are stored in this class.
 */
public class SaveState {
    private Stop currentStop;
    private Stop nextStop;
    private Route route;
    private double timeUntilNextStop;
    private int riders;
    private int speed;

    public SaveState(Route route, Stop currentStop, Stop nextStop, double
            timeUntilNextStop, int riders, int speed) {
        this.route = route;
        this.currentStop = currentStop;
        this.nextStop = nextStop;
        this.timeUntilNextStop = timeUntilNextStop;
        this.riders = riders;
        this.speed = speed;
    }

    /**
     * @return the Google-friendly HashMap for storing data.
     */
    public Map<String, Object> createSaveMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("currentStop", currentStop.createSaveMap());
        map.put("nextStop", nextStop.createSaveMap());
        map.put("timeUntilNextStop", timeUntilNextStop);
        map.put("route", route.createSaveMap());
        map.put("riders", riders);
        map.put("speed", speed);
        return map;
    }
}
