module com.example.servermail {
    requires javafx.controls;
    requires javafx.fxml;
    requires MaterialFX;


    opens com.example.servermail to javafx.fxml;
    exports com.example.servermail;
}