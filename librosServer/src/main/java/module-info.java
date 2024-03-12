module com.example.librosserver {
    requires javafx.controls;
    requires javafx.fxml;
    requires MaterialFX;


    opens com.example.librosserver to javafx.fxml;
    exports com.example.librosserver;
}