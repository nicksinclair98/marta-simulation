package mvc.model;

import java.util.HashMap;
import java.util.Map;

public class Stop {
    private int id;
    private String name;
    private double latitude;
    private double longitude;

    public Stop(){
        this(-1, null);
    }

    public Stop(int id, String name) {
        this(id, name, 0, 0);
    }

    public Stop(int id, String name, double latitude, double longitude) {
        this.id = id;
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public Map<String, Object> createSaveMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("id", id);
        map.put("name", name);
        map.put("latitude", latitude);
        map.put("longitude", longitude);
        return map;
    }

    @Override
    public String toString() {
        return "ID: " + getId() + " " + getName();
    }

    @Override
    public boolean equals(Object other) {
        if (other == null) return false;
        else if (!(other instanceof Stop)) return false;
        else return this.toString().equals(other.toString());
    }
}
