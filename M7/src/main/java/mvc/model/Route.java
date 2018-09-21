package mvc.model;

import mvc.controller.DataReader;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Route {
    private int id;
    private String name;
    private ArrayList<Stop> stops; //need a stop class

    public Route() {
        this(-1, null);
    }

    public Route(int id, String name) {
        this(id, name, null);
    }

    public Route(int id, String name, ArrayList<Integer> stops) {
        this.id = id;
        this.name = name;
        ArrayList<Stop> potentialStops = DataReader.getStopArray();
        ArrayList<Stop> realStops = new ArrayList<>();
        ArrayList<Integer> potentialIntegers = new ArrayList<>();
        for (Stop stopToInteger : potentialStops) {
            potentialIntegers.add(stopToInteger.getId());
        }
        for (Integer stop : stops) {
            if (potentialIntegers.contains(stop)) {
                realStops.add(potentialStops.get(stop));
            }
        }
        this.stops = realStops;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public ArrayList<Stop> getStops() {
        return stops;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name; 
    }

    public String toString() {
        return "Route " + getId() + ": " + getName() + " with stops: \n"
                + getStops();
    }

    public Map<String, Object> createSaveMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("id", id);
        map.put("name", name);
        return map;
    }
}