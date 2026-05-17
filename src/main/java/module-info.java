module com.financecontrol.bss {

    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;
    requires javafx.base;

    requires java.sql;
    requires java.desktop;

    requires itextpdf;

    opens app to javafx.fxml;
    opens app.controller to javafx.fxml;
    opens app.controller.admin to javafx.fxml;
    opens app.controller.vendedor to javafx.fxml, javafx.base;

    opens app.model to javafx.base;

    exports app;
}