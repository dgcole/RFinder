package rfinder.Controller;

import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
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

import java.util.ArrayList;
import java.util.Comparator;

public class ZoneController {
    @FXML
    private TableColumn<Zone, String> galCol, secCol, sysCol, bodCol, orbCol, btpCol;

    @FXML
    private TableColumn<Zone, Integer> zonCol, popCol;

    @FXML
    private TableView<Zone> zoneTable;

    @FXML
    Button refreshButton, clearButton;

    @FXML
    CheckBox resizeCheckbox;

    @FXML
    ComboBox<Galaxy> galaxyBox;

    @FXML
    ComboBox<Sector> sectorBox;

    @FXML
    ComboBox<System> systemBox;

    @FXML
    TextField rangeField;

    private static ZoneController instance;
    private boolean resize = false;

    static ZoneController getInstance() {
        return instance;
    }

    private final Runnable copier = new Runnable() {
        @Override
        public void run() {
            ObservableList<Zone> zones = zoneTable.getSelectionModel().getSelectedItems();
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

    @FXML
    public void initialize() {
        instance = this;

        zoneTable.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        refreshButton.setOnAction(this::refreshTable);
        clearButton.setOnAction(this::clearTable);
        resizeCheckbox.setOnAction(this::setResize);
        galaxyBox.setOnAction(this::setGalaxy);
        sectorBox.setOnAction(this::setSector);

        galCol.setCellValueFactory(param -> new ReadOnlyStringWrapper(param.getValue().getGalaxyName()));
        secCol.setCellValueFactory(param -> new ReadOnlyStringWrapper(param.getValue().getSectorName()));
        sysCol.setCellValueFactory(param -> new ReadOnlyStringWrapper(param.getValue().getSystemName()));
        bodCol.setCellValueFactory(param -> new ReadOnlyStringWrapper(param.getValue().getBodyName()));
        orbCol.setCellValueFactory(param -> new ReadOnlyStringWrapper(param.getValue().getOrbitalZone()));
        orbCol.setCellFactory(zoneColorFactory);
        btpCol.setCellValueFactory(param -> new ReadOnlyStringWrapper(param.getValue().getBodyType().toString()));


        Callback<TableColumn<Zone, Integer>, TableCell<Zone, Integer>> blanker = new Callback<TableColumn<Zone, Integer>, TableCell<Zone, Integer>>() {
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
                            String qualityBuilder = String.valueOf(item) +
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

            zoneTable.getColumns().add(newCol);
        }
        TableColumn<Zone, Object> fillerCol = new TableColumn<>("   ");
        zoneTable.getColumns().add(fillerCol);

        Galaxy placeholderGalaxy = new Galaxy();
        galaxyBox.setItems(FXCollections.observableArrayList(placeholderGalaxy));
        galaxyBox.setValue(placeholderGalaxy);
        galaxyBox.setCellFactory(MainController.getGalaxyBoxFactory());
        galaxyBox.setButtonCell(MainController.getGalaxyBoxFactory().call(null));

        Sector placeholderSector = new Sector();
        sectorBox.setItems(FXCollections.observableArrayList(placeholderSector));
        sectorBox.setValue(placeholderSector);
        sectorBox.setCellFactory(MainController.getSectorBoxFactory());
        sectorBox.setButtonCell(MainController.getSectorBoxFactory().call(null));

        System placeholderSystem = new System();
        systemBox.setItems(FXCollections.observableArrayList(placeholderSystem));
        systemBox.setValue(placeholderSystem);
        systemBox.setCellFactory(MainController.getSystemBoxFactory());
        systemBox.setButtonCell(MainController.getSystemBoxFactory().call(null));
    }

    @FXML
    public void setupStarmap() {
        ArrayList<Galaxy> galaxies = MainController.getInstance().getStarMap().getGalaxies();
        Galaxy placeholderGalaxy = new Galaxy();
        galaxies.sort(Comparator.comparing(Galaxy::getName));
        galaxies.add(0, placeholderGalaxy);
        galaxyBox.setItems(FXCollections.observableArrayList(galaxies));
        galaxyBox.setValue(placeholderGalaxy);
    }

    @FXML
    public void refreshTable(ActionEvent actionEvent) {
        zoneTable.getItems().clear();

        ArrayList<Zone> zones = MainController.getInstance().getStarMap().getZones();
        ZoneFilterTask zoneFilterTask = new ZoneFilterTask(zones, galaxyBox.getValue(), sectorBox.getValue(),
                systemBox.getValue(), rangeField.getText());

        zoneFilterTask.setOnSucceeded(event -> {
            zoneTable.setItems(FXCollections.observableArrayList(zoneFilterTask.getValue()));
            if (resize) {
                MainController.autoResize(zoneTable);
            }
        });

        RFinder.threadPool.submit(zoneFilterTask);
    }

    @FXML
    public void clearTable(ActionEvent actionEvent) {
        zoneTable.getItems().clear();
    }

    @FXML
    public void setResize(ActionEvent actionEvent) {
        resize = ((CheckBox) actionEvent.getSource()).isSelected();
    }

    @FXML
    public void setGalaxy(ActionEvent actionEvent) {
        MainController.updateSectorList(galaxyBox, sectorBox);
    }



    @FXML
    public void setSector(ActionEvent actionEvent) {
        MainController.updateSystemList(sectorBox, systemBox);
    }

    @FXML
    public void clearStarmap() {
        if (MainController.getInstance().getStarMap() == null) return;
        zoneTable.getItems().clear();
        galaxyBox.getItems().clear();
        galaxyBox.setItems(FXCollections.observableArrayList(new Galaxy()));
        sectorBox.getItems().clear();
        sectorBox.setItems(FXCollections.observableArrayList(new Sector()));
        systemBox.getItems().clear();
        systemBox.setItems(FXCollections.observableArrayList(new System()));
    }

    void registerCopier() {
        zoneTable.getScene().getAccelerators().clear();
        zoneTable.getScene().getAccelerators().put(new KeyCodeCombination(KeyCode.C, KeyCombination.CONTROL_ANY), copier);
    }
}
