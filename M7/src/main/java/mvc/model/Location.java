package mvc.model;

public class Location {
    private Stop currentStop;

    public Location() {
        this(null);
    }

    public Location(Stop stop) {
        this.currentStop = stop;
    }

    public Stop getCurrentStop() {
        return currentStop;
    }

    public void setCurrentStop(Stop stop) {
        this.currentStop = stop;
    }

    @Override
    public String toString() {
        return getCurrentStop().getName();
    }
}
