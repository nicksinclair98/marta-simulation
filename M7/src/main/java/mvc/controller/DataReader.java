package mvc.controller;

import java.util.ArrayList;
import java.util.Scanner;
import mvc.model.*;

public class DataReader {
    private static ArrayList<Stop> stopArray;
    private static ArrayList<Bus> busArray;
    private static ArrayList<Route> routeArray;

    public static void addStops(Scanner scanner) {
        stopArray = new ArrayList<>();
        scanner.useDelimiter(",|\n");
        while (scanner.hasNext()) {
            String word = scanner.next();
            if (word.contains("stop".subSequence(0,3))) {
                 int id = Integer.parseInt(scanner.next());
                 String name = scanner.next();
                 scanner.next();
                 double latitude = Double.parseDouble(scanner.next());
                 double longitude = Double.parseDouble(scanner.next());
                 Stop stop = new Stop(id, name, latitude, longitude);
                 stopArray.add(stop);
            }
        }
    }

    public static void addRoutes(Scanner scanner) {
        routeArray = new ArrayList<>();
        scanner.useDelimiter(",|\n");
        while (scanner.hasNext()) {
            String word = scanner.next();
            if (word.contains("route".subSequence(0,4))) {
                int id = Integer.parseInt(scanner.next());
                scanner.next();
                String name = scanner.next();
                ArrayList<Integer> stops = new ArrayList<>();
                int nextStop = Integer.parseInt(scanner.next());
                while (scanner.hasNextInt()) {
                    try {
                        System.out.println(nextStop);
                        stops.add(nextStop);
                        nextStop = Integer.parseInt(scanner.next());
                    } catch (Exception e){
                        System.out.println("could not add next stop");
                    }
                }
                Route route = new Route(id, name, stops);
                routeArray.add(route);
            }
        }
    }

    public static void addBus(Scanner scanner) {
        busArray = new ArrayList<>();
        scanner.useDelimiter(",|\n");
        while (scanner.hasNext()) {
            String word = scanner.next();
            if (word.contains("bus".subSequence(0,2))) {
                int id = Integer.parseInt(scanner.next());
                int routeId = Integer.parseInt(scanner.next());
                Route route = getRouteArray().get(routeId);
                Location loc = new Location();
                scanner.next();
                scanner.next();
                loc.setCurrentStop(route.getStops().get(0));
                int riders = Integer.parseInt(scanner.next());
                int speed = Integer.parseInt(scanner.next().substring(0,2));
                Bus bus = new Bus(id, route, loc, riders, speed, route.getStops().get(1)); //getting next stop from routes
                System.out.println(bus.getId());
                busArray.add(bus);
            }
        }
        scanner.close();
    }

    public static ArrayList<Stop> getStopArray() {
        return stopArray;
    }

    public static ArrayList<Route> getRouteArray() {
        return routeArray;
    }

    public static ArrayList<Bus> getBusArray() {
        return busArray;
    }

}
