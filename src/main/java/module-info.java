module rfinder {
    requires javafx.graphics;
    requires javafx.fxml;
    requires javafx.controls;
    requires java.xml;

    exports rfinder;
    exports rfinder.Controller;

    opens rfinder.Controller;
}