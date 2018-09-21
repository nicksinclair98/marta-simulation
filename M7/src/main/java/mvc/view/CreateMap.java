package mvc.view;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.property.SimpleStringProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import javafx.util.Duration;
import mvc.controller.*;
import mvc.model.Bus;
import mvc.model.Route;
import mvc.model.Stop;
import java.util.*;

public class CreateMap extends WelcomeScreen {

    public static void show(Stage primaryStage) {
        VBox vbox = new VBox();
        ArrayList<Stop> stops = DataReader.getStopArray();
        Group group = new Group();
        StackPane displayStack = new StackPane();
        Circle displayCircle = new Circle(100);
        displayCircle.setFill(Color.TRANSPARENT);
        displayCircle.setStrokeWidth(2);
        displayCircle.setStroke(Paint.valueOf("#039ED3"));
        SimpleStringProperty displayStringP = new SimpleStringProperty();
        String displayString = "Click on a bus or stop\n for information";
        displayStringP.setValue(displayString);
        Text displayText = new Text();
        displayText.setFont(new Font(15));
        displayText.setTextAlignment(TextAlignment.CENTER);
        displayText.textProperty().bind(displayStringP);
        displayStack.getChildren().addAll(displayCircle, displayText);
        displayStack.setLayoutX(700);
        displayStack.setLayoutY(-350);
        group.getChildren().add(displayStack);
        for (Stop stop : stops) {
            Button label = new Button(stop.getName());
            label.setOnAction(e -> {
                displayStringP.setValue(stop.toString());
            });
            label.setLayoutX(stop.getLongitude()* 3500);
            label.setLayoutY(-1 * stop.getLatitude() * 3500);
            group.getChildren().add(label);
        }
        ArrayList<Route> routes = DataReader.getRouteArray();
        HBox buttonBox = new HBox();
        buttonBox.setAlignment(Pos.CENTER);
        for (Route route : routes) {
            Button routeButton = new Button(route.getName());
            routeButton.setOnAction(e -> {
                ArrayList<Stop> routeStops = route.getStops();
                Random random = new Random();
                Color c = Color.rgb(random.nextInt(180),random.nextInt(180),random.nextInt(180));
                Stop nextStop = new Stop();
                for (int i = 0; i < routeStops.size(); i++) {
                    Stop currentStop = routeStops.get(i);
                    if (i == routeStops.size() -1) {
                        nextStop = routeStops.get(0);
                    } else {
                        nextStop = routeStops.get(i + 1);
                    }
                    Line line = new Line(currentStop.getLongitude() * 3500 + 3, -1 * currentStop.getLatitude() * 3500 + 3,
                            nextStop.getLongitude() * 3500 + 3, -1 * nextStop.getLatitude() * 3500 + 3);
                    line.setStroke(Paint.valueOf(c.toString()));
                    line.setStrokeWidth(5);
                    group.getChildren().add(line);
                    line.toBack();
                }
            });
            buttonBox.getChildren().add(routeButton);
        }
        Button backButton = new Button("Go Back");
        buttonBox.getChildren().add(backButton);
        backButton.setOnAction(e -> {
            primaryStage.close();
            showApplication();
        });
        ArrayList<Bus> buses = DataReader.getBusArray();
        for (Bus bus : buses) {
            LocationReader reader = new LocationReader(bus, bus.getRoute(), bus.getRoute().getStops().get(0));
            bus.setReader(reader);
            Group busGroup = new Group();
            SimpleStringProperty busInfoP = new SimpleStringProperty();
            Text busInfo = new Text();
            Button busLabel = new Button("Bus " + bus.getId());
            busGroup.getChildren().addAll(busLabel, busInfo);
            Button simulate = new Button("Next Stop");
            BusTimer busTimer = new BusTimer(bus.getReader(), simulate);
            PassengerCalculator passCalc = new PassengerCalculator(bus);
            busLabel.setOnAction(e -> {
                displayText.textProperty().unbind();
                SimpleStringProperty newDisplayString = new SimpleStringProperty();
                displayText.textProperty().bind(newDisplayString);
                Timer displayUpdater = new Timer();
                displayUpdater.scheduleAtFixedRate(new TimerTask() {
                    @Override
                    public void run() {
                        double timeCount = busTimer.getUntilNextTime();
                        String newCount = modifyCountdown(bus, timeCount);
                        newDisplayString.setValue(newCount);
                    }
                }, 0, 1000);
            });
            Random random = new Random();
            Color c1 = Color.rgb(random.nextInt(255),random.nextInt(255),random.nextInt(255));
            busLabel.setBackground(new Background(new BackgroundFill(Paint.valueOf(c1.toString()), new CornerRadii(5), new Insets(3))));
            group.getChildren().add(busGroup);
//            busLabel.setLayoutX(bus.getLocation().getCurrentStop().getLongitude() * 3500);
//            busLabel.setLayoutY(bus.getLocation().getCurrentStop().getLatitude() * 3500);
            StopUpdater updater = new StopUpdater(bus.getReader());
            busTimer.start();
            Timeline startTimeline = new Timeline();
            startTimeline.getKeyFrames().addAll(
                    new KeyFrame(Duration.ZERO, // set start position at 0
                            new KeyValue(busGroup.translateXProperty(), bus.getLocation().getCurrentStop().getLongitude() * 3500 + 3),
                            new KeyValue(busGroup.translateYProperty(), -1 * bus.getLocation().getCurrentStop().getLatitude() * 3500 + 3)),
                    new KeyFrame(new Duration(busTimer.getUntilNextTime()* 1000), // set end position at 40s
                            new KeyValue(busGroup.translateXProperty(), bus.getReader().getNextStop(bus.getReader().getIndexPointer() + 1).getLongitude() * 3500 + 3),
                            new KeyValue(busGroup.translateYProperty(), -1 * bus.getReader().getNextStop(bus.getReader().getIndexPointer() + 1).getLatitude() * 3500 + 3)));
            startTimeline.play();
            simulate.setOnAction(e -> {
                passCalc.updatePassengerCount();
                updater.nextStop();
                Timeline timeline = new Timeline();
                timeline.getKeyFrames().addAll(
                        new KeyFrame(Duration.ZERO, // set start position at 0
                                new KeyValue(busGroup.translateXProperty(), bus.getLocation().getCurrentStop().getLongitude() * 3500 + 3),
                                new KeyValue(busGroup.translateYProperty(), -1 * bus.getLocation().getCurrentStop().getLatitude() * 3500 + 3)),
                        new KeyFrame(new Duration(busTimer.getUntilNextTime()* 1000), // set end position at 40s
                                new KeyValue(busGroup.translateXProperty(), bus.getReader().getNextStop(bus.getReader().getIndexPointer() + 1).getLongitude() * 3500 + 3),
                                new KeyValue(busGroup.translateYProperty(), -1 * bus.getReader().getNextStop(bus.getReader().getIndexPointer() + 1).getLatitude() * 3500 + 3)));
                timeline.play();
            });
        }
        vbox.getChildren().addAll(group, buttonBox);
        vbox.setAlignment(Pos.CENTER);
        vbox.setMargin(group, new Insets(50));
        Scene scene = new Scene(vbox);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static String modifyCountdown(Bus bus, double time) {
        return "Bus " + bus.getId() + "\nNext Stop: " + bus.getReader().getNextStop(bus.getReader().getIndexPointer()+1).getName() +  "\nin " + time + " minutes";
    }
}


