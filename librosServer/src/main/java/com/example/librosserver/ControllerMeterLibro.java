package com.example.librosserver;

import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.*;
import java.net.Socket;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ControllerMeterLibro {

    @FXML
    private MFXButton btnEnviar;

    @FXML
    private MFXTextField introducirAutor;

    @FXML
    private MFXTextField introducirAño;
    @FXML
    private TextArea desc;

    @FXML
    private MFXTextField introducirIsbn;

    @FXML
    private MFXTextField introducirNombre;

    @FXML
    private Label labelAutor;

    @FXML
    private Label labelAño;

    @FXML
    private Label labelIsbn;
    @FXML
    private Label infoAutor;
    @FXML
    private ImageView meterImagen;

    @FXML
    private Label infoAño;

    @FXML
    private Label infoISBN;

    @FXML
    private Label infoNombre;
    private String imagenSeleccionada;
    private ObjectInputStream in;
    private ObjectOutputStream out;

    @FXML
    private Label labelNombre;
    @FXML
    private Label infoImagen;
    private Socket socket;
    private Data data;
    Map<String, String> columnasExpresiones = new HashMap<String, String>() {
        {
            put("Nombre", "^[\\w\\d\\s,.'\":;?!-]+$");
            put("Autor", "^[a-zA-ZáéíóúÁÉÍÓÚñÑüÜ' -]+$");
        }

    };
    /**
     * Método que se encarga de añadir un nuevo libro, comprueba los valores con expresiones
     * regulares, si hay algun fallo, el libro no se crea e indica el contenido erroneo
     * */
    @FXML
    void enviar(MouseEvent event) {
        this.socket = data.getSocket();
        boolean error = false;

        if(!validarContenido(this.columnasExpresiones.get("Nombre"),this.introducirNombre.getText())){
            error = true;
            this.introducirNombre.setText("");
        }
        if(!validarContenido(this.columnasExpresiones.get("Autor"),this.introducirAutor.getText())){
            error = true;
            this.introducirAutor.setText("");
        }
        if(this.imagenSeleccionada == null){
            error = true;
        }
        if (this.desc.getText().isEmpty()){
            error = true;
        }
        if (error){
            return;
        }

        Libro libro = new Libro(this.introducirNombre.getText(),this.introducirAutor.getText(),imagenSeleccionada,this.desc.getText());

        /* Enviar libro a servidor cifrando */

        try {
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
            byte[] operacion = encryptor.encrypt("crear",data.getLlaveServer());
            System.out.println("envio tamaño");
            out.writeInt(operacion.length);
            out.flush();
            System.out.println("envio operacion");
            out.write(operacion);
            out.flush();

            System.out.println("enviando libro");
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            oos.writeObject(libro);
            oos.close();
            byte[] libroBytes = baos.toByteArray();
            Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.ENCRYPT_MODE, this.data.getLlaves().getPrivate());
            byte[] libroCifrado = cipher.doFinal(libroBytes);
            out.writeInt(libroCifrado.length);
            out.flush();
            out.write(libroCifrado);
            out.flush();




        }catch (IOException err){
            System.out.println(err.getMessage());
        } catch (NoSuchPaddingException | IllegalBlockSizeException | BadPaddingException | NoSuchAlgorithmException |
                 InvalidKeyException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }


        this.meterImagen.setImage(new Image(getClass().getResourceAsStream("/images/imagenPlaceholder/preview.png")));
        this.introducirAutor.setText("");
        this.introducirNombre.setText("");
        this.desc.setText("");
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

    @FXML
    void meterImagen(MouseEvent event) {
        FileChooser filechooser = new FileChooser();

        filechooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("image files", "*.png","*.jpg","*.jpeg"));
        File selectedFile = filechooser.showOpenDialog(null);


        if(selectedFile != null){
            String imagePath = selectedFile.getAbsolutePath();
            this.imagenSeleccionada = imagePath;
            System.out.println(imagenSeleccionada);
            this.meterImagen.setImage(new Image("file:"+imagenSeleccionada));
        }else {
            if(this.imagenSeleccionada != null){
                this.meterImagen.setImage(new Image("file:"+imagenSeleccionada));
            }else {
                this.meterImagen.setImage(new Image(getClass().getResourceAsStream("/images/imagenPlaceholder/preview.png")));
            }

        }
    }
    public void recibirDatos(Data data){
        this.data = data;
    }

}

