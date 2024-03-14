package com.example.librosserver;

import io.github.palexdev.materialfx.controls.MFXButton;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

import java.io.IOException;

public class ControllerVistaDetallada {
    @FXML
    private MFXButton btnVolver;
    @FXML
    private TextArea desc;

    @FXML
    private ImageView img;

    @FXML
    private Label labelAutor;

    @FXML
    private Label labelTitulo;
    private Data data;
    private Libro libro;

    @FXML
    void volver(MouseEvent event) throws IOException {
        this.data.getControllers().getControllerMenu().verLibros(null);
    }
    /**
     * Método que se encarga de recibir el modelo para que el controlador tenga acceso a el, tambien
     * añade el titulo y el autor de libro a un label
     * */
    public void establecerDatos(Data data, Libro libro){
        this.data = data;
        this.libro = libro;
        this.labelAutor.setText(this.libro.getAutor());
        this.labelTitulo.setText(this.libro.getTitulo());
        this.desc.setText(libro.getDescripcion());
        this.img.setImage(new Image("file:"+this.libro.getImagen()));
    }
}
