module com.financecontrol.bss {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires javafx.graphics;
    requires javafx.base;
    requires itextpdf;
    requires java.desktop;

    opens app to javafx.fxml;
    opens app.controller to javafx.fxml;
    opens app.controller.admin to javafx.fxml;

    opens app.controller.vendedor to javafx.fxml, javafx.base;

    exports app;
}