package rfinder.Controller;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import rfinder.Hazeron.*;
import rfinder.RFinder;
import rfinder.StarMapHandler;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.File;
import java.lang.System;
import java.util.ArrayList;

public class Main {

    @FXML
    private TableColumn<Object, Object> col1, col2, col3, col4, col5, col6, col7, col8, col9, col10, col11;

    @FXML
    private TableView resourceTable;

    @FXML
    private ChoiceBox<String> resourceTypePicker;

    private StarMap starMap;
    private boolean resize = false;

    @FXML
    void initialize() {
        col1.setCellValueFactory(new PropertyValueFactory<>("resource"));
        col2.setCellValueFactory(new PropertyValueFactory<>("galaxy"));
        col3.setCellValueFactory(new PropertyValueFactory<>("sector"));
        col4.setCellValueFactory(new PropertyValueFactory<>("system"));
        col5.setCellValueFactory(new PropertyValueFactory<>("body"));

        col6.setCellValueFactory(new PropertyValueFactory<>("q1"));
        col7.setCellValueFactory(new PropertyValueFactory<>("q2"));
        col8.setCellValueFactory(new PropertyValueFactory<>("q3"));
        col9.setCellValueFactory(new PropertyValueFactory<>("a1"));
        col10.setCellValueFactory(new PropertyValueFactory<>("a2"));
        col11.setCellValueFactory(new PropertyValueFactory<>("a3"));

        ArrayList<String> resourceTypeNames = ResourceType.getAllNames();
        resourceTypePicker.setItems(FXCollections.observableArrayList(resourceTypeNames));
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

                starMap = starMapHandler.getStarMap();
                refreshTable();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void refreshTable() {
        if (starMap == null) return;
        resourceTable.getItems().clear();

        String type = resourceTypePicker.getValue();
        if (type == null) {
            for (Resource r : starMap.getResources()) {
                resourceTable.getItems().add(r);
            }
        } else {
            for (Resource r : starMap.getResources()) {
                if (r.getResource().equals(type)) resourceTable.getItems().add(r);
            }
        }

        if (resize) {
            resourceTable.getColumns().stream().forEach((col) -> {
                TableColumn column = (TableColumn) col;
                Text t = new Text(column.getText());
                double max = t.getLayoutBounds().getWidth();
                for (int i = 0; i < resourceTable.getItems().size(); i++) {
                    if (column.getCellData(i) != null) {
                        t = new Text(column.getCellData(i).toString());
                        double calcwidth = t.getLayoutBounds().getWidth();
                        if (calcwidth > max) {
                            max = calcwidth;
                        }
                    }
                }
                column.setPrefWidth(max + 10.0d);
            });
        }
    }

    public void clearStarmap() {
        resourceTable.getItems().clear();
    }

    public void setResize(ActionEvent actionEvent) {
        resize = ((CheckBox) actionEvent.getSource()).isSelected();
    }

    public void clearTable(ActionEvent actionEvent) {
        resourceTable.getItems().clear();
    }
}
