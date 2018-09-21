package mvc.model;

import mvc.controller.BusTimer;
import mvc.controller.LocationReader;

public class Bus {
    private int id;
    private Route route;
    private Location location;
    private int riders;
    private int speed;
    private BusTimer busTimer;
    private Stop nextStop;
    private LocationReader reader;

    public Bus(int id) {
        this(id, null, null, 0, 0, null);
    }

    public Bus(int id, Route route, Location location, int riders, int speed, Stop nextStop) {
        this.id = id;
        this.route = route;
        this.location = location;
        this.riders = riders;
        this.speed = speed;
        this.nextStop = nextStop;
    }

    public int getId() {
        return this.id;
    }

    public Route getRoute() {
        return route;
    }

    public void setRoute(Route route) {
        this.route = route;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public int getRiders() {
        return riders;
    }

    public void setRiders(int riders) {
        this.riders = riders;
    }

    public int getSpeed() {
        return speed;
    }

    public void setTimer(BusTimer busTimer) {
        this.busTimer = busTimer;
    }

    public BusTimer getTimer() {
        return  this.busTimer;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public Stop getNextStop() {
        return nextStop;
    }

    public void setNextStop(Stop nextStop) {
        this.nextStop = nextStop;
    }

    public void setReader(LocationReader reader) {
        this.reader = reader;
    }

    public LocationReader getReader() {
        return reader;
    }

    @Override
    public String toString() {
        return "Bus ID: " + getId() + "\nRoute: " + getRoute().getName() +
                "\nLocation: " + getLocation().toString() + "\nRiders: " +
                getRiders() + "\nSpeed: " + getSpeed();
    }

    @Override
    public int hashCode() {
        return id * 137;
    }

}
