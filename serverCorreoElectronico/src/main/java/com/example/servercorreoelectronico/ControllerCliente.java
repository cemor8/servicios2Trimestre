package com.example.servercorreoelectronico;

import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXCheckbox;
import io.github.palexdev.materialfx.controls.MFXScrollPane;
import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;

public class ControllerCliente {
    private String correo;
    private ClienteThread hilo;
    private String host = "localhost";
    private int puerto = 5000;
    @FXML
    private MFXScrollPane contenido;
    ControllerCrearCorreo controllerCrearCorreo;

    @FXML
    private MFXCheckbox enviados;

    @FXML
    private MFXCheckbox recibidos;
    private VBox vBox;
    Socket socket;
    ObjectInputStream in;
    ObjectOutputStream out;
    private ArrayList<Correo> correosRecibidos = new ArrayList<>();
    private ArrayList<Correo> correosEnviados = new ArrayList<>();
    @FXML
    private MFXTextField mensaje;
    @FXML
    private MFXTextField correoText;

    public void recibirCorreo(String mensaje) throws IOException, ClassNotFoundException {
        this.correo = mensaje;

        socket = new Socket(host, puerto);

        out = new ObjectOutputStream(socket.getOutputStream());

        out.writeObject(new Correo(this.correo, "server", null));

        in = new ObjectInputStream(socket.getInputStream());

        Correo correoRecibido = (Correo) in.readObject();

        System.out.println("recuperando mensajes");
        System.out.println(correoRecibido);

        while (correoRecibido != null) {
            if (correoRecibido.getDestinatario().equalsIgnoreCase(this.correo)) {
                this.correosRecibidos.add(correoRecibido);
            }else if(correoRecibido.getRemitente().equalsIgnoreCase(this.correo)){
                this.correosEnviados.add(correoRecibido);
            }

            correoRecibido = (Correo) in.readObject();
        }
        System.out.println("acabado de recuperar");
        this.hilo = new ClienteThread(socket, this);
        this.hilo.start();
        System.out.println(correosEnviados.size());
        System.out.println(correosRecibidos.size());
        this.cargarEnviados();

    }

    public void cargarEnviados() {
        this.enviados.setSelected(true);
        this.recibidos.setSelected(false);
        this.vBox = new VBox();
        this.vBox.setFillWidth(true);
        this.contenido.setContent(this.vBox);
        if (this.correosEnviados.isEmpty()) {
            System.out.println("fuera");
            return;
        }


        for (Correo correo : this.correosEnviados) {
            HBox hBox = new HBox();
            hBox.getStyleClass().add("correo");
            Label remitente = new Label("Yo");
            Label texto = new Label(correo.getMensaje());
            MFXButton btn = new MFXButton();
            btn.setId(String.valueOf(this.correosEnviados.indexOf(correo)));
            btn.getStyleClass().add("btnVer");
            btn.setOnMouseClicked(this::verEnviado);

            remitente.getStyleClass().add("remitente");
            texto.getStyleClass().add("texto");
            HBox.setMargin(btn, new Insets(10, 0, 0, 80));
            btn.setText("Ver");
            btn.setMinWidth(50);
            HBox.setMargin(remitente, new Insets(10, 0, 0, 10));
            HBox.setMargin(texto, new Insets(10, 0, 0, 20));

            hBox.getChildren().addAll(remitente, texto, btn);
            this.vBox.getChildren().add(hBox);

        }
    }

    public void cargarRecibidos() {
        this.recibidos.setSelected(true);
        this.enviados.setSelected(false);
        this.vBox = new VBox();
        this.vBox.setFillWidth(true);
        this.contenido.setContent(this.vBox);
        if (this.correosRecibidos.isEmpty()) {
            System.out.println("fuera");
            return;
        }
        for (Correo correo : this.correosRecibidos) {
            HBox hBox = new HBox();
            hBox.getStyleClass().add("correo");
            hBox.setPrefWidth(Double.MAX_VALUE);
            Label remitente = new Label(correo.getRemitente());
            Label texto = new Label(correo.getMensaje());
            MFXButton btn = new MFXButton();
            btn.setId(String.valueOf(this.correosRecibidos.indexOf(correo)));
            btn.getStyleClass().add("btnVer");
            remitente.getStyleClass().add("remitente");
            texto.getStyleClass().add("texto");
            btn.setOnMouseClicked(this::verRecibido);
            HBox.setMargin(btn, new Insets(0, 0, 0, 10));
            HBox.setMargin(remitente, new Insets(0, 0, 0, 5));
            HBox.setMargin(texto, new Insets(0, 0, 0, 10));
            hBox.getChildren().addAll(remitente, texto, btn);
            this.vBox.getChildren().add(hBox);
        }
    }

    public void verEnviado(javafx.scene.input.MouseEvent event) {

        MFXButton button = (MFXButton) event.getSource();
        int id = 0;
        try {
            id = Integer.parseInt(button.getId());
        } catch (Exception err) {
            System.out.println(err.getMessage());
        }
        Correo correo = this.correosEnviados.get(id);

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("vistaCorreo.fxml"));
        Parent root = null;
        try {
            root = fxmlLoader.load();
        } catch (IOException err) {
            System.out.println(err.getMessage());
        }

        ControllerVistaCorreo controllerVistaCorreo = fxmlLoader.getController();
        controllerVistaCorreo.recibirData(correo);
        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.show();

    }

    public void verRecibido(javafx.scene.input.MouseEvent event) {
        MFXButton button = (MFXButton) event.getSource();
        System.out.println(button.getId());
        int id = 0;
        try {
            id = Integer.parseInt(button.getId());
        } catch (Exception err) {
            System.out.println(err.getMessage());
        }
        System.out.println(id);
        Correo correo = this.correosRecibidos.get(id);

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("vistaCorreo.fxml"));
        Parent root = null;
        try {
            root = fxmlLoader.load();
        } catch (IOException err) {
            System.out.println(err.getMessage());
        }
        ControllerVistaCorreo controllerVistaCorreo = fxmlLoader.getController();
        controllerVistaCorreo.recibirData(correo);
        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.show();
    }

    @FXML
    public void enviar() throws IOException {
        /*
        System.out.println("Enviando mensajes");
        out = new DataOutputStream(socket.getOutputStream());
        out.writeUTF("ENVIAR:"+this.correo+":"+this.correoText.getText()+":"+this.mensaje.getText());

         */

        Stage stage = new Stage();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("correo.fxml"));
        Parent nodo = fxmlLoader.load();
        ControllerCrearCorreo controllerCrearCorreo = fxmlLoader.getController();
        controllerCrearCorreo.recibirData(this.correo, this, this.socket);
        this.controllerCrearCorreo = controllerCrearCorreo;
        stage.setScene(new Scene(nodo));
        stage.show();

    }

    public void modificar() {
        this.controllerCrearCorreo.correoErroneo();
    }

    public void recibirMensaje(Correo correo) {
        Platform.runLater(() -> {
            if (correo.getRemitente().equalsIgnoreCase(this.correo)) {
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
