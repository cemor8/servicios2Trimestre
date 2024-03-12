package com.example.librosserver;


import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.AnchorPane;


import java.io.IOException;

public class ControllerPanelPrincipal {

    @FXML
    private AnchorPane main;

    @FXML
    private AnchorPane menuLateral;

    @FXML
    private AnchorPane rellenarContenido;
    private Data data;


    /**
     * Método que se encarga de cargar el inicio de la app
     * */
    public void cargarLibros() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("VistaLibros.fxml"));
        Parent root = fxmlLoader.load();
        ControllerVistaLibro controllerVistaLibro = fxmlLoader.getController();
        controllerVistaLibro.iniciar(this.data);
        this.cambiarContenido(root);
    }
    /**
     * Método que se encarga de cambiar el contenido del anchor que
     * mostrara las diferentes vistas de la app
     * */
    public void cambiarContenido(Parent root){
        this.rellenarContenido.getChildren().setAll(root);
    }

    /**
     * Método que se encarga de recibir el modelo para que el controlador tenga acceso a el
     * */
    public void establecerDatos(Data data) throws IOException {
        this.data = data;
        this.cargarLibros();
        this.cargarMenu();
        this.data.getControllers().setControllerPanelPrincipal(this);

    }
    public void cargarMenu() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("menu.fxml"));
        Parent root = fxmlLoader.load();
        ControllerMenu controllerMenu = fxmlLoader.getController();
        controllerMenu.establecerDatos(this.data);
        this.menuLateral.getChildren().setAll(root);
    }

}

