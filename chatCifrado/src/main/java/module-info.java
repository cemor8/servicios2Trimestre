module com.example.chatcifrado {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.chatcifrado to javafx.fxml;
    exports com.example.chatcifrado;
}