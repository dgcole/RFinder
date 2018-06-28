package rfinder.Filter;

import javafx.collections.FXCollections;
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
        rangeField.textProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal.isEmpty()) return;
            if (!newVal.matches("\\d*")) {
                rangeField.setText(newVal.replaceAll("[^\\d]", ""));
            }
        });

        StarMap starMap = MainController.getInstance().getStarMap();
        if (starMap == null) return;
        ArrayList<Galaxy> galaxies = starMap.getGalaxies();
        Galaxy placeholderGalaxy = new Galaxy();
        galaxies.add(0, placeholderGalaxy);
        galaxyBox.setItems(FXCollections.observableArrayList(galaxies));
        galaxyBox.setValue(placeholderGalaxy);
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
    private void updateSectorList() {

    }
}
