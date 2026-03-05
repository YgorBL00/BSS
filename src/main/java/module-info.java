module com.financecontrol.bss {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;

    opens app to javafx.fxml;
    opens app.controller to javafx.fxml;

    exports app;
}