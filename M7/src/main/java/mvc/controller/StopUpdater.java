package mvc.controller;

/**
 * the StopUpdater class utilizes abstraction to the degree that a single instance of it is related to a single Bus in
 * some sort of loop. (See BusScreen starting on line 40). The StopUpdater takes in a
 * LocationReader(Bus, Route, Stop (the first stop on the route usually when it is instantiated). Because the
 * LocationReader is tied to the bus, and the reader is a private variable of StopUpdater, so an instantiated
 * StopUpdater has the ability to access all aspects of the Bus, its Route, and its Stops through the reader, which
 * has the ability to decipher that information. See the documentation on LocationReader for more information.
 */
public class StopUpdater {

    private LocationReader reader;

    public StopUpdater(LocationReader reader) {this.reader = reader;}

    /**
     * If the indexPointer points to the last stop of the route, i.e. the integer of the index's position points to the
     * size of the ArrayList of Stops minus one, the reader's indexPointer will be set to -1, so that when the
     * updateCounter() method is called at the end of nextStop(), the indexPointer will point to 0, or the first stop
     * in the route, completing the "loop" of the route. The location of the bus will be set to index 0 by way of
     * reader.setLocation(). Otherwise, the new Location of the bus will be set to the Stop in the ArrayList of Stops
     * that is equal to indexPointer + 1.
     */
    public void nextStop() {
        if (reader.getIndexPointer() + 1
                == reader.getBus().getRoute().getStops().size()) {
            reader.setIndexPointer(-1);
            reader.setLocation(
                    reader.getBus()
                            .getRoute()
                            .getStops()
                            .get(0)
            );
            reader.getBus().setNextStop(
                    reader.getBus().getRoute().getStops().get(0)
            );
        } else {
            reader.setLocation(
                    reader.getBus()
                            .getRoute()
                            .getStops()
                            .get(reader.getIndexPointer() + 1)
            );
            reader.getBus().setNextStop(
                    reader.getNextStop(reader.getIndexPointer() + 1)
            );
        }
        reader.updateCounter();
    }
}
