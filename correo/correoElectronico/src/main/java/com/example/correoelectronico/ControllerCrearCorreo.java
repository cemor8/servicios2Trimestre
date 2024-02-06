package com.example.correoelectronico;

import javafx.fxml.FXML;
import javafx.scene.input.MouseEvent;

public class ControllerCrearCorreo {
    private String correo;

    @FXML
    void enviar(MouseEvent event) {

    }
    private void recibirData(String mensaje){
        this.correo = mensaje;
    }

}

