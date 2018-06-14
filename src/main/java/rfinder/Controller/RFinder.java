package rfinder.Controller;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Dialog;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.util.Objects;

public class RFinder extends Application {
    private static Stage primaryStage;

    @Override
    public void start(Stage primaryStage) throws Exception{
        RFinder.primaryStage = primaryStage;
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getClassLoader().getResource("rfinder.fxml")));

        primaryStage.setTitle("Hello World");
        primaryStage.setScene(new Scene(root, 960, 540));
        primaryStage.show();
    }

    public void exit(ActionEvent actionEvent) {
        primaryStage.close();
    }

    public void about(ActionEvent actionEvent) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("About");
        alert.setHeaderText("Resource Finder 0.1.0");
        alert.setContentText("© 2018 expert700, all right reserved.");
    }
}
