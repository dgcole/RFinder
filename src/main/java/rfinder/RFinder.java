package rfinder;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.DepthTest;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class RFinder extends Application {
    public static Stage mainStage;
    public static ExecutorService threadPool = Executors.newFixedThreadPool(2);

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
        mainStage.setOnCloseRequest(event -> {
            Platform.exit();
            System.exit(0);
        });
        mainStage.show();


    }

    public static void main(String[] args) {
        Application.launch(RFinder.class, args);
    }
}
