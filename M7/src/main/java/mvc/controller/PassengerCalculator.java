package mvc.controller;
import java.util.concurrent.ThreadLocalRandom;
import mvc.model.Bus;

public class PassengerCalculator { //counts passengers for a bus

    private Bus bus;
    private int initialRiders;
    private int exitingBus; //those exiting bus
    private int boardingBus; // those boarding bus

    /**
     * Creates a new PassengerCalculator object
     *
     * @param bus bus with passenger information
     */
    public PassengerCalculator(Bus bus) {
        this.bus = bus;
    }

    /**
     * Determines how many passengers are on a bus and updates the variable for
     * number of individuals boarding and leaving the bus
     *
     * @return number of passengers on a bus
     */
    public int updatePassengerCount() {
        initialRiders = bus.getRiders();
        if (initialRiders == 0) { //if bus empty just let people on
            int boardRandom = ThreadLocalRandom.current().nextInt(0, 11); //board random
            boardingBus = boardRandom;
            bus.setRiders(bus.getRiders() + boardRandom);
        } else { //let people off and let them on
            int exitRandom = ThreadLocalRandom.current().nextInt(2, 6); //exit random
            int boardRandom = ThreadLocalRandom.current().nextInt(0, 11); //board random
            exitingBus = exitRandom;
            boardingBus = boardRandom;
            if ((bus.getRiders() - exitRandom) <= 0) { //if riders - random int is 0 or less then empty bus
                bus.setRiders(0);
            } else {
                bus.setRiders(bus.getRiders() - exitRandom);
            }
            bus.setRiders(bus.getRiders() + boardRandom); //let people on bus
        }
        return bus.getRiders(); //riders on bus
    }

    /**
     * Get count of how many people are exiting bus
     *
     * @return number of people leaving bus
     */
    public int getExitingBus() {
        return exitingBus;
    }

    /**
     * Get count of how many people are boarding bus
     *
     * @return number of people boarding bus
     */
    public int getBoardingBus() {
        return boardingBus;
    }
}
