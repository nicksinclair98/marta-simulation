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
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import javafx.scene.layout.HBox;
import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;
import mvc.controller.*;
import mvc.model.*;

public class RouteScreen extends WelcomeScreen {

    private static Stage mainStage = new Stage();

    public static void show() {
        BorderPane mainScreen = setDesign();
        Scene scene = new Scene(mainScreen);
        mainStage.setScene(scene);
        mainStage.setMaximized(true);
        appStage.close();
        mainStage.show();
    }

    public static BorderPane setDesign() {
        BorderPane mainScreen = new BorderPane();
        Text routeText = new Text("Routes");
        routeText.setStyle("-fx-font: 60 Calibri;");
        mainScreen.setTop(routeText);
        mainScreen.setMargin(routeText, new Insets(150, 0, 0, 0));
        mainScreen.setAlignment(routeText, Pos.CENTER);
        VBox routeFeed = displayData();
        routeFeed.setAlignment(Pos.CENTER);
        mainScreen.setCenter(routeFeed);
        mainScreen.setMargin(routeFeed, new Insets(0, 0, 70, 0));
        mainScreen.setAlignment(routeFeed, Pos.CENTER);
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

    public static VBox displayData() {
        VBox routeFeed = new VBox();
        try {
            ArrayList<Route> routes = DataReader.getRouteArray();
            String text = "";
            for (Route route : routes) {
                text = text + route.toString() + "\n";
            }
            Text routeText = new Text(text);
            routeText.setTextAlignment(TextAlignment.CENTER);
            routeFeed.getChildren().addAll(routeText);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return routeFeed;
    }
}
