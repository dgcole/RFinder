package rfinder.Controller;

import javafx.collections.ListChangeListener;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.input.DragEvent;
import javafx.stage.FileChooser;
import rfinder.RFinder;
import rfinder.StarMapHandler;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.File;

public class Main {

    @FXML
    TableColumn col1, col2, col3, col4, col5, col6, col7, col8, col9, col10;

    @FXML
    TableView resourceTable;

    @FXML
    void initialize() {
        col1.prefWidthProperty().bind(resourceTable.widthProperty().multiply(.15));
        col2.prefWidthProperty().bind(resourceTable.widthProperty().multiply(.15));
        col3.prefWidthProperty().bind(resourceTable.widthProperty().multiply(.15));
        col4.prefWidthProperty().bind(resourceTable.widthProperty().multiply(.15));

        col5.prefWidthProperty().bind(resourceTable.widthProperty().multiply(.08));
        col6.prefWidthProperty().bind(resourceTable.widthProperty().multiply(.08));
        col7.prefWidthProperty().bind(resourceTable.widthProperty().multiply(.08));
        col8.prefWidthProperty().bind(resourceTable.widthProperty().multiply(.08));
        col9.prefWidthProperty().bind(resourceTable.widthProperty().multiply(.08));
        col10.prefWidthProperty().bind(resourceTable.widthProperty().multiply(.08));
    }

    public void exit(ActionEvent actionEvent) {
        RFinder.mainStage.close();
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

        File selectedFile = fileChooser.showOpenDialog(RFinder.mainStage);
        if (selectedFile != null) {
            try {
                SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
                SAXParser saxParser = saxParserFactory.newSAXParser();
                StarMapHandler starMapHandler = new StarMapHandler();
                saxParser.parse(selectedFile, starMapHandler);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static <S, T> void setColumnOrder(TableView table, TableColumn<S, T> ...columns) {
        table.getColumns().addListener(new ListChangeListener() {
            public boolean suspended;
            @Override
            public void onChanged(Change change) {
                change.next();
                if (change.wasReplaced() && !suspended) {
                    this.suspended = true;
                    table.getColumns().setAll(columns);
                    this.suspended = false;
                }
            }
        });
    }

    public void fixColumnOrder(DragEvent dragEvent) {
        setColumnOrder(resourceTable, col1, col2, col3, col4, col5, col6, col7, col8, col9, col10);
        System.out.println(":ts");
    }
}
