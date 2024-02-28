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
    private Label labelNombre;
    private Stage stage;


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

    /**
     * Método que se encarga de inicializar el cliente, envia un mensaje al servidor especifico para el login y recibe y carga correos
     * @param mensaje
     * @throws IOException
     * @throws ClassNotFoundException
     */

    public void recibirCorreo(String mensaje) throws IOException, ClassNotFoundException {
        this.correo = mensaje;
        this.labelNombre.setText("Hola, " + this.correo);
        socket = new Socket(host, puerto);

        out = new ObjectOutputStream(socket.getOutputStream());

        out.writeObject(new Correo(this.correo, "server", null, null));

        in = new ObjectInputStream(socket.getInputStream());

        Correo correoRecibido = (Correo) in.readObject();

        System.out.println("recuperando mensajes");
        System.out.println(correoRecibido);

        while (correoRecibido != null) {
            if (correoRecibido.getDestinatario().equalsIgnoreCase(correoRecibido.getRemitente())&& correoRecibido.getDestinatario().equalsIgnoreCase(this.correo)){
                this.correosRecibidos.add(correoRecibido);
                this.correosEnviados.add(correoRecibido);
            } else if (correoRecibido.getDestinatario().equalsIgnoreCase(this.correo)) {
                this.correosRecibidos.add(correoRecibido);
            } else if (correoRecibido.getRemitente().equalsIgnoreCase(this.correo)) {
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

    /**
     * Método que se encarga de cargar los correos enviados en la vista
     */
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
            Label remitente = new Label("Para: "+correo.getDestinatario());
            Label texto = new Label(correo.getAsunto());
            MFXButton btn = new MFXButton();
            btn.setId(String.valueOf(this.correosEnviados.indexOf(correo)));
            btn.getStyleClass().add("btnVer");
            btn.setOnMouseClicked(this::verEnviado);

            remitente.getStyleClass().add("remitente");
            texto.getStyleClass().add("texto");
            HBox.setMargin(btn, new Insets(10, 20, 0, 70));
            btn.setText("Ver");
            btn.setMinWidth(50);
            HBox.setMargin(remitente, new Insets(15, 0, 0, 10));
            HBox.setMargin(texto, new Insets(15, 0, 0, 70));

            hBox.getChildren().addAll(remitente, texto, btn);
            this.vBox.getChildren().add(hBox);
            VBox.setMargin(hBox,new Insets(10,5,0,10));

        }
    }

    /**
     * Método que se encarga de cargar los correos recibidos en la vista
     */
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
            Label texto = new Label(correo.getAsunto());
            MFXButton btn = new MFXButton();
            btn.setId(String.valueOf(this.correosRecibidos.indexOf(correo)));
            btn.getStyleClass().add("btnVer");
            btn.setText("Ver");
            btn.setMinWidth(50);
            remitente.getStyleClass().add("remitente");
            texto.getStyleClass().add("texto");
            btn.setOnMouseClicked(this::verRecibido);
            HBox.setMargin(btn, new Insets(10, 20, 0, 70));
            HBox.setMargin(remitente, new Insets(15, 0, 0, 5));
            HBox.setMargin(texto, new Insets(15, 0, 0, 70));

            hBox.getChildren().addAll(remitente, texto, btn);
            this.vBox.getChildren().add(hBox);
            VBox.setMargin(hBox,new Insets(10,5,0,10));
        }
    }

    /**
     * Método que carga la vista detallada de un correo enviado
     * @param event
     */
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
    /**
     * Método que carga la vista detallada de un correo recibido
     * @param event
     */
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

    /**
     * Método que carga la nueva vista para enviar un nuevo correo
     * @throws IOException
     */
    @FXML
    public void enviar() throws IOException {

        Stage stage = new Stage();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("correo.fxml"));
        Parent nodo = fxmlLoader.load();
        ControllerCrearCorreo controllerCrearCorreo = fxmlLoader.getController();
        controllerCrearCorreo.recibirData(this.correo, this, this.socket);
        this.controllerCrearCorreo = controllerCrearCorreo;
        stage.setScene(new Scene(nodo));
        stage.show();
        this.stage = stage;


    }

    public void modificar() {
        this.controllerCrearCorreo.correoErroneo();
    }

    /**
     * Método que se encarga de recibir los nuevos correos y añadirlos a la lista
     * @param correo
     */
    public void recibirMensaje(Correo correo) {
        Platform.runLater(() -> {

            if (correo.getRemitente().equalsIgnoreCase(correo.getDestinatario())){
                System.out.println("iguales");
                this.correosEnviados.add(correo);
                this.correosRecibidos.add(correo);
                if (this.enviados.isSelected()) {
                    this.cargarEnviados();
                }else {
                    this.cargarRecibidos();
                }
            }else if (correo.getRemitente().equalsIgnoreCase(this.correo)) {
                System.out.println("entre");
                System.out.println(correo);
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
            if (this.stage != null) {
                stage.close();
            }
        });


    }

}
