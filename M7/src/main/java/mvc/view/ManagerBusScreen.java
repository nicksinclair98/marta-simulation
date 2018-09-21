package mvc.view;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.CollectionReference;
import com.google.cloud.firestore.Firestore;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.scene.text.TextBoundsType;
import javafx.stage.Stage;
import mvc.controller.BusTimer;
import mvc.controller.DataBaseControl.StateRetriever;
import mvc.controller.DataReader;
import mvc.controller.LocationReader;
import mvc.controller.StopUpdater;
import mvc.model.Bus;
import mvc.controller.DataBaseControl.GenerateSaveState;
import javafx.stage.FileChooser;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import java.io.File;
import java.util.*;

public class ManagerBusScreen extends WelcomeScreen {

    private static Stage mainStage = new Stage();
    private static ArrayList<Bus> buses;
    private static String boundCountdown;
    private static ArrayList<BusTimer> busTimers;

    /**
     * this method initializes the method calls that will design the BusScreen and instantiates the buttons that will
     * decide whether the database is used for startup.
     */

    public static void show() {
        Button fromDataBase = new Button("From DataBase");
        Button csvSelector = new Button("From CSV");
        final FileChooser fileChooser = new FileChooser();
        Stage displayModeStage = new Stage();
        createOptionsMenu(csvSelector, fromDataBase, displayModeStage);

        csvSelector.setOnAction(
                new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(final ActionEvent event) {
                        File file = fileChooser.showOpenDialog(displayModeStage);
                        if (file != null) {
                            displayModeStage.close();
                            BorderPane mainScreen = setDesign(false, file);
                            Scene scene = new Scene(mainScreen);
                            mainStage.setScene(scene);
                            mainStage.setMaximized(true);
                            appStage.hide();
                            mainStage.show();
                        }
                    }

                }
        );

        fromDataBase.setOnAction(database -> {
            displayModeStage.close();
            BorderPane mainScreen = setDesign(true, null);
            Scene scene = new Scene(mainScreen);
            mainStage.setScene(scene);
            mainStage.setMaximized(true);
            appStage.hide();
            mainStage.show();
        });
    }

    /**
     * displayData generates a fully functional and informational VBox that can easily be added to the BorderPane in
     * setDesign(). This method loops through a BusArray and accesses information through various controller classes
     * to build a displayable VBox with all of the Bus' information. This method also creates a timer that counts
     * down until the next arrival inside the VBox, which is displayed on the BorderPane.
     *
     * @param dataBase the boolean value that dictates whether the user opted to load information from the database
     * @return the VBox describing the Bus' data
     */

    public static VBox displayData(boolean dataBase, File file) {
        VBox busFeed = new VBox();
        HBox busBox = new HBox();
        busTimers = new ArrayList<>();

        //the following are two different paths to get the BusArray depending on if the user utilizes the database.

        if (!(dataBase)) {
            loadData(file);
            buses = getBusArray();
        } else {
            buses = StateRetriever.retrieveData();
        }

        //this loop gathers information from the buses, initiates BusTimer Classes for the buses, and starts the
        //countdown timer for the user display.

        for (Bus bus : buses) {
            String path = System.getProperty("user.dir");
            Image busImg = new Image("file:///" + path +
                    "/src/main/java/assets/martaTrain.png");
            ImageView busView = new ImageView(busImg);

            if (!(dataBase)) {
                LocationReader reader = new LocationReader(bus, bus.getRoute(), bus.getRoute().getStops().get(0));
                bus.setReader(reader);
            }

            VBox titleBox = new VBox();

            Text id = new Text("Bus Id: " + bus.getId());
            HBox idBox = new HBox(id);

            SimpleStringProperty displayString = new SimpleStringProperty();
            String fullInfo = bus.getReader().fullInfo();
            displayString.set(fullInfo);
            Text info = new Text();
            info.textProperty().bind(displayString);

            Button simulate = new Button("Next Stop");
            StopUpdater updater = new StopUpdater(bus.getReader());
            simulate.setOnAction(e -> {
                updater.nextStop();
                displayString.set(bus.getReader().fullInfo());
            });

            BusTimer busTimer = new BusTimer(bus.getReader(), simulate);
            busTimers.add(busTimer);
            bus.setTimer(busTimer);
            busTimer.start();

            //the next block of code dictates the display and initialization of the timer display. This timer counts
            //down at a specified rate and is displayed by a SimpleStringProperty (bound to a String) inside of a
            //Circle object.

            SimpleStringProperty countdown = new SimpleStringProperty();
            double count = busTimer.getUntilNextTime();
            boundCountdown = "Next Bus in\n" + count + "\n minutes";
            countdown.set(boundCountdown);
            HBox displayCountdown = new HBox();
            Text displayTextObject = new Text();
            displayTextObject.textProperty().bind(countdown);
            VBox boxStack = new VBox();
            StackPane stack = new StackPane();
            Circle circle = new Circle();
            displayCountdown.getChildren().addAll(displayTextObject);
            Timer displayUpdater = new Timer();
            displayUpdater.scheduleAtFixedRate(new TimerTask() {
                @Override
                public void run() {
                    double timeCount = busTimer.getUntilNextTime();
                    modifyCountdown(timeCount);
                    countdown.set(boundCountdown);
                }
            }, 0, 500l);

            //this block puts all of the parts of the busFeed together and calls the method to finish the design.

            stack.getChildren().addAll(circle, displayTextObject);
            boxStack.getChildren().addAll(stack);
            VBox busStack = new VBox(busView, info, boxStack, simulate);
            titleBox.getChildren().addAll(idBox, busStack);
            busBox.getChildren().addAll(titleBox);
            designBusDisplayBox(circle, displayTextObject, busStack, busBox,
                    titleBox, busView, info, boxStack, simulate,
                    idBox, id);
        }

        Text busText = new Text("Active Buses");
        busText.setTextAlignment(TextAlignment.CENTER);
        busText.setStyle("-fx-font: 40 Calibri;");
        busFeed.getChildren().addAll(busText, busBox);
        busFeed.setMargin(busBox, new Insets(0, 0, 10, 0));

        return busFeed;
    }

    /**
     * method responsible for primary creation of the mainScreen BorderPane. It takes the complete VBox of bus
     * information created by displayData and formats it into a BorderPane, where a ToolBar with manager options
     * is at the top, the busFeed is central, and the "Go Back" button resides at the bottom.
     *
     * @param dataBase the boolean value that dictates whether the user opted to load information from the database
     * @return the BorderPane that will be displayed on the stage.
     */
    public static BorderPane setDesign(boolean dataBase, File intakeFile) {
        BorderPane mainScreen = new BorderPane();
        VBox busFeed = displayData(dataBase, intakeFile);
        busFeed.setAlignment(Pos.CENTER);
        mainScreen.setCenter(busFeed);
        mainScreen.setAlignment(busFeed, Pos.CENTER);
        Button back = new Button("Go Back");
        HBox backButton = new HBox(back);
        backButton.setAlignment(Pos.CENTER);
        ToolBar toolBar = new ToolBar();
        Pane emptyPane = new Pane();
        HBox spring = new HBox(emptyPane);
        spring.setHgrow(emptyPane, Priority.ALWAYS);
        MenuButton file = new MenuButton("Manage");
        MenuItem save = new MenuItem("Save Simulation");
        MenuItem StopSim = new MenuItem("Stop Simulation");
        MenuItem StartSim = new MenuItem("Start Simulation");
        MenuItem deleteButton = new MenuItem("Clear Database");
        deleteButton.setOnAction(e -> {
            Firestore db = GenerateSaveState.getDb();
            CollectionReference col = db.collection("SaveStates");
            GenerateSaveState.deleteCollection(col, 1);
        });
        file.getItems().addAll(save, StopSim, StartSim, deleteButton);
        save.setOnAction(saveEvent -> {
            GenerateSaveState.generate(buses);
            mainStage.close();
            appStage.show();
        });

        StartSim.setOnAction(e -> {
            for (BusTimer bustimer : busTimers) {
                bustimer.start();
            }
        });

        StopSim.setOnAction(e -> {
            for (BusTimer bustimer : busTimers) {
                bustimer.pause();
            }
        });

        file.setOnMouseClicked(fileClick -> {
            file.show();
        });
        toolBar.getItems().addAll(spring, file);
        mainScreen.setTop(toolBar);

        back.setOnAction(e -> {
            mainStage.close();
            appStage.show();
        });
        VBox buttons = new VBox(5);
        buttons.getChildren().addAll(backButton);
        buttons.setAlignment(Pos.CENTER);
        mainScreen.setBottom(buttons);
        mainScreen.setMargin(buttons, new Insets(0, 0, 25, 0));
        mainScreen.setMargin(busFeed, new Insets(25, 0, 0, 0));
        mainScreen.setMargin(toolBar, new Insets(0, 0, 0, 0));
        return mainScreen;
    }

    public static ArrayList<Bus> getBusArray() {
        return DataReader.getBusArray();
    }

    /**
     * @param time refreshes the countdown display on the BusScreen
     */
    public static void modifyCountdown(double time) {
        boundCountdown = "Next Bus in\n" + time + "\n minutes";
    }

    /**
     * Almost all design aspects of the code have been consolidated into the following method for ease of change and/
     * or tracing. The objects in the method are separated chronologically as they appear on screen
     *
     * @param circle            the circle contains the displayTextObject, an object that displays the time until arrival
     * @param displayTextObject an object that displays the time until arrival
     * @param busStack          the busStack is the vbox consisting of other displays, with a blue box around it
     * @param busBox            the busBox is the HBox containing a busStack for each bus
     * @param titleBox          Titlebox is the box that combines the title "Active Buses" and the simulate (Next Stop)
     *                          button to the busBox
     * @param busView           busView is the image of the bus portrayed
     * @param info              info is the body of the box, with information regarding the bus
     * @param boxStack          an HBox that encloses a StackPane, which holds the circle and displayTextObject
     * @param simulate          the Next Stop button
     * @param idBox             idBox is the vbox containing the bus id
     * @param id                the Text of the Bus id
     */
    public static void designBusDisplayBox(Circle circle, Text displayTextObject, VBox busStack, HBox busBox,
                                           VBox titleBox, ImageView busView, Text info, VBox boxStack, Button simulate,
                                           HBox idBox, Text id) {

        //descriptionBox is the vbox containing the bus id, it contains description, a Text

        idBox.setAlignment(Pos.CENTER);
        id.setFont(new Font(20));
        id.setTextAlignment(TextAlignment.CENTER);
        id.setTextAlignment(TextAlignment.CENTER);

        //busView is the image of the bus portrayed

        busView.setPreserveRatio(true);
        busView.setFitWidth(200);

        //info is the body of the box, with information regarding the bus

        info.setTextAlignment(TextAlignment.CENTER);

        //the circle contains the displayTextObject, an object that displays the time until arrival

        circle.setRadius(65);
        circle.setFill(Color.TRANSPARENT);
        circle.setStrokeWidth(2);
        circle.setStroke(Paint.valueOf("#039ED3"));
        displayTextObject.setFont(new Font(15));
        displayTextObject.setBoundsType(TextBoundsType.VISUAL);
        displayTextObject.setTextAlignment(TextAlignment.CENTER);

        //the busStack encompasses all the above, with a blue box around it

        busStack.setStyle("-fx-border-color: #039ED3;" +
                "-fx-border-insets: -30;" +
                "-fx-border-width: 1;"
        );
        busStack.setMargin(busView, new Insets(-5, 0, 10, 0));
        busStack.setMargin(info, new Insets(0, 0, 0, 0));
        busStack.setMargin(boxStack, new Insets(0, 0, 0, 0));
        busStack.setMargin(simulate, new Insets(15, 0, -5, 0));
        busStack.setAlignment(Pos.CENTER);

        //the busBox is the HBox containing a busStack for each bus

        busBox.setAlignment(Pos.CENTER);
        busBox.setMargin(titleBox, new Insets(0, 0, 0, 0));

        //even spacing between each busBox
        GridPane busBoxes = new GridPane();
        int count = 0;
        busBoxes.add(busBox, count, 0);
        count++;

//
//        ObservableList<Node> modifyableNodes = busBox.getChildren();
//        int counter = 1;

        //This code loops through the buses and puts busStacks closer together, closing the gap that was between them.

//        for (Node node : modifyableNodes) {
//            if (counter % 2 != 0) {
//                busBox.setMargin(node, new Insets(0, 100, 0, 0));
//            } else {
//                busBox.setMargin(node, new Insets(0, 0, 0, 100));
//            }
//            counter++;
//        }

        //titleBox is the box that combines the title "Active Buses" and the simulate (Next Stop) button to the busBox

        titleBox.setMargin(idBox, new Insets(0, 0, 40, 0));
        titleBox.setMargin(busStack, new Insets(0, 0, 0, 0));

    }

    /**
     * this method presents a simple dialogue box that allows the user to decide which option they want to access data
     * from: the beginning(the csv file) or through the database
     *
     * @param fromBeginning    the button to access via csv
     * @param fromDataBase     the button to access via database
     * @param displayModeStage the stage that the dialogue is presented on.
     */

    public static void createOptionsMenu(Button fromBeginning, Button fromDataBase, Stage displayModeStage) {
        VBox displayModeVbox = new VBox();
        Scene displayModeScene = new Scene(displayModeVbox);
        displayModeStage.setScene(displayModeScene);
        displayModeStage.setTitle("Choose Display Mode");
        HBox displayButtons = new HBox();
        displayButtons.getChildren().addAll(fromBeginning, fromDataBase);
        displayModeVbox.getChildren().addAll(displayButtons);
        displayModeVbox.setAlignment(Pos.CENTER);
        displayModeStage.show();
    }

}
