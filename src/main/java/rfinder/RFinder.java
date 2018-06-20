package rfinder;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import rfinder.Hazeron.*;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.File;
import java.util.Objects;

public class RFinder extends Application {
    public static Stage mainStage;

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

    public static void main(String[] args) {
        Application.launch(RFinder.class, args);
    }
}
