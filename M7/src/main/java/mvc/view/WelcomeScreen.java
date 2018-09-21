package mvc.view;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import javafx.scene.control.Button;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import mvc.controller.*;
import mvc.model.*;
import java.io.File;
import java.util.Scanner;

public class WelcomeScreen extends Application {

    protected static Stage primaryStage = new Stage();
    protected static Stage appStage = new Stage();
    protected static Account account;

    public void start(Stage stage) {
        Scene scene = welcomeDesign();
        primaryStage = stage;
        primaryStage.setScene(scene);
        primaryStage.setTitle("Marta Simulation System");
        primaryStage.setMaximized(true);
        primaryStage.show();
    }

    public static void loadData(File file) {
        try {
            Scanner scanner = new Scanner(file);
            DataReader.addStops(scanner);
            scanner.close();
            scanner = new Scanner(file);
            DataReader.addRoutes(scanner);
            scanner.close();
            scanner = new Scanner(file);
            DataReader.addBus(scanner);
            scanner.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    static Scene welcomeDesign() {
        BorderPane pane = new BorderPane();
        VBox vbox = new VBox();
        pane.setCenter(vbox);
        Scene scene = new Scene(pane);

        Text martaWelcome = new Text("MARTA Simulation System");
        martaWelcome.setStyle("-fx-font: 60 Calibri;");
        martaWelcome.setTextAlignment(TextAlignment.CENTER);
        Button loginButton = new Button("Log In");
        Button registerButton = new Button("Register");
        HBox mss = new HBox(martaWelcome);
        mss.setAlignment(Pos.CENTER);
        HBox loginOrRegister = new HBox(loginButton, registerButton);

        registerButton.setOnAction(e -> toRegister());
        loginButton.setOnAction(e2 -> toLogin());

        loginOrRegister.setAlignment(Pos.CENTER);
        vbox.getChildren().addAll(mss, loginOrRegister);
        Insets margin = new Insets(50);
        vbox.setMargin(mss, margin);
        vbox.setMargin(loginOrRegister, margin);
        vbox.setAlignment(Pos.CENTER);
        vbox.setSpacing(25);
        return scene;
    }

    static Scene appDesign(Stage stage) {
        BorderPane appPane = new BorderPane();
        VBox vbox = new VBox();
        Scene appScene = new Scene(appPane);
        Text loginSuccess = new Text("You've successfully logged in!");
        loginSuccess.setStyle("-fx-font: 30 Calibri;");
        appPane.setTop(loginSuccess);

        Button routes = new Button("Routes");
        routes.setStyle(
                "-fx-background-radius: 5em; " +
                        "-fx-min-width: 100px; " +
                        "-fx-min-height: 100px; " +
                        "-fx-max-width: 100px; " +
                        "-fx-max-height: 100px;"
        );
        routes.setOnAction(e5 -> RouteScreen.show());

        Button stops = new Button("Stops");
        stops.setStyle(
                "-fx-background-radius: 5em; " +
                        "-fx-min-width: 100px; " +
                        "-fx-min-height: 100px; " +
                        "-fx-max-width: 100px; " +
                        "-fx-max-height: 100px;"
        );
        stops.setOnAction(e6 -> StopScreen.show());

        Button buses = new Button("Buses");
        buses.setStyle(
                "-fx-background-radius: 5em; " +
                        "-fx-min-width: 100px; " +
                        "-fx-min-height: 100px; " +
                        "-fx-max-width: 100px; " +
                        "-fx-max-height: 100px;"
        );
        if (account.isManager()) {
            buses.setOnAction(e7 -> ManagerBusScreen.show());
        } else {
            buses.setOnAction(e7 -> BusScreen.show());
        }

        Button map = new Button("Map");
        map.setStyle(
                "-fx-background-radius: 5em; " +
                        "-fx-min-width: 100px; " +
                        "-fx-min-height: 100px; " +
                        "-fx-max-width: 100px; " +
                        "-fx-max-height: 100px;"
        );
        map.setOnAction(e -> {
            appStage.hide();
            CreateMap.show(appStage);
        });

        HBox buttons = new HBox(routes, stops, buses, map);
        buttons.setMargin(routes, new Insets(0, 20, 0, 20));
        buttons.setMargin(stops, new Insets(0, 20, 0, 20));
        buttons.setMargin(buses, new Insets(0, 20, 0, 20));
        buttons.setMargin(map, new Insets(0, 20, 0, 20));
        buttons.setAlignment(Pos.CENTER);
        vbox.getChildren().addAll(buttons);

        Button logoutButton = new Button("Log Out");
        logoutButton.setOnAction(e4 -> {
            stage.close();
            showWelcome();
        });
        HBox logout =  new HBox(logoutButton);
        logout.setAlignment(Pos.CENTER);
        Insets margin = new Insets(50);
        vbox.setMargin(logout, margin);
        vbox.setAlignment(Pos.CENTER);
        vbox.setSpacing(25);

        appPane.setCenter(vbox);
        appPane.setBottom(logout);
        appPane.setMargin(loginSuccess, new Insets(150, 0, 0, 0));
        appPane.setMargin(logout, new Insets(0, 0, 150, 0));
        appPane.setAlignment(loginSuccess, Pos.CENTER);

        return appScene;
    }

    static void toRegister() {
        VBox newUser = new VBox();
        Scene newScene = new Scene(newUser);
        Stage newStage = new Stage();
        newStage.setScene(newScene);
        newStage.setTitle("New User Registration");
        Text newUserRegistration = new Text("New User Registration");
        newUserRegistration.setStyle("-fx-font: 24 Calibri;");
        TextField username = new TextField("Create Username:");
        TextField password = new TextField("Create Password:");
        TextField passwordVerification = new TextField("Verify Password:");
        TextField phone = new TextField("Enter phone number:");
        Button create = new Button("Create");
        newUser.getChildren().addAll(newUserRegistration, username, password, passwordVerification,
                phone, create);
        Insets margin = new Insets(50);
        newUser.setMargin(newUserRegistration, margin);
        newUser.setAlignment(Pos.CENTER);
        newStage.show();

        create.setOnAction(e -> {
            RegisterObject.register(username, password, passwordVerification, phone, newStage);
        });
    }

    static void toLogin() {
        VBox loginUser = new VBox();
        Scene loginScene = new Scene(loginUser);
        Stage loginStage = new Stage();
        loginStage.setScene(loginScene);
        loginStage.setTitle("Login");
        Text loginText = new Text("Login");
        TextField user = new TextField("Username:");
        TextField pass = new TextField("Password:");
        Button loginButton2 = new Button("Login");
        loginUser.getChildren().addAll(loginText, user, pass, loginButton2);
        loginUser.setAlignment(Pos.CENTER);
        loginStage.show();

        loginButton2.setOnAction(e3 -> {
            String username = user.getCharacters().toString();
            String password = pass.getCharacters().toString();
            account = new Account(username, password, 1111111111, false,
                    true);
            Account[] accounts = new Account[1];
            accounts[0] = account;
            LoginObject testLogin = new LoginObject(accounts);

            if (testLogin.checkLogin(account)) {
                loginStage.close();
                showApplication();
            }

        });

    }

    static void showWelcome() {
        Scene scene = welcomeDesign();
        primaryStage.setScene(scene);
        primaryStage.setTitle("Marta Simulation System");
        primaryStage.setMaximized(true);
        primaryStage.show();
    }

    static void showApplication() {
        primaryStage.close();
        appStage.setScene(appDesign(appStage));
        appStage.setTitle("Application");
        appStage.setMaximized(true);
        appStage.show();
    }

}