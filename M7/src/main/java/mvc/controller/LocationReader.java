package mvc.controller;

import mvc.model.Bus;
import mvc.model.Location;
import mvc.model.Route;
import mvc.model.Stop;
import java.util.ArrayList;

public class LocationReader {
    private Bus bus;
    private int indexPointer;
    private Route route;
    private double time;

    public LocationReader() {
        this(null, null, null);
    }

    /**
     * Creates a new LocationReader object
     *
     * @param bus bus with location information
     * @param route route that bus is on
     * @param stop stop for location information
     */
    public LocationReader(Bus bus, Route route, Stop stop) {
        this.bus = bus;
        this.route = route;
        setLocation(stop);
    }

    /**
     * Sets the current location of the bus
     *
     * @param stop current location of the bus, to be used to initialize a stop and then
     *             set to the bus's current location variable
     */
    public void setLocation(Stop stop) {
        Location loc = new Location(stop);
        bus.setLocation(loc);
    }

    public Bus getBus() {
        return this.bus;
    }

    /**
     * Gets the distance between the bus' current stop and the next stop
     * if the next Stop cannot be retrieved by means of indexing due to an indexOutOfBoundsException, the first stop
     * in the ArrayList of Stops will be assigned to the value of stop2.
     *
     * @return distance between the bus' current stop and the next stop
     */
    public double getDistance() {
        Stop stop1 = bus.getLocation().getCurrentStop();
        Stop stop2 = new Stop();
        try {
            stop2 = bus.getRoute().getStops().get(indexPointer + 1);
        } catch (IndexOutOfBoundsException e) {
            stop2 = bus.getRoute().getStops().get(0);
        }
        return DistanceCalculator.distanceBetween(stop1, stop2);
    }

    /**
     * @return the position of the bus on its route, i.e. the index of the stop where the bus is currently in the
     * ArrayList of stops, a variable contained in the Route class.
     */
    public int getIndexPointer() {
        return this.indexPointer;
    }

    public void setIndexPointer(int indexPointer) {
        this.indexPointer = indexPointer;
    }

    /**
     * Returns the total information of the bus in the form of an easy to read string format: the string includes the
     * passengers on the bus, those exiting, and those entering.
     *
     * @ passenger information
     */
    public String passengerInfo() {
        PassengerCalculator calc = new PassengerCalculator(bus);
        calc.updatePassengerCount();
        int passOn = bus.getRiders();
        int passExit = calc.getExitingBus();
        int passEnter = calc.getBoardingBus();
        return "Passengers on bus: " + passOn + "\n" + "Passengers exited bus: "
                + passExit + "\n" + "Passengers entered bus: " + passEnter
                + "\n";
    }

    /**
     * @return the String representation of the current stop and the next stop.
     */
    public String stopInfo() {
        return "Current Stop: " + bus.getLocation().getCurrentStop() + "\n" +
                "Next Stop: " + getNextStop(indexPointer + 1) + "\n";
    }


    /**
     * @param i the index of the stop that is to be retrieved, not to be confused with the indexPointer, which only
     *          points to the current stop. if the index is equal to the size of the ArrayList, the first stop in
     *          the ArrayList will be returned, following the "loop" idea for a Bus's Route.
     * @return the Stop that is to be retrieved from the ArrayList of Stops from the Route class.
     */
    public Stop getNextStop(int i) {
        ArrayList<Stop> stops = bus.getRoute().getStops();
        if (i == stops.size()) {
            Stop stop = stops.get(0);
            return stop;
        }
        return stops.get(i);
    }

    /**
     * @return the time it will take for the bus to reach its next stop from
     * its current stop
     */
    public String timeInfo() {
        time = TimeCalculator.calculateTime(bus, getDistance());
        return "Time until arrival at next stop: " + time + " minutes\n";
    }

    public double timeTillNextStop() {
        return TimeCalculator.calculateTime(bus, getDistance());
    }

    public String distanceInfo() {
        return "Distance to next stop: " + Math.round(getDistance() * 10.0) / 10.0 + " miles\n";
    }

    /**
     * Increments the indexPointer so as to keep track of the Bus along the
     * Route.
     */
    public void updateCounter() {
        indexPointer = indexPointer + 1;
    }

    /**
     * @return the String representation of all the information that can be provided by this class. To be used by
     * an application user for visual purposes only.
     */
    public String fullInfo() {
        return passengerInfo() + stopInfo() + timeInfo() + distanceInfo();
    }
}
