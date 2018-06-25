package rfinder;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.util.Objects;

public class RFinder extends Application {
    public static Stage mainStage;

    @Override
    public void start(Stage mainStage) throws Exception {
        RFinder.mainStage = mainStage;
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getClassLoader().getResource("rfinder.fxml")));


        Scene rootScene = new Scene(root);

        mainStage.setTitle("RFinder");
        mainStage.getIcons().add(new Image(Objects.requireNonNull(getClass().getClassLoader().getResource("icon.png")).toString()));
        mainStage.setScene(rootScene);
        mainStage.setMinWidth(1024);
        mainStage.setMinHeight(768);
        mainStage.show();


    }

    public static void main(String[] args) {
        Application.launch(RFinder.class, args);
    }
}
