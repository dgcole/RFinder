package rfinder.Controller;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.w3c.dom.Document;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.io.InputStream;
import java.util.Objects;

public class RFinder extends Application {
    private static Stage mainStage;

    @Override
    public void start(Stage primaryStage) throws Exception {
        RFinder.mainStage = primaryStage;
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getClassLoader().getResource("rfinder.fxml")));

        primaryStage.setTitle("RFinder");
        primaryStage.setScene(new Scene(root, 960, 540));
        primaryStage.show();
    }

    public void exit(ActionEvent actionEvent) {
        mainStage.close();
    }

    public void about(ActionEvent actionEvent) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("About");
        alert.setHeaderText("RFinder 0.1.0");
        alert.setContentText("Copyright © 2018 expert700, all right reserved.");

        alert.show();
    }

    public void importStarmap(ActionEvent actionEvent) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Starmap");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Starmap (*.xml)", "*.xml"));
        fileChooser.setInitialDirectory(new File(System.getProperty("user.home") + "/Shores of Hazeron/"));

        File selectedFile = fileChooser.showOpenDialog(mainStage);
        if (selectedFile != null) {
            try {
                DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
                DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
                long before = System.currentTimeMillis();
                Document document = documentBuilder.parse(selectedFile);
                long after = System.currentTimeMillis();
                System.out.println(after - before);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
