module com.financecontrol.bss {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires javafx.graphics;
    requires javafx.base;
    requires itextpdf;
    requires java.desktop;
    requires org.update4j;

    opens app to javafx.fxml;
    opens app.controller to javafx.fxml;
    opens app.controller.admin to javafx.fxml;
    opens app.controller.vendedor to javafx.fxml, javafx.base;

    opens app.model to javafx.base;   // ⭐ CORREÇÃO AQUI

    exports app;
}