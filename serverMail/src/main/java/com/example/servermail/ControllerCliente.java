package com.example.servermail;

import io.github.palexdev.materialfx.controls.MFXCheckbox;
import io.github.palexdev.materialfx.controls.MFXScrollPane;
import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;

public class ControllerCliente {
    private String correo;
    private ClienteThread hilo;
    private String host = "localhost";
    //puerto del server
    private int puerto = 5000;
    @FXML
    private MFXScrollPane contenido;

    @FXML
    private MFXCheckbox enviados;

    @FXML
    private MFXCheckbox recibidos;
    private VBox vBox;
    Socket socket;
    DataInputStream in;
    DataOutputStream out;
    private ArrayList<Correo> correosRecibidos = new ArrayList<>();
    private ArrayList<Correo> correosEnviados = new ArrayList<>();
    @FXML
    private MFXTextField mensaje;
    @FXML
    private MFXTextField correoText;

    public void recibirCorreo(String mensaje) throws IOException {
        this.correo = mensaje;
        socket = new Socket(host,puerto);

        out = new DataOutputStream(socket.getOutputStream());
        out.writeUTF("LOGIN:"+this.correo);

        in=new DataInputStream(socket.getInputStream());
        String correoRecibido = in.readUTF();
        System.out.println("recuperando mensajes");
        System.out.println(correoRecibido);
        while (!correoRecibido.equalsIgnoreCase("fin")){
            String[] partes = correoRecibido.split(":");
            System.out.println(Arrays.toString(partes));
            switch (partes[0]) {
                case "ENVIADO":
                    this.correosEnviados.add(new Correo(partes[1],partes[2],partes[3]));
                    break;
                case "RECIBIDO":
                    System.out.println("entre");
                    this.correosRecibidos.add(new Correo(partes[1],partes[2],partes[3]));
                    break;
                default:
                    break;
            }
            correoRecibido = in.readUTF();
        }
        System.out.println("acabado de recuperar");
        this.hilo = new ClienteThread(socket,this);
        this.hilo.start();
        System.out.println(correosEnviados.size());
        System.out.println(correosRecibidos.size());
        this.cargarEnviados();

    }
    public void cargarEnviados(){
        this.enviados.setSelected(true);
        this.recibidos.setSelected(false);
        this.vBox = new VBox();
        this.contenido.setContent(this.vBox);
        if(this.correosEnviados.isEmpty()){
            System.out.println("fuera");
            return;
        }


        for(Correo correo : this.correosEnviados){
            HBox hBox = new HBox();
            Label remitente = new Label("Yo: ");
            Label texto = new Label(correo.getMensaje());
            hBox.getChildren().addAll(remitente,texto);
            this.vBox.getChildren().add(hBox);

        }
    }
    public void cargarRecibidos(){
        this.recibidos.setSelected(true);
        this.enviados.setSelected(false);
        this.vBox = new VBox();
        this.contenido.setContent(this.vBox);
        if(this.correosRecibidos.isEmpty()){
            System.out.println("fuera");
            return;
        }
        for(Correo correo : this.correosRecibidos){
            HBox hBox = new HBox();
            Label remitente = new Label(correo.getRemitente());
            Label texto = new Label(correo.getMensaje());
            hBox.getChildren().addAll(remitente,texto);
            this.vBox.getChildren().add(hBox);
        }
    }
    @FXML
    public void enviar() throws IOException {
        /*
        Stage stage = new Stage();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("correo.fxml"));
        Parent nodo = fxmlLoader.load();
        ControllerCrearCorreo controllerCrearCorreo = fxmlLoader.getController();
        controllerCrearCorreo.recibirData(this.correo);
        stage.setScene(new Scene(nodo));
        stage.show();

         */
        System.out.println("Enviando mensajes");
        out = new DataOutputStream(socket.getOutputStream());
        out.writeUTF("ENVIAR:"+this.correo+":"+this.correoText.getText()+":"+this.mensaje.getText());

    }
    public void recibirMensaje(Correo correo, String protocolo){
        Platform.runLater(() -> {
            if (protocolo.equalsIgnoreCase("ENVIADO")) {
                this.correosEnviados.add(correo);
                if (this.enviados.isSelected()) {
                    this.cargarEnviados();
                }
            } else {
                this.correosRecibidos.add(correo);
                if (this.recibidos.isSelected()) {
                    this.cargarRecibidos();
                }
            }
        });


    }

}
