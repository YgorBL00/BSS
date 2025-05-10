module com.example.bss {
    requires javafx.controls;
    requires javafx.fxml;

    opens com.example.bss to javafx.fxml;
    opens com.example.bss.model to javafx.base;
    // Se você tiver controllers FXML (por exemplo, em com.example.bss.ui), abra também para javafx.fxml:
    // opens com.example.bss.ui to javafx.fxml;

    exports com.example.bss;
    exports com.example.bss.model;
    // exports com.example.bss.ui; // Caso precise que outros módulos acessem o UI
}