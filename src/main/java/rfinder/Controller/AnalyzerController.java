package rfinder.Controller;

import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.*;
import javafx.scene.layout.VBox;
import rfinder.Filter.RangeFilter;
import rfinder.Filter.ResourceFilter;
import rfinder.Hazeron.System;
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
    private static AnalyzerController instance;

    private final Runnable copier = new Runnable() {
        @Override
        public void run() {
            ObservableList<System> systems = systemTable.getSelectionModel().getSelectedItems();
            if (systems.size() == 0) return;
            final Clipboard clipboard = Clipboard.getSystemClipboard();
            final ClipboardContent content = new ClipboardContent();
            ArrayList<String> data = new ArrayList<>();
            for (System system : systems ) {
                data.add(system.getParent().getParent().getName());
                data.add(system.getParent().getName());
                data.add(system.getName());
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

    @FXML
    public void initialize() {
        instance = this;

        systemTable.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        resourceFilters = new ArrayList<>();
        rangeFilters = new ArrayList<>();

        addButton.setOnAction(actionEvent -> addResourceFilter());
        clearButton.setOnAction(actionEvent -> clearFilters());
        scanButton.setOnAction(actionEvent -> scanSystems());
        tableClearButton.setOnAction(actionEvent -> clearTable());

        galCol.setCellValueFactory(param -> new ReadOnlyStringWrapper(param.getValue().getParent().getParent().getName()));
        secCol.setCellValueFactory(param -> new ReadOnlyStringWrapper(param.getValue().getParent().getName()));
        sysCol.setCellValueFactory(param -> new ReadOnlyStringWrapper(param.getValue().getName()));

        ArrayList<String> filterTypes = new ArrayList<>();
        filterTypes.add("Range Filter");
        filterTypes.add("Resource Filter");
        filterTypeBox.setItems(FXCollections.observableArrayList(filterTypes));
        filterTypeBox.setValue(filterTypes.get(0));
    }

    private void addResourceFilter() {
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

    private void clearFilters() {
        filterBox.getChildren().clear();
        resourceFilters.clear();
        rangeFilters.clear();
    }

    private void scanSystems() {
        if ((resourceFilters.size() == 0 && rangeFilters.size() == 0)
                || MainController.getInstance().getStarMap() == null) return;

        SystemFilterTask systemFilterTask = new SystemFilterTask(MainController.getInstance().getStarMap().getSystems(),
                resourceFilters, rangeFilters);

        systemFilterTask.setOnSucceeded(event -> systemTable.setItems(FXCollections.observableArrayList(systemFilterTask.getValue())));

        RFinder.threadPool.submit(systemFilterTask);
    }

    private void clearTable() {
        systemTable.getItems().clear();
    }

    static AnalyzerController getInstance() {
        return instance;
    }

    void registerCopier() {
        systemTable.getScene().getAccelerators().clear();
        systemTable.getScene().getAccelerators().put(new KeyCodeCombination(KeyCode.C, KeyCombination.CONTROL_ANY), copier);
    }
}
