package rfinder.Controller;

import javafx.fxml.FXML;
import rfinder.Hazeron.StarMap;
import rfinder.Util.StarMapReceiver;

public class ResourceController implements StarMapReceiver {

    @FXML
    void initialize() {
        MainController.getInstance().registerController(this);
    }
    @Override
    public void onStarMapUpdate(StarMap starMap) {

    }

    @Override
    public void onStarMapClear() {

    }
}
