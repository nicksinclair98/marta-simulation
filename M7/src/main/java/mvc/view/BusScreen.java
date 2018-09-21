package mvc.view;

import javafx.beans.property.SimpleStringProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;

import mvc.controller.*;
import mvc.model.*;

public class BusScreen extends WelcomeScreen {

    private static Stage mainStage = new Stage();
    private static ArrayList<BusTimer> busTimers;

    public static void show() {
        BorderPane mainScreen = setDesign();
        Scene scene = new Scene(mainScreen);
        mainStage.setScene(scene);
        mainStage.setMaximized(true);
        appStage.close();
        mainStage.show();
    }

    public static VBox displayData() {
        VBox busFeed = new VBox();
        ArrayList<Bus> buses = getBusArray();
        HBox busBox = new HBox();
        busBox.setAlignment(Pos.CENTER);

        for (Bus bus : buses) {
            String path = System.getProperty("user.dir");
            Image busImg = new Image("file:///" + path +
                    "/src/main/java/assets/martaTrain.png");
            ImageView busView = new ImageView(busImg);
            busView.setPreserveRatio(true);
            busView.setFitWidth(400);
            Text description = new Text(bus.toString());
            LocationReader reader = new LocationReader(bus, bus.getRoute(), bus.getRoute().getStops().get(0));



            SimpleStringProperty displayString = new SimpleStringProperty();
            String fullInfo = reader.fullInfo();
            Text info = new Text();
            displayString.set(fullInfo);
            info.setTextAlignment(TextAlignment.CENTER);
            description.setTextAlignment(TextAlignment.CENTER);
            info.textProperty().bind(displayString);
            VBox busStack = new VBox(busView, description, info);
            busStack.setAlignment(Pos.CENTER);
            Button simulate = new Button("Next Stop");
            StopUpdater updater = new StopUpdater(reader);
            simulate.setOnAction(e -> {
                updater.nextStop();
                displayString.set(reader.fullInfo());
            });
            BusTimer busTimer = new BusTimer(reader, simulate);
            busTimers.add(busTimer);
            bus.setTimer(busTimer);
            busTimer.start();

            busBox.getChildren().addAll(busStack, simulate);

        }
        busFeed.getChildren().addAll(busBox);
        return busFeed;
    }

    public static BorderPane setDesign() {
        BorderPane mainScreen = new BorderPane();
        Text busText = new Text("Buses");
        busText.setStyle("-fx-font: 60 Calibri;");
        mainScreen.setTop(busText);
        mainScreen.setMargin(busText, new Insets(25, 0, 0, 0));
        mainScreen.setAlignment(busText, Pos.CENTER);
        VBox busFeed = displayData();
        busFeed.setAlignment(Pos.CENTER);
        mainScreen.setCenter(busFeed);
        mainScreen.setMargin(busFeed, new Insets(0, 0, 150, 0));
        mainScreen.setAlignment(busFeed, Pos.CENTER);
        Button back = new Button("Go Back");
        HBox backButton = new HBox(back);
        backButton.setAlignment(Pos.CENTER);
        back.setOnAction(e -> {
            mainStage.close();
            appStage.show();
        });
        mainScreen.setBottom(backButton);
        mainScreen.setMargin(backButton, new Insets(-100, 0, 200, 0));
        return mainScreen;
    }

    public static ArrayList<Bus> getBusArray() {
        return DataReader.getBusArray();
    }

}