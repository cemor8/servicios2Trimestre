package com.example.librosserver;


import io.github.palexdev.materialfx.controls.MFXButton;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;


import java.io.IOException;

public class ControllerCadaLibro {
    @FXML
    private ImageView meterImagen;

    @FXML
    private Label autor;

    @FXML
    private MFXButton btn;

    @FXML
    private Label titulo;
    private Libro libro;
    private Data data;

    /**
     * Método que se encarga de cargar la vista de cada libro
     * @param event
     */
    @FXML
    void ver(MouseEvent event) {

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("vista_libro.fxml"));
        Parent root = null;
        try {
            root = fxmlLoader.load();
        }catch (IOException err){
            System.out.println(err.getMessage());
        }
        ControllerVistaDetallada controllerVistaDetallada =fxmlLoader.getController();
        controllerVistaDetallada.establecerDatos(this.data,libro);
        this.data.getControllers().getControllerPanelPrincipal().cambiarContenido(root);


    }

    /**
     * Método que se encarga de recibir informacion
     * @param data
     * @param libro
     */
    public void recibirData(Data data, Libro libro){
        this.data = data;
        this.libro = libro;
        this.titulo.setText(this.libro.getTitulo());
        this.autor.setText(this.libro.getAutor());
        meterImagen.setPreserveRatio(false);
        this.meterImagen.setImage(new Image("file:"+this.libro.getImagen()));
    }

}

