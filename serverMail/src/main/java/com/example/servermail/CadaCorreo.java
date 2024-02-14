package com.example.servermail;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class CadaCorreo {

    @FXML
    private Label contenido;

    @FXML
    private Label destinatario;

    @FXML
    private Label remitente;
    private Correo correo;

    public void recibir(Correo correo){
        this.correo = correo;
        this.rellenar();
    }
    public void rellenar(){
        this.destinatario.setText(this.correo.getDestinatario());
        this.contenido.setText(this.correo.getMensaje());
        this.remitente.setText(this.correo.getRemitente());
    }

}

