package rfinder.Controller;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import rfinder.Hazeron.*;
import rfinder.StarMapHandler;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.File;
import java.lang.System;
import java.util.Objects;

public class RFinder extends Application {
    private static Stage mainStage;
    private static StarMap starMap;

    @FXML
    private AnchorPane topPane;

    @Override
    public void start(Stage mainStage) throws Exception {
        RFinder.mainStage = mainStage;
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getClassLoader().getResource("rfinder.fxml")));

        Scene rootScene = new Scene(root);

        mainStage.setTitle("RFinder");
        mainStage.setScene(rootScene);
        mainStage.setMinWidth(960);
        mainStage.setMinHeight(540);
        mainStage.show();
    }

    public void exit(ActionEvent actionEvent) {
        mainStage.close();
    }

    public void about(ActionEvent actionEvent) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("About");
        alert.setHeaderText("RFinder 0.1.0");
        alert.setContentText("RFinder Copyright © 2018 expert700, all right reserved.\n" +
                "Icons Copyright © 1999-2018 Software Engineering, Inc.");

        alert.show();
    }

    public void importStarmap(ActionEvent actionEvent) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open StarMap");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("StarMap (*.xml)", "*.xml"));
        fileChooser.setInitialDirectory(new File(java.lang.System.getProperty("user.home") + "/Shores of Hazeron/"));

        File selectedFile = fileChooser.showOpenDialog(mainStage);
        if (selectedFile != null) {
            try {
                SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
                SAXParser saxParser = saxParserFactory.newSAXParser();
                StarMapHandler starMapHandler = new StarMapHandler();
                saxParser.parse(selectedFile, starMapHandler);
                starMap = starMapHandler.getStarMap();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
