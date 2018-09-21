package mvc.controller;

import mvc.model.Bus;

public class TimeCalculator {

    /**
     * Creates a new TimeCalculator object
     */
    public TimeCalculator() {

    }

    /**
     * Calculates the time it will take for a bus to go from its current stop
     * to its next stop
     *
     * @param bus bus whose time to stop we want to find
     * @param distance to next stop
     * @return time to stop
     */
    public static int calculateTime(Bus bus, double distance) {
        Double distanceDouble = new Double(distance);
        return  1 + (distanceDouble.intValue() * 60 / bus.getSpeed());
    }
}
