package rfinder.Controller;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.util.Callback;
import rfinder.Hazeron.*;
import rfinder.RFinder;
import rfinder.Util.MixedComparator;
import rfinder.Util.StarMapHandler;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.File;
import java.util.ArrayList;

public class Main {

    @FXML
    private TableColumn<Object, Object> col1, col2, col3, col4, col5, col6, col7, col8, col9, col10, col11, col12;

    @FXML
    private TableView resourceTable;

    @FXML
    private ChoiceBox<String> resourceTypePicker;

    @FXML
    private TextField minimumQuality;

    private StarMap starMap;
    private boolean resize = false;

    @FXML
    void initialize() {
        col1.setCellValueFactory(new PropertyValueFactory<>("resource"));
        col2.setCellValueFactory(new PropertyValueFactory<>("galaxy"));
        col3.setCellValueFactory(new PropertyValueFactory<>("sector"));
        col4.setCellValueFactory(new PropertyValueFactory<>("system"));
        col5.setCellValueFactory(new PropertyValueFactory<>("body"));
        col6.setCellValueFactory(new PropertyValueFactory<>("diameter"));

        var blanker = new Callback<TableColumn<Object, Object>, TableCell<Object, Object>>() {
            @Override
            public TableCell<Object, Object> call(TableColumn<Object, Object> param) {
                return new TableCell<>() {
                    @Override
                    protected void updateItem(Object item, boolean empty) {
                        if (item != null && !empty) {
                            super.setText(((Integer) item) == 0 ? "" : item.toString());
                        } else {
                            super.setText("");
                        }
                    }
                };
            }
        };
        col7.setCellValueFactory(new PropertyValueFactory<>("q1"));
        col8.setCellValueFactory(new PropertyValueFactory<>("q2"));
        col8.setCellFactory(blanker);
        col9.setCellValueFactory(new PropertyValueFactory<>("q3"));
        col9.setCellFactory(blanker);
        col10.setCellValueFactory(new PropertyValueFactory<>("a1"));
        col11.setCellValueFactory(new PropertyValueFactory<>("a2"));
        col12.setCellValueFactory(new PropertyValueFactory<>("a3"));

        ArrayList<String> resourceTypeNames = ResourceType.getAllNames();
        resourceTypePicker.setItems(FXCollections.observableArrayList(resourceTypeNames));

        minimumQuality.textProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal.equals("")) return;
            if (!newVal.matches("\\d*")) {
                minimumQuality.setText(newVal.replaceAll("[^\\d]", ""));
            } else if (Integer.parseInt(newVal) > 255) {
                minimumQuality.setText("255");
            } else if (Integer.parseInt(newVal) < 1) {
                minimumQuality.setText("1");
            }
        });
    }

    @FXML
    public void exit(ActionEvent actionEvent) {
        RFinder.mainStage.close();
    }

    @FXML
    public void about(ActionEvent actionEvent) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("About");
        alert.setHeaderText("RFinder 0.1.0");
        alert.setContentText("RFinder Copyright © 2018 expert700, all right reserved.");

        alert.show();
    }

    @FXML
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

    @FXML
    public void refreshTable() {
        if (starMap == null) return;
        resourceTable.getItems().clear();

        String type = resourceTypePicker.getValue();
        int minQual = Integer.parseInt(minimumQuality.textProperty().get());
        if (type == null) {
            for (Resource r : starMap.getResources()) {
                resourceTable.getItems().add(r);
            }
        } else {
            for (Resource r : starMap.getResources()) {
                boolean resourceMatch = r.getResource().equals(type);
                boolean qualityMatch = (r.getQ1() >= minQual ||
                r.getQ2() >= minQual || r.getQ3() >= minQual);

                if (resourceMatch && qualityMatch) {
                    resourceTable.getItems().add(r);
                }
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

    @FXML
    public void clearStarmap() {
        resourceTable.getItems().clear();
    }

    @FXML
    public void setResize(ActionEvent actionEvent) {
        resize = ((CheckBox) actionEvent.getSource()).isSelected();
    }

    @FXML
    public void clearTable(ActionEvent actionEvent) {
        resourceTable.getItems().clear();
    }
}
