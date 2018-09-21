package mvc.controller;

import mvc.model.Stop;
import static java.lang.Math.pow;
import static java.lang.Math.sqrt;

public class DistanceCalculator {

    /**
     * Creates a new DistanceCalculator object
     */
    public DistanceCalculator() {

    }

    /**
     * Calculates the distance between two stops
     *
     * @param stop1 stop 1
     * @param stop2 stop 2
     * @return distance between stop1 and stop2
     */

    public static double distanceBetween(Stop stop1, Stop stop2) {

        double distanceConversion = 70.0;

        double x1 = stop1.getLongitude();
        double x2 = stop2.getLongitude();
        double y1 = stop1.getLatitude();
        double y2 = stop2.getLatitude();

        double diff1 = x2 - x1;
        double diff2 = y2 - y1;

        return distanceConversion * sqrt(pow(diff1, 2) + pow(diff2, 2));

    }

}