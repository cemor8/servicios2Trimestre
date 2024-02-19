package com.example.servermail;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ControllerLogin {

    @FXML
    private MFXTextField introducirNombre;
    Map<String, String> columnasExpresiones = new HashMap<String, String>() {
        {
            put("mail", "^[a-zA-Z0-9._%+-]+@(gmail.com|hotmail.com)$");
        }

    };


    /**
     * Método que comprueba el login, si es correcto, lleva a la pantalla del panel al usuario.
     */
    @FXML
    void enviarCredenciales(ActionEvent event) throws IOException {
        if (this.introducirNombre.getText().isEmpty() || !this.validarContenido(columnasExpresiones.get("mail"),this.introducirNombre.getText())){
            return;
        }



        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("hello-view.fxml"));
        Parent root = fxmlLoader.load();
        ControllerCliente cliente = fxmlLoader.getController();
        cliente.recibirCorreo(this.introducirNombre.getText());
        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.setTitle("Chat");
        stage.show();
        MFXButton button = (MFXButton) event.getSource();
        Stage stageThis= (Stage) button.getScene().getWindow();
        stageThis.close();
    }
    /**
     * Método que devuelve true si se cumple una expresion regular en una string
     *
     * @param patron       expresion regular
     * @param texto_buscar texto donde buscar el patron
     */
    public boolean validarContenido(String patron, String texto_buscar) {
        Pattern patronValidar = Pattern.compile(patron);
        Matcher matcher = patronValidar.matcher(texto_buscar);
        return matcher.matches();
    }

}
