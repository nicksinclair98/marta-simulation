package mvc.controller;


import javafx.scene.control.Button;

import java.util.Timer;
import java.util.TimerTask;

public class BusTimer {
    private LocationReader reader;
    private double untilNextTime;
    private Button simulate;
    private Timer timer;

    public BusTimer(LocationReader reader, Button simulate) {
        this.reader = reader;
        this.simulate = simulate;
    }

    /**
     * starts a busTimer that will decrement the untilNextTime variable until it reaches zero. When it reaches zero,
     * the Next Stop button will be automatically fired and the StopUpdater associated with the bus will then update
     * the stops. The method functions on method recursion, and is restarted every time until the user closes the app.
     */
    public void start() {
        untilNextTime = reader.timeTillNextStop();
        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                if (untilNextTime <= 0) {
                    timer.cancel();
                    start();
                    simulate.fire();
                }
                untilNextTime = untilNextTime-1;
                System.out.println(untilNextTime);
            }
        }, 0, 1000l);
    }

    public void pause() {
        this.timer.cancel();
    }

    public double getUntilNextTime() {
        return untilNextTime;
    }
}
