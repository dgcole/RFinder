package rfinder.Controller;

import javafx.application.Platform;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.control.*;
import javafx.scene.input.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.util.Callback;
import rfinder.Hazeron.*;
import rfinder.Hazeron.System;
import rfinder.RFinder;
import rfinder.Tasks.DistributionCalculatorTask;
import rfinder.Tasks.ResourceFilterTask;
import rfinder.Util.Colorizer;
import rfinder.Util.StarMapHandler;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.File;
import java.util.ArrayList;
import java.util.Comparator;

public class MainController {

    @FXML
    private TableColumn<Resource, String> col1, col2, col3, col4, col5, col6, col7;

    @FXML
    private TableColumn<Resource, Integer> col8, col9, col10, col14, col11, col12, col13, col15;

    @FXML
    private TableView<Resource> resourceTable;

    @FXML
    private ComboBox<String> diameterBox, zoneBox;

    @FXML
    private ComboBox<ResourceType> resourceTypeBox;

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

    @FXML
    private LineChart<Integer, Double> distributionChart;

    @FXML
    private Tab resourceTab, zoneTab, analyzerTab, mapTab;

    private StarMap starMap;
    private boolean resize = false;
    private static MainController instance;

    private static final Callback<ListView<Galaxy>, ListCell<Galaxy>> galaxyBoxFactory = lv -> new ListCell<Galaxy>() {
        @Override
        protected void updateItem(Galaxy item, boolean empty) {
            super.updateItem(item, empty);
            setText(empty ? "" : item.getName());
        }
    };

    private static final Callback<ListView<Sector>, ListCell<Sector>> sectorBoxFactory = lv -> new ListCell<Sector>() {
        @Override
        protected void updateItem(Sector item, boolean empty) {
            super.updateItem(item, empty);
            setText(empty ? "" : item.getName());
        }
    };

    private static final Callback<ListView<System>, ListCell<System>> systemBoxFactory = lv -> new ListCell<System>() {
        @Override
        protected void updateItem(System item, boolean empty) {
            super.updateItem(item, empty);
            setText(empty ? "" : item.getName());
        }
    };

    private static final Callback<TableColumn<Resource, String>, TableCell<Resource, String>> zoneColorFactory = param -> new TableCell<Resource, String>() {
        @Override
        protected void updateItem(String item, boolean empty) {
            super.updateItem(item, empty);
            setText(empty ? "" : item);
            setTextFill(empty ? Color.BLACK : Colorizer.getZoneColor(item));
        }
    };

    private final Runnable copier = new Runnable() {
        @Override
        public void run() {
            ObservableList<Resource> resources = resourceTable.getSelectionModel().getSelectedItems();
            if (resources.size() == 0) return;
            final Clipboard clipboard = Clipboard.getSystemClipboard();
            final ClipboardContent content = new ClipboardContent();
            ArrayList<String> data = new ArrayList<>();
            for (Resource r : resources) {
                data.add(r.getResourceType().toString());
                data.add(r.getGalaxy());
                data.add(r.getSector());
                data.add(r.getSystem());
                data.add(r.getBody());
                data.add(r.getDiameter());
                data.add(r.getZone());
                data.add(String.valueOf(r.getQ1()));
                data.add(String.valueOf(r.getQ2()));
                data.add(String.valueOf(r.getQ3()));
                data.add(String.valueOf(r.getQ4()));
                data.add(String.valueOf(r.getA1()));
                data.add(String.valueOf(r.getA2()));
                data.add(String.valueOf(r.getA3()));
                data.add(String.valueOf(r.getA4()));
                data.add("\n");
            }
            StringBuilder raw = new StringBuilder();
            for (String s : data) {
                raw.append(s);
                if (!s.equals("\n")) raw.append("\t");
            }
            content.putString(raw.toString());
            clipboard.setContent(content);
        }
    };

    @SuppressWarnings("Duplicates")
    @FXML
    void initialize() {
        instance = this;

        importItem.setOnAction(this::importStarmap);
        clearItem.setOnAction(this::clearStarmap);
        exitItem.setOnAction(this::exit);
        aboutItem.setOnAction(this::about);
        refreshButton.setOnAction(this::refreshTable);
        resizeCheckbox.setOnAction(this::setResize);
        clearButton.setOnAction(this::clearTable);
        galaxyBox.setOnAction(this::setGalaxy);
        sectorBox.setOnAction(this::setSector);
        resourceTab.setOnSelectionChanged(this::registerResourceCopier);
        zoneTab.setOnSelectionChanged(this::registerZoneCopier);
        analyzerTab.setOnSelectionChanged(this::registerAnalyzerCopier);

        resourceTable.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        Platform.runLater(() -> resourceTable.getScene().getAccelerators().put(new KeyCodeCombination(KeyCode.C, KeyCombination.CONTROL_ANY), copier));

        col1.setCellValueFactory(param -> new ReadOnlyStringWrapper(param.getValue().getResourceType().toString()));
        col2.setCellValueFactory(param -> new ReadOnlyStringWrapper(param.getValue().getGalaxy()));
        col3.setCellValueFactory(param -> new ReadOnlyStringWrapper(param.getValue().getSector()));
        col4.setCellValueFactory(param -> new ReadOnlyStringWrapper(param.getValue().getSystem()));
        col5.setCellValueFactory(param -> new ReadOnlyStringWrapper(param.getValue().getBody()));
        col6.setCellValueFactory(param -> new ReadOnlyStringWrapper(param.getValue().getDiameter()));
        col7.setCellValueFactory(param -> new ReadOnlyStringWrapper(param.getValue().getZone()));
        col7.setCellFactory(zoneColorFactory);

        Callback<TableColumn<Resource, Integer>, TableCell<Resource, Integer>> blanker =
                new Callback<TableColumn<Resource, Integer>, TableCell<Resource, Integer>>() {
            @Override
            public TableCell<Resource, Integer> call(TableColumn<Resource, Integer> param) {
                return new TableCell<Resource, Integer>() {
                    @Override
                    protected void updateItem(Integer item, boolean empty) {
                        if (item != null && !empty) {
                            if (item == 0) {
                                super.setText("");
                                return;
                            }
                            super.setText(item.toString());
                            this.setTextFill(Colorizer.getQualityColor(item));
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
        col14.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue().getQ4()));
        col14.setCellFactory(blanker);
        col11.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue().getA1()));
        col11.setCellFactory(percentAdder);
        col12.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue().getA2()));
        col12.setCellFactory(percentAdder);
        col13.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue().getA3()));
        col13.setCellFactory(percentAdder);
        col15.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue().getA4()));
        col15.setCellFactory(percentAdder);

        ArrayList<ResourceType> resourceTypes = new ArrayList<>(ResourceType.getTypes());
        resourceTypes.add(0, ResourceType.ANY);
        resourceTypeBox.setItems(FXCollections.observableArrayList(resourceTypes));
        resourceTypeBox.setValue(ResourceType.ANY);

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


        diameterBox.setValue("Any");
        diameterBox.setDisable(true);

        zoneBox.setItems(FXCollections.observableArrayList("Any", "Star", "Inferno", "Inner", "Habitable", "Frigid", "Outer"));
        zoneBox.setValue("Any");

        galaxyBox.setCellFactory(galaxyBoxFactory);
        galaxyBox.setButtonCell(galaxyBoxFactory.call(null));

        sectorBox.setCellFactory(sectorBoxFactory);
        sectorBox.setButtonCell(sectorBoxFactory.call(null));

        systemBox.setCellFactory(systemBoxFactory);
        systemBox.setButtonCell(systemBoxFactory.call(null));
    }

    @FXML
    public void exit(ActionEvent actionEvent) {
        Platform.exit();
        java.lang.System.exit(0);
    }

    @FXML
    public void about(ActionEvent actionEvent) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("About");
        alert.setHeaderText("RFinder 1.0.0");
        alert.setContentText("Developed by expert700.");

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
            clearStarmap(actionEvent);
            ZoneController.getInstance().clearStarmap();
            try {

                Task<StarMap> mainTask = new Task<StarMap>() {
                    @Override
                    protected StarMap call() throws Exception {
                        SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
                        SAXParser saxParser = saxParserFactory.newSAXParser();

                        StarMapHandler starMapHandler = new StarMapHandler();
                        saxParser.parse(selectedFile, starMapHandler);

                        return starMapHandler.getStarMap();
                    }
                };

                mainTask.setOnSucceeded(event -> {
                    starMap = mainTask.getValue();
                    refreshTable(null);
                    ZoneController.getInstance().refreshTable(null);

                    ArrayList<Galaxy> galaxies = starMap.getGalaxies();
                    Galaxy placeholderGalaxy = new Galaxy();
                    galaxies.sort(Comparator.comparing(Galaxy::getName));
                    galaxies.add(0, placeholderGalaxy);
                    galaxyBox.setItems(FXCollections.observableArrayList(galaxies));
                    galaxyBox.setValue(placeholderGalaxy);

                    ZoneController.getInstance().setupStarmap();

                    DistributionCalculatorTask distributionCalculatorTask = new DistributionCalculatorTask(starMap.getResources());
                    distributionCalculatorTask.setOnSucceeded(param -> distributionChart.getData().add(distributionCalculatorTask.getValue()));
                    RFinder.threadPool.submit(distributionCalculatorTask);
                });

                RFinder.threadPool.submit(mainTask);
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

        int minQual = Integer.parseInt(minimumQuality.textProperty().get());

        ResourceFilterTask resourceFilterTask = new ResourceFilterTask(starMap.getResources(),
                resourceTypeBox.getValue(), minQual, galaxyBox.getValue(), sectorBox.getValue(), systemBox.getValue(),
                range.getText(), diameterBox.getValue(), zoneBox.getValue());

        resourceFilterTask.setOnSucceeded(event -> {
            resourceTable.setItems(FXCollections.observableArrayList(resourceFilterTask.getValue()));
            if (resize) {
                autoResize(resourceTable);
            }
        });

        RFinder.threadPool.submit(resourceFilterTask);
    }

    @FXML
    public void clearStarmap(ActionEvent actionEvent) {
        if (starMap == null) return;
        resourceTable.getItems().clear();
        galaxyBox.getItems().clear();
        galaxyBox.setItems(FXCollections.observableArrayList(new Galaxy()));
        sectorBox.getItems().clear();
        sectorBox.setItems(FXCollections.observableArrayList(new Sector()));
        systemBox.getItems().clear();
        systemBox.setItems(FXCollections.observableArrayList(new System()));
        distributionChart.getData().clear();
        starMap = null;
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
        updateSectorList(galaxyBox, sectorBox);
    }

    @FXML
    public void setSector(ActionEvent actionEvent) {
        updateSystemList(sectorBox, systemBox);
    }

    public static void updateSystemList(ComboBox<Sector> sectorBox, ComboBox<System> systemBox) {
        if (sectorBox.getValue() == null) return;
        System placeholderSystem = new System();
        if (sectorBox.getValue().isPlaceholder()) {
            systemBox.setItems(FXCollections.observableArrayList(placeholderSystem));
            systemBox.setValue(placeholderSystem);
            return;
        }
        ArrayList<System> systems = sectorBox.getValue().getSystems();
        systems.sort(Comparator.comparing(System::getName));
        systems.add(0, placeholderSystem);
        systemBox.setItems(FXCollections.observableArrayList(systems));
        systemBox.setValue(placeholderSystem);
    }

    public static void updateSectorList(ComboBox<Galaxy> galaxyBox, ComboBox<Sector> sectorBox) {
        if (galaxyBox.getValue() == null) return;
        Sector placeholderSector = new Sector();
        if (galaxyBox.getValue().isPlaceholder()) {
            sectorBox.setItems(FXCollections.observableArrayList(placeholderSector));
            sectorBox.setValue(placeholderSector);
            return;
        }
        ArrayList<Sector> sectors = galaxyBox.getValue().getSectors();
        sectors.sort(Comparator.comparing(Sector::getName));
        sectors.add(0, placeholderSector);
        sectorBox.setItems(FXCollections.observableArrayList(sectors));
        sectorBox.setValue(placeholderSector);
    }

    public static MainController getInstance() {
        return instance;
    }

    public StarMap getStarMap() {
        return starMap;
    }

    public static Callback<ListView<Galaxy>, ListCell<Galaxy>> getGalaxyBoxFactory() {
        return galaxyBoxFactory;
    }

    public static Callback<ListView<Sector>, ListCell<Sector>> getSectorBoxFactory() {
        return sectorBoxFactory;
    }

    public static Callback<ListView<System>, ListCell<System>> getSystemBoxFactory() {
        return systemBoxFactory;
    }

    public static boolean checkRange(Sector originSector, System originSystem, System target, int parsecs) {
        double originX, originY, originZ;
        originX = originY = originZ = 0;
        boolean set = false;
        if ((originSystem == null || originSystem.isPlaceholder())
                && (originSector != null && !originSector.isPlaceholder())) {
            originX = originSector.getX() * 10;
            originY = originSector.getY() * 10;
            originZ = originSector.getZ() * 10;
            set = true;
        } else if ((originSystem != null && !originSystem.isPlaceholder())) {
            originX = originSystem.getX();
            originY = originSystem.getY();
            originZ = originSystem.getZ();
            set = true;
        }

        if (set) {
            double targetX = target.getX();
            double targetY = target.getY();
            double targetZ = target.getZ();

            double dist = Math.sqrt((originX - targetX) * (originX - targetX) +
                    (originY - targetY) * (originY - targetY) + (originZ - targetZ) * (originZ - targetZ));
            return dist <= parsecs;
        } else {
            return false;
        }
    }

    static <T> void autoResize(TableView<T> tableView) {
        if (tableView.getItems().isEmpty()) return;
        tableView.getColumns().forEach((col) -> {
            Text t = new Text(col.getText());
            double max = t.getLayoutBounds().getWidth();
            for (int i = 0; i < tableView.getItems().size(); i = i + (tableView.getItems().size() / 50) + 1) {
                if (((TableColumn) col).getCellData(i) != null) {
                    t = new Text(((TableColumn) col).getCellData(i).toString());
                    double calcwidth = t.getLayoutBounds().getWidth();
                    if (calcwidth > max) {
                        max = calcwidth;
                    }
                }
            }
            t = new Text(col.getText());
            col.setPrefWidth(Math.max(max + 50.0, t.getLayoutBounds().getWidth() + 20.0));
        });
    }

    @FXML
    private void registerResourceCopier(Event event) {
        if (resourceTab.isSelected()) {
            resourceTable.getScene().getAccelerators().clear();
            resourceTable.getScene().getAccelerators().put(new KeyCodeCombination(KeyCode.C, KeyCombination.CONTROL_ANY), copier);
        }
    }

    @FXML
    private void registerZoneCopier(Event event) {
        if (zoneTab.isSelected()) {
            ZoneController.getInstance().registerCopier();
        }
    }

    @FXML
    private void registerAnalyzerCopier(Event event) {
        if (analyzerTab.isSelected()) {
            AnalyzerController.getInstance().registerCopier();
        }
    }
}
