package com.example.servermail;

import javafx.fxml.FXML;
import javafx.scene.input.MouseEvent;

public class ControllerCrearCorreo {
    private String correo;

    @FXML
    void enviar(MouseEvent event) {

    }
    public void recibirData(String mensaje){
        this.correo = mensaje;
    }

}

