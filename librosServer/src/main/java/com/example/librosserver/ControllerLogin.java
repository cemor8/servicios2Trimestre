package com.example.librosserver;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import javax.crypto.*;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ControllerLogin {

    @FXML
    private MFXTextField introducirNombre;
    private String host = "localhost";
    private int puerto = 5000;
    ObjectInputStream in;
    ObjectOutputStream out;
    Map<String, String> columnasExpresiones = new HashMap<String, String>() {
        {
            put("mail", "^[a-zA-Z0-9._%+-]+@(gmail.com|hotmail.com)$");
        }

    };


    /**
     * Método que comprueba el login, si es correcto, lleva a la pantalla del panel al usuario.
     */
    @FXML
    void enviarCredenciales(ActionEvent event) throws IOException, ClassNotFoundException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {

        /*
        if (this.introducirNombre.getText().isEmpty() || !this.validarContenido(columnasExpresiones.get("mail"),this.introducirNombre.getText())){
            return;
        }

         */


        /* Comunicacion con server */



        Data data = new Data();
        KeyPairGeneratorExample keyPairGenerator = new KeyPairGeneratorExample();
        KeyPair keyPair = keyPairGenerator.generateKeyPair();
        data.setLlaves(keyPair);

        Socket socket = new Socket(host, puerto);
        data.setSocket(socket);


        out = new ObjectOutputStream(socket.getOutputStream());
        System.out.println("enviado publica");
        out.writeObject(data.getLlaves().getPublic());
        out.flush();

        in = new ObjectInputStream(socket.getInputStream());

        System.out.println("recibiendo publica");
        PublicKey llaveServer = (PublicKey) in.readObject();
        System.out.println("pase");
        if (llaveServer == null){
            System.out.println("error al recibir llave de server");
            return;
        }
        System.out.println("del todo");
        data.setLlaveServer(llaveServer);

        Encryptor encryptor = new Encryptor();
        Decryptor decryptor = new Decryptor();
        byte[] operacion = encryptor.encrypt("login",data.getLlaveServer());
        System.out.println("envio tamaño");
        out.writeInt(operacion.length);
        out.flush();
        System.out.println("envio operacion");
        out.write(operacion);
        out.flush();



        System.out.println("recibo texto");
        byte[] cipherText = encryptor.encrypt(introducirNombre.getText(),data.getLlaveServer());
        out.writeInt(cipherText.length);
        out.flush();
        out.write(cipherText);
        out.flush();

        int longitud = in.readInt();
        byte[] respuesta = new byte[longitud];
        in.readFully(respuesta);
        String respuestaDesc = decryptor.decrypt(respuesta,data.getLlaves().getPrivate());

        System.out.println(respuestaDesc);
        if (respuestaDesc.equalsIgnoreCase("repetido")){
            System.out.println("mal");
        }else {

            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("panel_principal.fxml"));
            Parent root = fxmlLoader.load();
            ControllerPanelPrincipal controllerPanelPrincipal = fxmlLoader.getController();
            controllerPanelPrincipal.establecerDatos(data);
            System.out.println("bien");



            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Chat");
            stage.show();
            MFXButton button = (MFXButton) event.getSource();
            Stage stageThis= (Stage) button.getScene().getWindow();
            stageThis.close();
        }

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