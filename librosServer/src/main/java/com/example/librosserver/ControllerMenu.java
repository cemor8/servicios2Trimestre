package com.example.librosserver;


import javafx.css.PseudoClass;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;


import java.io.IOException;

public class ControllerMenu {

    @FXML
    private HBox hboxConfig;

    @FXML
    private HBox hboxDetallada;

    @FXML
    private HBox hboxLibros;

    @FXML
    private HBox hboxMeter;

    @FXML
    private HBox hboxUsuario;

    @FXML
    private ImageView imagenConfig;

    @FXML
    private ImageView imagenDetallada;

    @FXML
    private ImageView imagenLibros;

    @FXML
    private ImageView imagenMeter;

    @FXML
    private ImageView imagenUsuario;

    @FXML
    private Label labelAñadirLibro;

    @FXML
    private Label labelConfig;

    @FXML
    private Label labelLibros;

    @FXML
    private Label labelMostrarNombre;

    @FXML
    private Label labelUsuario;

    @FXML
    private Label labelVista;
    private Data data;

    /**
     * Método que se encarga de mostrar el apartado de añadir libros
     * */
    @FXML
    void meterLibro(MouseEvent event) throws IOException {
        this.data.setViendo(false);
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("meter_libro.fxml"));
        Parent root = fxmlLoader.load();
        ControllerMeterLibro controllerMeterLibro = fxmlLoader.getController();
        controllerMeterLibro.recibirDatos(this.data);
        this.data.getControllers().getControllerPanelPrincipal().cambiarContenido(root);

        this.labelAñadirLibro.pseudoClassStateChanged(PseudoClass.getPseudoClass("selected"), true);
        this.hboxMeter.pseudoClassStateChanged(PseudoClass.getPseudoClass("selected"), true);

        this.imagenMeter.setImage(new Image(getClass().getResourceAsStream("/images/plusgris.png")));


    }
    /**
     * Método que se encarga de mostrar la lista con todos los libros
     * */
    @FXML
    void verLibros(MouseEvent event) throws IOException {
        this.data.setViendo(true);


        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("vistaLibros.fxml"));
        Parent root = fxmlLoader.load();

        ControllerVistaLibro controllerVistaLibro = fxmlLoader.getController();
        controllerVistaLibro.iniciar(this.data);

        this.data.getControllers().getControllerPanelPrincipal().cambiarContenido(root);

        this.labelLibros.pseudoClassStateChanged(PseudoClass.getPseudoClass("selected"), true);
        this.hboxLibros.pseudoClassStateChanged(PseudoClass.getPseudoClass("selected"), true);

        this.imagenLibros.setImage(new Image(getClass().getResourceAsStream("/images/librosgris.png")));


    }

    /**
     * Método que se encarga de recibir el modelo para que el controlador tenga acceso a el, tambien muestra
     * el nombre del usuario en un label.
     * */
    public void establecerDatos(Data data) {
        this.data = data;
        this.data.getControllers().setControllerMenu(this);
    }
}
