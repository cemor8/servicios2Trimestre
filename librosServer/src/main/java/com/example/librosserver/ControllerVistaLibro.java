package com.example.librosserver;

import io.github.palexdev.materialfx.controls.MFXScrollPane;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.util.ArrayList;

public class ControllerVistaLibro {
    ArrayList<Libro> librosRecorrer;
    private Data data;
    private Socket socket;
    ObjectInputStream in;
    private VBox vBox = new VBox();
    @FXML
    private MFXScrollPane meterLibros;
    ObjectOutputStream out;

    /**
     * Método que recibe datos del servidor
     * @param data
     */
    public void iniciar(Data data)  {
        try {
            this.data = data;
            this.socket = data.getSocket();
            this.librosRecorrer = new ArrayList<>();

            out = new ObjectOutputStream(socket.getOutputStream());

            out.writeObject(data.getLlaves().getPublic());
            out.flush();

            in = new ObjectInputStream(socket.getInputStream());


            PublicKey llaveServer = (PublicKey) in.readObject();

            if (llaveServer == null){
                System.out.println("error al recibir llave de server");
                return;
            }

            data.setLlaveServer(llaveServer);
            Encryptor encryptor = new Encryptor();
            byte[] operacion = encryptor.encrypt("recibir",data.getLlaveServer());

            out.writeInt(operacion.length);
            out.flush();

            out.write(operacion);
            out.flush();

            int longitud = in.readInt();

            while (longitud > 0){

                byte[] operacionCifrada = new byte[longitud];
                in.readFully(operacionCifrada);
                Cipher decrypter = Cipher.getInstance("RSA");
                decrypter.init(Cipher.DECRYPT_MODE,this.data.getLlaves().getPrivate());
                byte[] libroBytes = decrypter.doFinal(operacionCifrada);
                ByteArrayInputStream bais = new ByteArrayInputStream(libroBytes);
                ObjectInputStream ois = new ObjectInputStream(bais);
                Libro libro = (Libro) ois.readObject();
                this.librosRecorrer.add(libro);
                longitud = in.readInt();

            }


        }catch (IOException err){
            System.out.println(err.getMessage());
        } catch (ClassNotFoundException | NoSuchPaddingException | NoSuchAlgorithmException |
                 IllegalBlockSizeException | InvalidKeyException | BadPaddingException e) {
            throw new RuntimeException(e);
        }
        this.recorrer();


    }

    /**
     * Método que se encarga de mostrar los libros en la lista
     */
    public void recorrer()  {
        try {
            vBox = new VBox();
            int i = 0;
            HBox hBox = new HBox();
            for (Libro libro : librosRecorrer){
                AnchorPane anchorPane = new AnchorPane();
                anchorPane.setId(String.valueOf(librosRecorrer.indexOf(libro)));

                anchorPane.getStyleClass().add("cadaAnchor");

                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("cada_libro.fxml"));
                Parent root = fxmlLoader.load();

                ControllerCadaLibro controllerCadaLibro = fxmlLoader.getController();
                controllerCadaLibro.recibirData(this.data, libro);
                anchorPane.getChildren().setAll(root);
                HBox.setMargin(anchorPane, new Insets(0, 20, 0, 20));
                hBox.getChildren().add(anchorPane);
                VBox.setMargin(hBox, new Insets(20, 0, 0, 100));
                if ((i + 1) % 3 == 0) {
                    vBox.getChildren().add(hBox);

                    hBox = new HBox();
                }

                i++;

            }
            if (!hBox.getChildren().isEmpty()) {
                vBox.getChildren().add(hBox);
            }
            this.meterLibros.setContent(vBox);
        }catch (IOException err){
            System.out.println(err.getMessage());
        }

    }
}
