package rfinder.Controller;

import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.util.Callback;
import rfinder.Hazeron.*;
import rfinder.Hazeron.System;
import rfinder.RFinder;
import rfinder.Util.StarMapHandler;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.File;
import java.util.ArrayList;
import java.util.Comparator;

public class Main {

    @FXML
    private TableColumn<Resource, String> col1, col2, col3, col4, col5, col6, col7;

    @FXML
    private TableColumn<Resource, Integer> col8, col9, col10, col11, col12, col13;

    @FXML
    private TableView<Resource> resourceTable;

    @FXML
    private ComboBox<String> resourceTypeBox, diameterBox, zoneBox;

    @FXML
    private ComboBox<Galaxy> galaxyBox;

    @FXML
    private ComboBox<Sector> sectorBox;

    @FXML
    private ComboBox<System> systemBox;

    @FXML
    private TextField minimumQuality, range;

    @FXML
    private MenuItem importItem, clearItem, exitItem, aboutItem;

    @FXML
    private Button refreshButton, clearButton;

    @FXML
    private CheckBox resizeCheckbox;

    private StarMap starMap;
    private boolean resize = false;

    @SuppressWarnings("Duplicates")
    @FXML
    void initialize() {
        importItem.setOnAction(this::importStarmap);
        clearItem.setOnAction(this::clearStarmap);
        exitItem.setOnAction(this::exit);
        aboutItem.setOnAction(this::about);
        refreshButton.setOnAction(this::refreshTable);
        resizeCheckbox.setOnAction(this::setResize);
        clearButton.setOnAction(this::clearTable);
        galaxyBox.setOnAction(this::setGalaxy);
        sectorBox.setOnAction(this::setSector);

        col1.setCellValueFactory(param -> new ReadOnlyStringWrapper(param.getValue().getResource()));
        col2.setCellValueFactory(param -> new ReadOnlyStringWrapper(param.getValue().getGalaxy()));
        col3.setCellValueFactory(param -> new ReadOnlyStringWrapper(param.getValue().getSector()));
        col4.setCellValueFactory(param -> new ReadOnlyStringWrapper(param.getValue().getSystem()));
        col5.setCellValueFactory(param -> new ReadOnlyStringWrapper(param.getValue().getBody()));
        col6.setCellValueFactory(param -> new ReadOnlyStringWrapper(param.getValue().getDiameter()));
        col7.setCellValueFactory(param -> new ReadOnlyStringWrapper(param.getValue().getZone()));
        Callback<TableColumn<Resource, String>, TableCell<Resource, String>> colorizer =
                new Callback<TableColumn<Resource, String>, TableCell<Resource, String>>() {
            @Override
            public TableCell<Resource, String> call(TableColumn<Resource, String> param) {
                return new TableCell<Resource, String>() {
                    @Override
                    protected void updateItem(String item, boolean empty) {
                        super.updateItem(item, empty);
                        setText(empty ? null : getString());
                        if (getString().contains("Star")) this.setTextFill(Color.RED);
                        else if (getString().contains("Inf")) this.setTextFill(Color.RED);
                        else if (getString().contains("Inn")) this.setTextFill(Color.ORANGE);
                        else if (getString().contains("Hab")) this.setTextFill(Color.GREEN);
                        else if (getString().contains("Out")) this.setTextFill(Color.BLUE);
                        else if (getString().contains("Fri")) this.setTextFill(Color.LIGHTBLUE);
                    }

                    private String getString() {
                        return getItem() == null ? "" : getItem();
                    }
                };
            }
        };
        col7.setCellFactory(colorizer);

        Callback<TableColumn<Resource, Integer>, TableCell<Resource, Integer>> blanker =
                new Callback<TableColumn<Resource, Integer>, TableCell<Resource, Integer>>() {
            @Override
            public TableCell<Resource, Integer> call(TableColumn<Resource, Integer> param) {
                return new TableCell<Resource, Integer>() {
                    @Override
                    protected void updateItem(Integer item, boolean empty) {
                        if (item != null && !empty) {
                            if (((Integer) item) == 0) {
                                super.setText("");
                                return;
                            }
                            super.setText(item.toString());
                            if (((Integer) item) < 75) this.setTextFill(Color.RED);
                            else if (((Integer) item) < 155) this.setTextFill(Color.ORANGE);
                            else if (((Integer) item) < 225) this.setTextFill(Color.GREEN);
                            else if (((Integer) item) < 245) this.setTextFill(Color.DARKGREEN);
                            else this.setTextFill(Color.BLUE);
                        } else {
                            super.setText("");
                        }
                    }
                };
            }
        };

        Callback<TableColumn<Resource, Integer>, TableCell<Resource, Integer>> percentAdder =
                new Callback<TableColumn<Resource, Integer>, TableCell<Resource, Integer>>() {
            @Override
            public TableCell<Resource, Integer> call(TableColumn<Resource, Integer> param) {
                return new TableCell<Resource, Integer>() {
                    @Override
                    protected void updateItem(Integer item, boolean empty) {
                        super.setText(empty ? "" : item.toString() + "%");
                    }
                };
            }
        };

        col8.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue().getQ1()));
        col8.setCellFactory(blanker);
        col9.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue().getQ2()));
        col9.setCellFactory(blanker);
        col10.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue().getQ3()));
        col10.setCellFactory(blanker);
        col11.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue().getA1()));
        col11.setCellFactory(percentAdder);
        col12.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue().getA2()));
        col12.setCellFactory(percentAdder);
        col13.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue().getA3()));
        col13.setCellFactory(percentAdder);

        ArrayList<String> resourceTypeNames = ResourceType.getAllNames();
        resourceTypeNames.sort(Comparator.comparing(String::toString));
        resourceTypeNames.add(0, "Any");
        resourceTypeBox.setItems(FXCollections.observableArrayList(resourceTypeNames));
        resourceTypeBox.setValue("Any");

        Galaxy placeholderGalaxy = new Galaxy();
        galaxyBox.setItems(FXCollections.observableArrayList(placeholderGalaxy));
        galaxyBox.setValue(placeholderGalaxy);

        Sector placeholderSector = new Sector();
        sectorBox.setItems(FXCollections.observableArrayList(placeholderSector));
        sectorBox.setValue(placeholderSector);

        System placeholderSystem = new System();
        systemBox.setItems(FXCollections.observableArrayList(placeholderSystem));
        systemBox.setValue(placeholderSystem);

        minimumQuality.textProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal.isEmpty()) return;
            if (!newVal.matches("\\d*")) {
                minimumQuality.setText(newVal.replaceAll("[^\\d]", ""));
            } else if (Integer.parseInt(newVal) > 255) {
                minimumQuality.setText("255");
            } else if (Integer.parseInt(newVal) < 1) {
                minimumQuality.setText("1");
            }
        });

        range.textProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal.isEmpty()) return;
            if (!newVal.matches("\\d*")) {
                range.setText(newVal.replaceAll("[^\\d]", ""));
            }
        });


        diameterBox.setItems(FXCollections.observableArrayList("Any", "1800m", "3800m", "5700m",
                "7600m", "9400m", "11400m", "15200m", "17000m", "19000m", "20800m", "22800m", "Ringworld"));
        diameterBox.setValue("Any");

        zoneBox.setItems(FXCollections.observableArrayList("Any", "Star", "Inferno", "Inner", "Habitable", "Frigid", "Outer"));
        zoneBox.setValue("Any");

        Callback<ListView<Galaxy>, ListCell<Galaxy>> galaxyBoxFactory = lv -> new ListCell<Galaxy>() {
            @Override
            protected void updateItem(Galaxy item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty ? "" : item.getName());
            }
        };
        galaxyBox.setCellFactory(galaxyBoxFactory);
        galaxyBox.setButtonCell(galaxyBoxFactory.call(null));

        Callback<ListView<Sector>, ListCell<Sector>> sectorBoxFactory = lv -> new ListCell<Sector>() {
            @Override
            protected void updateItem(Sector item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty ? "" : item.getName());
            }
        };
        sectorBox.setCellFactory(sectorBoxFactory);
        sectorBox.setButtonCell(sectorBoxFactory.call(null));

        Callback<ListView<System>, ListCell<System>> systemBoxFactory = lv -> new ListCell<System>() {
            @Override
            protected void updateItem(System item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty ? "" : item.getName());
            }
        };
        systemBox.setCellFactory(systemBoxFactory);
        systemBox.setButtonCell(systemBoxFactory.call(null));
    }

    @FXML
    public void exit(ActionEvent actionEvent) {
        RFinder.mainStage.close();
    }

    @FXML
    public void about(ActionEvent actionEvent) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("About");
        alert.setHeaderText("RFinder 0.1.3");
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
                refreshTable(null);

                ArrayList<Galaxy> galaxies = starMap.getGalaxies();
                Galaxy placeholderGalaxy = new Galaxy();
                galaxies.sort(Comparator.comparing(Galaxy::getName));
                galaxies.add(0, placeholderGalaxy);
                galaxyBox.setItems(FXCollections.observableArrayList(galaxies));
                galaxyBox.setValue(placeholderGalaxy);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @SuppressWarnings("ConstantConditions")
    @FXML
    public void refreshTable(ActionEvent actionEvent) {
        if (starMap == null) return;
        resourceTable.getItems().clear();

        String type = resourceTypeBox.getValue();
        int minQual = Integer.parseInt(minimumQuality.textProperty().get());
        for (Resource r : starMap.getResources()) {
            boolean resourceMatch = (type == null || type.equals("Any")) || r.getResource().equals(type);

            boolean qualityMatch = (r.getQ1() >= minQual ||
                r.getQ2() >= minQual || r.getQ3() >= minQual);

            boolean galaxyMatch = (galaxyBox.getValue() == null || galaxyBox.getValue().isPlaceholder())
                    || galaxyBox.getValue() == r.getGalaxyInternal();

            boolean sectorMatch = (sectorBox.getValue() == null || sectorBox.getValue().isPlaceholder())
                    || sectorBox.getValue() == r.getSectorInternal();

            boolean systemMatch = (systemBox.getValue() == null || systemBox.getValue().isPlaceholder())
                    || systemBox.getValue() == r.getSystemInternal();

            boolean diameterMatch = (diameterBox.getValue() == null || diameterBox.getValue().equals("Any"))
                    || (diameterBox.getValue().equals("Ringworld") && r.getBody().contains("Ringworld"));

            boolean zoneMatch = (zoneBox.getValue() == null || zoneBox.getValue().equals("Any"))
                    || zoneBox.getValue().equals(r.getZone());

            if (!diameterMatch) {
                diameterMatch = diameterBox.getValue().equals(r.getDiameter());
            }

            boolean rangeMatch = false;

            if (!range.getText().isEmpty()) {
                double targetX, targetY, targetZ;
                targetX = targetY = targetZ = 0;
                boolean set = false;
                if ((systemBox.getValue() == null || systemBox.getValue().isPlaceholder())
                        && (sectorBox.getValue() != null && !sectorBox.getValue().isPlaceholder())) {
                    Sector target = sectorBox.getValue();
                    targetX = target.getX() * 10;
                    targetY = target.getY() * 10;
                    targetZ = target.getZ() * 10;
                    set = true;
                } else if ((systemBox.getValue() != null && !systemBox.getValue().isPlaceholder())) {
                    System target = systemBox.getValue();
                    targetX = target.getX();
                    targetY = target.getY();
                    targetZ = target.getZ();
                    set = true;
                }

                if (set) {
                    double originX = r.getSystemInternal().getX();
                    double originY = r.getSystemInternal().getY();
                    double originZ = r.getSystemInternal().getZ();

                    double dist = Math.sqrt((targetX - originX) * (targetX - originX) +
                            (targetY - originY) * (targetY - originY) + (targetZ - originZ) * (targetZ - originZ));
                    if (dist < Double.parseDouble(range.getText())) rangeMatch = true;
                } else {
                    rangeMatch = true;
                }
            }


            if (resourceMatch && qualityMatch && diameterMatch && zoneMatch && ((galaxyMatch && sectorMatch && systemMatch) || rangeMatch)) {
                resourceTable.getItems().add(r);
            }
        }

        if (resize) {
            autoResize();
        }
    }

    private void autoResize() {
        resourceTable.getColumns().forEach((col) -> {
            Text t = new Text(col.getText());
            double max = t.getLayoutBounds().getWidth();
            for (int i = 0; i < resourceTable.getItems().size(); i = i + (resourceTable.getItems().size() / 50)) {
                if (((TableColumn) col).getCellData(i) != null) {
                    t = new Text(((TableColumn) col).getCellData(i).toString());
                    double calcwidth = t.getLayoutBounds().getWidth();
                    if (calcwidth > max) {
                        max = calcwidth;
                    }
                }
            }
            col.setPrefWidth(max + 20.0d);
        });
    }

    @FXML
    public void clearStarmap(ActionEvent actionEvent) {
        resourceTable.getItems().clear();
        galaxyBox.getItems().clear();
        galaxyBox.setItems(FXCollections.observableArrayList(new Galaxy()));
        sectorBox.getItems().clear();
        sectorBox.setItems(FXCollections.observableArrayList(new Sector()));
        systemBox.getItems().clear();
        systemBox.setItems(FXCollections.observableArrayList(new System()));
    }

    @FXML
    public void setResize(ActionEvent actionEvent) {
        resize = resizeCheckbox.isSelected();
    }

    @FXML
    public void clearTable(ActionEvent actionEvent) {
        resourceTable.getItems().clear();
    }

    @FXML
    public void setGalaxy(ActionEvent actionEvent) {
        if (galaxyBox.getValue() == null || galaxyBox.getValue().isPlaceholder()) return;
        ArrayList<Sector> sectors = galaxyBox.getValue().getSectors();
        sectors.sort(Comparator.comparing(Sector::getName));
        sectors.add(0, new Sector());
        sectorBox.setItems(FXCollections.observableArrayList(sectors));
    }

    @FXML
    public void setSector(ActionEvent actionEvent) {
        if (sectorBox.getValue() == null || sectorBox.getValue().isPlaceholder()) return;
        ArrayList<System> systems = sectorBox.getValue().getSystems();
        systems.sort(Comparator.comparing(System::getName));
        systems.add(0, new System());
        systemBox.setItems(FXCollections.observableArrayList(systems));
    }
}
