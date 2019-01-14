package rfinder.Controller;

import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import rfinder.Hazeron.*;
import rfinder.RFinder;
import rfinder.Util.StarMapHandler;
import rfinder.Util.StarMapReceiver;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.File;
import java.lang.System;
import java.util.ArrayList;
import java.util.Objects;

public class MainController {

    @FXML
    private MenuItem importItem, clearItem, exitItem, aboutItem;

    @FXML
    private Tab resourceTab, statisticTab, systemTab;

    private StarMap starMap;
    private static MainController instance;
    private ArrayList<Object> controllers;

    @SuppressWarnings("Duplicates")
    @FXML
    void initialize() {
        instance = this;

        controllers = new ArrayList<>();

        importItem.setOnAction(this::importStarmap);
        clearItem.setOnAction(this::clearStarmap);
        exitItem.setOnAction(this::exit);
        aboutItem.setOnAction(this::about);

        try {
            Parent resourceTabContents = FXMLLoader.load(Objects.requireNonNull(getClass().getClassLoader().getResource("resources.fxml")));
            resourceTab.setContent(resourceTabContents);
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Something is very wrong.");
            alert.setContentText("RFinder could not load some or all of the \nfxml layout files, and can not continue.");
            alert.showAndWait();

            e.printStackTrace();
            System.exit(1);
        }
    }

    @FXML
    private void exit(ActionEvent actionEvent) {
        Platform.exit();
        java.lang.System.exit(0);
    }

    @FXML
    private void about(ActionEvent actionEvent) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("About");
        alert.setHeaderText("RFinder 1.0.0");
        alert.setContentText("Developed by expert700.");

        alert.show();
    }

    @FXML
    private void importStarmap(ActionEvent actionEvent) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open StarMap");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("StarMap (*.xml)", "*.xml"));
        fileChooser.setInitialDirectory(new File(java.lang.System.getProperty("user.home") + "/Shores of Hazeron/"));

        File selectedFile = fileChooser.showOpenDialog(RFinder.mainStage);
        if (selectedFile != null) {
            clearStarmap(actionEvent);
            try {

                Task<StarMap> mainTask = new Task<StarMap>() {
                    @Override
                    protected StarMap call() throws Exception {
                        SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
                        SAXParser saxParser = saxParserFactory.newSAXParser();

                        StarMapHandler starMapHandler = new StarMapHandler();
                        saxParser.parse(selectedFile, starMapHandler);

                        return starMapHandler.getStarMap();
                    }
                };

                mainTask.setOnSucceeded(event -> {
                    starMap = mainTask.getValue();
                });

                RFinder.threadPool.submit(mainTask);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    private void clearStarmap(ActionEvent actionEvent) {
        starMap = null;
    }

    static MainController getInstance() {
        return instance;
    }

    public <T extends StarMapReceiver> void registerController(T controller) {
        controllers.add(controller);
    }
}
