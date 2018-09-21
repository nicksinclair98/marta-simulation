package mvc.view;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.chart.*;
import javafx.scene.control.Button;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import java.io.File;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Scanner;
import mvc.controller.*;
import mvc.model.*;

public class StopScreen extends WelcomeScreen {

    private static Stage mainStage = new Stage();

    public static void show() {
        BorderPane mainScreen = setDesign();
        Scene scene = new Scene(mainScreen);
        mainStage.setScene(scene);
        mainStage.setMaximized(true);
        appStage.close();
        mainStage.show();
    }

    public static VBox displayData() {
        VBox stopFeed = new VBox();
        ArrayList<Stop> stops = getStops();
        ScatterChart<Number, Number> chart = createChart(stops);
        stopFeed.getChildren().addAll(chart);
        return stopFeed;
    }

    public static BorderPane setDesign() {
        BorderPane mainScreen = new BorderPane();
        Text routeText = new Text("Stops");
        routeText.setStyle("-fx-font: 60 Calibri;");
        mainScreen.setTop(routeText);
        mainScreen.setMargin(routeText, new Insets(150, 0, 0, 0));
        mainScreen.setAlignment(routeText, Pos.CENTER);
        VBox stopFeed = displayData();
        stopFeed.setAlignment(Pos.CENTER);
        mainScreen.setCenter(stopFeed);
        mainScreen.setMargin(stopFeed, new Insets(0, 0, 70, 0));
        mainScreen.setAlignment(stopFeed, Pos.CENTER);
        Button back = new Button("Go Back");
        HBox backButton = new HBox(back);
        backButton.setAlignment(Pos.CENTER);
        back.setOnAction(e -> {
            mainStage.close();
            appStage.show();
        });
        mainScreen.setBottom(backButton);
        mainScreen.setMargin(backButton, new Insets(0, 0, 100, 0));
        return mainScreen;
    }

    public static ArrayList<Stop> getStops() {
        return DataReader.getStopArray();
    }

    public static ScatterChart<Number, Number> createChart(ArrayList<Stop> stops) {
        ObservableList<XYChart.Data<Number,Number>> dataPoints = FXCollections.observableArrayList();
        for (Stop stop : stops) {
            double x = stop.getLongitude();
            double y = stop.getLatitude();
            String name = stop.getName();
            XYChart.Data<Number, Number> stopPlot = new XYChart.Data<>(x, y);
            Button stopButton = new Button(name);
            Label stopLabel = new Label(stop.toString());
            stopLabel.setVisible(false);
            stopPlot.setNode(stopButton);
            stopButton.setOnMouseEntered(e -> dialogueBoxAndWindowControl(stop, stopButton));
            dataPoints.add(stopPlot);
        }
        XYChart.Series<Number, Number> dataSeries = new XYChart.Series<>();
        dataSeries.setData(dataPoints);
        ObservableList<XYChart.Series<Number,Number>> finalPoints = FXCollections.observableArrayList();
        finalPoints.add(dataSeries);
        NumberAxis xAxis = new NumberAxis("Longitude", -0.04, 0.2, 0.02);
        NumberAxis yAxis = new NumberAxis("Latitude", -0.04, 0.2, 0.02);
        ScatterChart<Number, Number> chart = new ScatterChart<>(xAxis, yAxis);
        chart.setData(finalPoints);
        return chart;
    }

    public static void dialogueBoxAndWindowControl(Stop stop, Button stopButton) {
        Dialog message = new Dialog();
        Stage window = (Stage) message.getDialogPane().getScene().getWindow();
        message.setContentText(stop.toString());
        message.setX(stopButton.localToScreen(stopButton.getBoundsInLocal()).getMinX() - 20);
        message.setY(stopButton.localToScreen(stopButton.getBoundsInLocal()).getMinY() - 40);
        Button hide = new Button();
        hide.setStyle("-fx-background-color: transparent;" + "-fx-border-color: transparent;");
        hide.setMinWidth(90);
        hide.setMinHeight(40);
        message.setGraphic(hide);
        hide.setOnMouseExited(e1 -> window.close());
        message.show();
    }
}