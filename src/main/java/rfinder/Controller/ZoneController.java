package rfinder.Controller;

import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.text.Text;
import javafx.util.Callback;
import rfinder.Hazeron.*;
import rfinder.Hazeron.System;
import rfinder.Tasks.ZoneFilterTask;
import rfinder.Util.Colorizer;

import java.util.ArrayList;
import java.util.Arrays;
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

    @FXML
    public void initialize() {
        instance = this;

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
        btpCol.setCellValueFactory(param -> new ReadOnlyStringWrapper(param.getValue().getBodyType()));

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

        ArrayList<ResourceType> resourceTypes = new ArrayList<>(Arrays.asList(ResourceType.values()));
        resourceTypes.sort(Comparator.comparing(ResourceType::toString));

        for (int i = 0; i < resourceTypes.size(); i++) {
            ResourceType resourceType = resourceTypes.get(i);
            final int index = i;

            TableColumn<Zone, Integer> newCol = new TableColumn<>(resourceType.toString());
            newCol.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Zone, Integer>, ObservableValue<Integer>>() {
                @Override
                public ObservableValue<Integer> call(TableColumn.CellDataFeatures<Zone, Integer> param) {
                    return new ReadOnlyObjectWrapper<>(param.getValue().getQuality(index));
                }
            });
            newCol.setCellFactory(new Callback<TableColumn<Zone, Integer>, TableCell<Zone, Integer>>() {
                @Override
                public TableCell<Zone, Integer> call(TableColumn<Zone, Integer> param) {
                    return new TableCell<Zone, Integer>() {
                        @Override
                        protected void updateItem(Integer item, boolean empty) {
                            if (empty || item == null) return;
                            if (item == 0) {
                                super.setText("");
                                return;
                            }
                            super.setTextFill(Colorizer.getQualityColor(item));
                            String qualityBuilder = String.valueOf(item) +
                                    " (" +
                                    //TODO; NPE here when refreshing table.
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

        Galaxy placeholderGalaxy = new Galaxy();
        galaxyBox.setItems(FXCollections.observableArrayList(placeholderGalaxy));
        galaxyBox.setValue(placeholderGalaxy);
        galaxyBox.setCellFactory(Main.getGalaxyBoxFactory());
        galaxyBox.setButtonCell(Main.getGalaxyBoxFactory().call(null));

        Sector placeholderSector = new Sector();
        sectorBox.setItems(FXCollections.observableArrayList(placeholderSector));
        sectorBox.setValue(placeholderSector);
        sectorBox.setCellFactory(Main.getSectorBoxFactory());
        sectorBox.setButtonCell(Main.getSectorBoxFactory().call(null));

        System placeholderSystem = new System();
        systemBox.setItems(FXCollections.observableArrayList(placeholderSystem));
        systemBox.setValue(placeholderSystem);
        systemBox.setCellFactory(Main.getSystemBoxFactory());
        systemBox.setButtonCell(Main.getSystemBoxFactory().call(null));
    }

    @FXML
    public void setupStarmap() {
        ArrayList<Galaxy> galaxies = Main.getInstance().getStarMap().getGalaxies();
        Galaxy placeholderGalaxy = new Galaxy();
        galaxies.sort(Comparator.comparing(Galaxy::getName));
        galaxies.add(0, placeholderGalaxy);
        galaxyBox.setItems(FXCollections.observableArrayList(galaxies));
        galaxyBox.setValue(placeholderGalaxy);
    }

    @FXML
    public void refreshTable(ActionEvent actionEvent) {
        zoneTable.getItems().clear();

        ArrayList<Zone> zones = Main.getInstance().getStarMap().getZones();
        ZoneFilterTask zoneFilterTask = new ZoneFilterTask(zones, galaxyBox.getValue(), sectorBox.getValue(),
                systemBox.getValue(), rangeField.getText());

        zoneFilterTask.setOnSucceeded(event -> {
            zoneTable.setItems(FXCollections.observableArrayList(zoneFilterTask.getValue()));
        });

        new Thread(zoneFilterTask).start();
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
        Main.updateSectorList(galaxyBox, sectorBox);
    }



    @FXML
    public void setSector(ActionEvent actionEvent) {
        Main.updateSystemList(sectorBox, systemBox);
    }

    @FXML
    public void clearStarmap() {
        if (Main.getInstance().getStarMap() == null) return;
        zoneTable.getItems().clear();
        galaxyBox.getItems().clear();
        galaxyBox.setItems(FXCollections.observableArrayList(new Galaxy()));
        sectorBox.getItems().clear();
        sectorBox.setItems(FXCollections.observableArrayList(new Sector()));
        systemBox.getItems().clear();
        systemBox.setItems(FXCollections.observableArrayList(new System()));
    }
}
