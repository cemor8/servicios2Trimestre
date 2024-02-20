package com.example.servercorreoelectronico;

import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.input.MouseEvent;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ControllerCrearCorreo {
    private String direccion;
    @FXML
    private TextArea contenido;
    private ObjectOutputStream out;

    @FXML
    private MFXTextField destinatario;
    private Socket socket;
    private ControllerCliente cliente;
    @FXML
    private Label correoErroneo;

    @FXML
    void enviar(MouseEvent event) throws IOException {
        if(this.contenido.getText().isEmpty() || this.destinatario.getText().isEmpty()){
            return;
        }
        out = new ObjectOutputStream(socket.getOutputStream());
        out.writeObject(new Correo(this.destinatario.getText(),this.direccion,this.contenido.getText()));
    }
    public void recibirData(String mensaje, ControllerCliente cliente, Socket socket){
        this.direccion = mensaje;
        this.cliente = cliente;
        this.socket = socket;

    }public void correoErroneo(){
        Platform.runLater(() -> {
            this.correoErroneo.setText("Mal");
        });
    }

}

