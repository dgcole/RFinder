package rfinder.Controller;

import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.util.Callback;
import rfinder.Hazeron.*;
import rfinder.Hazeron.System;
import rfinder.RFinder;
import rfinder.Tasks.ZoneFilterTask;
import rfinder.Util.Colorizer;
import rfinder.Util.StarMapReceiver;

import java.util.ArrayList;
import java.util.Comparator;

public class ResourceController implements StarMapReceiver {
    @FXML
    TableColumn<Zone, String> galCol, secCol, sysCol, bodCol, orbCol, btpCol;

    @FXML
    TableColumn<Zone, Integer> zonCol, popCol;

    @FXML
    TableView<Zone> table;

    @FXML
    Button refreshButton, clearButton;

    @FXML
    CheckBox resizeCheckBox;

    @FXML
    ComboBox<Galaxy> galaxyBox;

    @FXML
    ComboBox<Sector> sectorBox;

    @FXML
    ComboBox<System> systemBox;

    @FXML
    TextField rangeField;

    private boolean resize = false;
    private StarMap starMap;

    private final Runnable copier = new Runnable() {
        @Override
        public void run() {
            ObservableList<Zone> zones = table.getSelectionModel().getSelectedItems();
            if (zones.size() == 0) return;
            final Clipboard clipboard = Clipboard.getSystemClipboard();
            final ClipboardContent content = new ClipboardContent();
            ArrayList<String> data = new ArrayList<>();
            for (Zone zone : zones) {
                data.add(zone.getGalaxyName());
                data.add(zone.getSectorName());
                data.add(zone.getSystemName());
                data.add(zone.getBodyName());
                data.add(zone.getZone() == 0 ? "" : String.valueOf(zonCol.getCellData(zone)));
                data.add(zone.getOrbitalZone());
                data.add(zone.getBodyType().toString());
                data.add(zone.getPopulationLimit() == 0 ? "" : String.valueOf(zone.getPopulationLimit()));
                for (int i = 0; i < ResourceType.getTypes().size(); i++) {
                    data.add(String.format("%d (%d%%)", zone.getQuality(i), zone.getAbundance(i)));
                }
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

    private static final Callback<TableColumn<Zone, String>, TableCell<Zone, String>> zoneColorFactory = param -> new TableCell<Zone, String>() {
        @Override
        protected void updateItem(String item, boolean empty) {
            super.updateItem(item, empty);
            setText(empty ? "" : item);
            setTextFill(empty ? Color.BLACK : Colorizer.getZoneColor(item));
        }
    };

    private static final Callback<TableColumn<Zone, Integer>, TableCell<Zone, Integer>> blanker = new Callback<TableColumn<Zone, Integer>, TableCell<Zone, Integer>>() {
        @Override
        public TableCell<Zone, Integer> call(TableColumn<Zone, Integer> param) {
            return new TableCell<Zone, Integer>() {
                @Override
                protected void updateItem(Integer item, boolean empty) {
                    if (empty) return;
                    super.setText(item == 0 ? "" : item.toString());
                }
            };
        }
    };

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

    @FXML
    void initialize() {
        MainController.registerController(this);

        table.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        refreshButton.setOnAction(actionEvent1 -> refreshTable());
        clearButton.setOnAction(actionEvent -> clearData());
        resizeCheckBox.setOnAction(actionEvent -> resize = ((CheckBox) actionEvent.getSource()).isSelected());
        galaxyBox.setOnAction(actionEvent -> updateSectorList());
        sectorBox.setOnAction(actionEvent -> updateSystemList());

        galCol.setCellValueFactory(param -> new ReadOnlyStringWrapper(param.getValue().getGalaxyName()));
        secCol.setCellValueFactory(param -> new ReadOnlyStringWrapper(param.getValue().getSectorName()));
        sysCol.setCellValueFactory(param -> new ReadOnlyStringWrapper(param.getValue().getSystemName()));
        bodCol.setCellValueFactory(param -> new ReadOnlyStringWrapper(param.getValue().getBodyName()));
        orbCol.setCellValueFactory(param -> new ReadOnlyStringWrapper(param.getValue().getOrbitalZone()));
        orbCol.setCellFactory(zoneColorFactory);
        btpCol.setCellValueFactory(param -> new ReadOnlyStringWrapper(param.getValue().getBodyType().toString()));

        zonCol.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue().getZone()));
        zonCol.setCellFactory(blanker);
        popCol.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue().getPopulationLimit()));
        popCol.setCellFactory(blanker);

        ArrayList<ResourceType> resourceTypes = ResourceType.getTypes();

        for (int i = 0; i < resourceTypes.size(); i++) {
            ResourceType resourceType = resourceTypes.get(i);
            final int index = i;

            TableColumn<Zone, Integer> newCol = new TableColumn<>(resourceType.toString());
            newCol.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue().getQuality(index)));
            newCol.setCellFactory(new Callback<TableColumn<Zone, Integer>, TableCell<Zone, Integer>>() {
                @Override
                public TableCell<Zone, Integer> call(TableColumn<Zone, Integer> param) {
                    return new TableCell<Zone, Integer>() {
                        @Override
                        protected void updateItem(Integer item, boolean empty) {
                            if (empty || item == null || this.getTableRow().getItem() == null) return;
                            if (item == 0) {
                                super.setText("");
                                return;
                            }
                            super.setTextFill(Colorizer.getQualityColor(item));
                            String qualityBuilder = item +
                                    " (" +
                                    ((Zone) this.getTableRow().getItem()).getAbundance(index) +
                                    "%)";
                            super.setText(qualityBuilder);
                        }
                    };
                }
            });

            Text text = new Text(resourceType.toString());
            newCol.setPrefWidth(Math.max(text.getLayoutBounds().getWidth() + 20.0, 60.0));

            table.getColumns().add(newCol);
        }

        TableColumn<Zone, Object> fillerCol = new TableColumn<>("   ");
        table.getColumns().add(fillerCol);

        Galaxy placeholderGalaxy = new Galaxy();
        galaxyBox.setItems(FXCollections.observableArrayList(placeholderGalaxy));
        galaxyBox.setValue(placeholderGalaxy);
        galaxyBox.setCellFactory(galaxyBoxFactory);
        galaxyBox.setButtonCell(galaxyBoxFactory.call(null));

        Sector placeholderSector = new Sector();
        sectorBox.setItems(FXCollections.observableArrayList(placeholderSector));
        sectorBox.setValue(placeholderSector);
        sectorBox.setCellFactory(sectorBoxFactory);
        sectorBox.setButtonCell(sectorBoxFactory.call(null));

        System placeholderSystem = new System();
        systemBox.setItems(FXCollections.observableArrayList(placeholderSystem));
        systemBox.setValue(placeholderSystem);
        systemBox.setCellFactory(systemBoxFactory);
        systemBox.setButtonCell(systemBoxFactory.call(null));
    }

    private void refreshTable() {
        if (starMap == null) return;

        if (table.getScene().getAccelerators().isEmpty()) {
            table.getScene().getAccelerators().put(new KeyCodeCombination(KeyCode.C, KeyCombination.CONTROL_ANY), copier);
        }
        table.getItems().clear();

        ArrayList<Zone> zones = starMap.getZones();
        ZoneFilterTask zoneFilterTask = new ZoneFilterTask(zones, galaxyBox.getValue(), sectorBox.getValue(),
                systemBox.getValue(), rangeField.getText());

        zoneFilterTask.setOnSucceeded(event -> {
            table.setItems(FXCollections.observableArrayList(zoneFilterTask.getValue()));
            if (resize) {
                autoResize(table);
            }
        });

        RFinder.threadPool.submit(zoneFilterTask);
    }

    private static <T> void autoResize(TableView<T> tableView) {
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

    @SuppressWarnings("Duplicates")
    private void updateSectorList() {
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

    @SuppressWarnings("Duplicates")
    private void updateSystemList() {
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

    @SuppressWarnings("Duplicates")
    @Override
    public void onStarMapUpdate(StarMap starMap) {
        this.starMap = starMap;

        ArrayList<Galaxy> galaxies = starMap.getGalaxies();
        Galaxy placeholderGalaxy = new Galaxy();
        galaxies.sort(Comparator.comparing(Galaxy::getName));
        galaxies.add(0, placeholderGalaxy);
        galaxyBox.setItems(FXCollections.observableArrayList(galaxies));
        galaxyBox.setValue(placeholderGalaxy);

        refreshTable();
    }

    private void clearData() {
        table.getItems().clear();
        galaxyBox.getItems().clear();
        galaxyBox.setItems(FXCollections.observableArrayList(new Galaxy()));
        sectorBox.getItems().clear();
        sectorBox.setItems(FXCollections.observableArrayList(new Sector()));
        systemBox.getItems().clear();
        systemBox.setItems(FXCollections.observableArrayList(new System()));
    }

    @Override
    public void onStarMapClear() {
        if (starMap == null) return;

        clearData();
        starMap = null;
    }


}
