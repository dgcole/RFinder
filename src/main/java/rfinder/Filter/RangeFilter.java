package rfinder.Filter;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import rfinder.Controller.MainController;
import rfinder.Hazeron.Galaxy;
import rfinder.Hazeron.Sector;
import rfinder.Hazeron.StarMap;
import rfinder.Hazeron.System;

import java.io.IOException;
import java.util.ArrayList;

@SuppressWarnings("Duplicates")
public class RangeFilter extends GridPane {
    @FXML
    ComboBox<Galaxy> galaxyBox;

    @FXML
    ComboBox<Sector> sectorBox;

    @FXML
    ComboBox<System> systemBox;

    @FXML
    TextField rangeField;

    public RangeFilter() {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getClassLoader().getResource("range_filter.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    public void initialize() {
        galaxyBox.setOnAction(this::setGalaxy);
        sectorBox.setOnAction(this::setSector);

        rangeField.textProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal.isEmpty()) return;
            if (!newVal.matches("\\d*")) {
                rangeField.setText(newVal.replaceAll("[^\\d]", ""));
            }
        });

        galaxyBox.setCellFactory(MainController.getGalaxyBoxFactory());
        galaxyBox.setButtonCell(MainController.getGalaxyBoxFactory().call(null));

        sectorBox.setCellFactory(MainController.getSectorBoxFactory());
        sectorBox.setButtonCell(MainController.getSectorBoxFactory().call(null));

        systemBox.setCellFactory(MainController.getSystemBoxFactory());
        systemBox.setButtonCell(MainController.getSystemBoxFactory().call(null));

        StarMap starMap = MainController.getInstance().getStarMap();

        Galaxy placeholderGalaxy = new Galaxy();
        if (starMap == null) {
            galaxyBox.setItems(FXCollections.observableArrayList(placeholderGalaxy));
        } else {
            ArrayList<Galaxy> galaxies = starMap.getGalaxies();
            galaxies.add(0, placeholderGalaxy);
            galaxyBox.setItems(FXCollections.observableArrayList(galaxies));
        }
        galaxyBox.setValue(placeholderGalaxy);

        Sector placeholderSector = new Sector();
        sectorBox.setItems(FXCollections.observableArrayList(placeholderSector));
        sectorBox.setValue(placeholderSector);

        System placeholderSystem = new System();
        systemBox.setItems(FXCollections.observableArrayList(placeholderSystem));
        systemBox.setValue(placeholderSystem);
    }

    public Galaxy getGalaxy() {
        return galaxyBox.getValue();
    }

    public Sector getSector() {
        return sectorBox.getValue();
    }

    public System getSystem() {
        return systemBox.getValue();
    }

    public int getRange() {
        return rangeField.getText().isEmpty() ? 0 : Integer.parseInt(rangeField.getText());
    }

    @FXML
    private void setGalaxy(ActionEvent actionEvent) {
        MainController.updateSectorList(galaxyBox, sectorBox);
    }

    @FXML
    private void setSector(ActionEvent actionEvent) {
        MainController.updateSystemList(sectorBox, systemBox);
    }
}
