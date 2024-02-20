package com.example.servercorreoelectronico;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class ControllerVistaCorreo {

    @FXML
    private Label labelDescripcion;

    @FXML
    private Label labelDestinatario;

    @FXML
    private Label labelRemitente;
    public void recibirData(Correo correo){
        this.labelDescripcion.setText(correo.getMensaje());
        this.labelDestinatario.setText(correo.getDestinatario());
        this.labelRemitente.setText(correo.getRemitente());
    }

}

