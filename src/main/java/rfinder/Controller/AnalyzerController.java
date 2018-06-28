package rfinder.Controller;

import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import rfinder.Filter.RangeFilter;
import rfinder.Hazeron.System;
import rfinder.Filter.ResourceFilter;
import rfinder.RFinder;
import rfinder.Tasks.SystemFilterTask;

import java.util.ArrayList;


public class AnalyzerController {
    @FXML
    VBox filterBox;

    @FXML
    Button addButton, clearButton, scanButton, tableClearButton;

    @FXML
    ComboBox<String> filterTypeBox;

    @FXML
    TableView<System> systemTable;

    @FXML
    TableColumn<System, String> galCol, secCol, sysCol;

    private ArrayList<ResourceFilter> resourceFilters;
    private ArrayList<RangeFilter> rangeFilters;

    @FXML
    public void initialize() {
        resourceFilters = new ArrayList<>();
        rangeFilters = new ArrayList<>();

        addButton.setOnAction(this::addResourceFilter);
        clearButton.setOnAction(this::clearFilters);
        scanButton.setOnAction(this::scanSystems);
        tableClearButton.setOnAction(this::clearTable);

        galCol.setCellValueFactory(param -> new ReadOnlyStringWrapper(param.getValue().getParent().getParent().getName()));
        secCol.setCellValueFactory(param -> new ReadOnlyStringWrapper(param.getValue().getParent().getName()));
        sysCol.setCellValueFactory(param -> new ReadOnlyStringWrapper(param.getValue().getName()));

        ArrayList<String> filterTypes = new ArrayList<String>();
        filterTypes.add("Resource Filter");
        filterTypes.add("Range Filter");
        filterTypeBox.setItems(FXCollections.observableArrayList(filterTypes));
        filterTypeBox.setValue(filterTypes.get(0));
    }

    private void addResourceFilter(ActionEvent actionEvent) {
        String selected = filterTypeBox.getValue();
        if (selected.isEmpty()) return;
        if (selected.equals("Resource Filter")) {
            ResourceFilter resourceFilter = new ResourceFilter();
            resourceFilters.add(resourceFilter);
            filterBox.getChildren().add(resourceFilter);
            filterBox.getChildren().add(new Separator());
        } else if (selected.equals("Range Filter")) {
            RangeFilter rangeFilter = new RangeFilter();
            rangeFilters.add(rangeFilter);
            filterBox.getChildren().add(rangeFilter);
            filterBox.getChildren().add(new Separator());
        }
    }

    private void clearFilters(ActionEvent actionEvent) {
        filterBox.getChildren().clear();
        resourceFilters.clear();
        rangeFilters.clear();
    }

    private void scanSystems(ActionEvent actionEvent) {
        if ((resourceFilters.size() == 0 && rangeFilters.size() == 0)
                || MainController.getInstance().getStarMap() == null) return;

        SystemFilterTask systemFilterTask = new SystemFilterTask(MainController.getInstance().getStarMap().getSystems(),
                resourceFilters, rangeFilters);

        systemFilterTask.setOnSucceeded(event -> {
            systemTable.setItems(FXCollections.observableArrayList(systemFilterTask.getValue()));
        });

        RFinder.threadPool.submit(systemFilterTask);
    }

    private void clearTable(ActionEvent actionEvent) {
        systemTable.getItems().clear();
    }
}
