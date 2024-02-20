module com.example.servercorreoelectronico {
    requires javafx.controls;
    requires javafx.fxml;
    requires MaterialFX;


    opens com.example.servercorreoelectronico to javafx.fxml;
    exports com.example.servercorreoelectronico;
}