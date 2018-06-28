package rfinder.Filter;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import rfinder.Hazeron.ResourceType;

import java.io.IOException;
import java.util.ArrayList;

@SuppressWarnings("Duplicates")
public class ResourceFilter extends GridPane {
    @FXML
    ComboBox<ResourceType> resourceTypeBox;

    @FXML
    TextField qualityField;

    public ResourceFilter() {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getClassLoader().getResource("resource_filter.fxml"));
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
        qualityField.textProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal.isEmpty()) return;
            if (!newVal.matches("\\d*")) {
                qualityField.setText(newVal.replaceAll("[^\\d]", ""));
            }
        });

        ArrayList<ResourceType> resourceTypes = new ArrayList<>(ResourceType.getTypes());
        resourceTypes.add(0, ResourceType.ANY);
        resourceTypeBox.setItems(FXCollections.observableArrayList(resourceTypes));
    }

    public int getQuality() {
        return qualityField.getText().isEmpty() ? 1 : Integer.parseInt(qualityField.getText());
    }

    public ResourceType getType() {
        return resourceTypeBox.getValue();
    }
}
