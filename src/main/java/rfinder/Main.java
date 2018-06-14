package rfinder;

import javafx.application.Application;
import rfinder.Controller.RFinder;

public class Main {
    public static Exception lastException;

    public static void main(String[] args) {
        Application.launch(RFinder.class, args);
    }
}
