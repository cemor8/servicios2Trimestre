module com.example.correoelectronico {
    requires javafx.controls;
    requires javafx.fxml;
    requires MaterialFX;


    opens com.example.correoelectronico to javafx.fxml;
    exports com.example.correoelectronico;
}