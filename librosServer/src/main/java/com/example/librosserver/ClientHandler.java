package com.example.librosserver;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ClientHandler extends Thread {
    private Socket socket;
    private ObjectInputStream in;
    private ObjectOutputStream out;
    private ArrayList<Libro> libros; // Asegúrate de pasar esta lista al constructor si es necesario
    private HashMap<String, Socket> direcciones;
    private HashMap<String, PublicKey> llaves;
    private KeyPair llavesServer;

    public ClientHandler(Socket socket, ArrayList<Libro> libros, HashMap<String, Socket> direcciones, HashMap<String, PublicKey> llaves, KeyPair keyPair) {
        this.socket = socket;
        this.libros = libros;
        this.direcciones = direcciones;
        this.llaves = llaves;
        this.llavesServer = keyPair;
    }

    /**
     * Método que se encarga de gestionar las conexiones con cada cliente del servidor
     */
    @Override
    public void run() {
        while (true) {
            try {


                in = new ObjectInputStream(socket.getInputStream());
                out = new ObjectOutputStream(socket.getOutputStream());

                Decryptor decryptor = new Decryptor();
                Encryptor encryptor = new Encryptor();

                PublicKey llave = (PublicKey) in.readObject();

                out.writeObject(llavesServer.getPublic());
                out.flush();

                int longitud = in.readInt();


                byte[] operacionCifrada = new byte[longitud];
                in.readFully(operacionCifrada);

                String operacion = decryptor.decrypt(operacionCifrada,llavesServer.getPrivate());



                if (operacion.equalsIgnoreCase("login")){
                    longitud = in.readInt();
                    byte[] cipherText = new byte[longitud];
                    in.readFully(cipherText);
                    String nombre = decryptor.decrypt(cipherText,llavesServer.getPrivate());

                    if (this.llaves.containsKey(nombre) ||this.llaves.containsValue(llave)){
                        byte[] text = encryptor.encrypt("repetido",llave);
                        out.writeInt(text.length);
                        out.flush();
                        out.write(text);
                        out.flush();
                    }else {
                        this.llaves.put(nombre,llave);
                        this.direcciones.put(nombre,socket);
                        byte[] text = encryptor.encrypt("correcto",llave);
                        out.writeInt(text.length);
                        out.flush();
                        out.write(text);
                        out.flush();
                    }

                }else if(operacion.equalsIgnoreCase("crear")){
                    longitud = in.readInt();
                    operacionCifrada = new byte[longitud];
                    in.readFully(operacionCifrada);
                    Cipher decrypter = Cipher.getInstance("RSA");
                    decrypter.init(Cipher.DECRYPT_MODE,llave);
                    byte[] libroBytes = decrypter.doFinal(operacionCifrada);
                    ByteArrayInputStream bais = new ByteArrayInputStream(libroBytes);
                    ObjectInputStream ois = new ObjectInputStream(bais);
                    Libro libro = (Libro) ois.readObject();
                    this.libros.add(libro);



                }else if (operacion.equalsIgnoreCase("recibir")){

                    for (Libro libro : libros){

                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        ObjectOutputStream oos = new ObjectOutputStream(baos);
                        oos.writeObject(libro);
                        oos.close();
                        byte[] libroBytes = baos.toByteArray();
                        Cipher cipher = Cipher.getInstance("RSA");
                        cipher.init(Cipher.ENCRYPT_MODE, llave);
                        byte[] libroCifrado = cipher.doFinal(libroBytes);
                        out.writeInt(libroCifrado.length);
                        out.flush();
                        out.write(libroCifrado);
                        out.flush();
                    }
                    out.writeInt(0);
                    out.flush();

                }


            } catch (IOException | ClassNotFoundException e) {
                System.out.println("Error al manejar al cliente: " + e.getMessage());
                return;
            } catch (NoSuchPaddingException | InvalidKeyException | BadPaddingException | NoSuchAlgorithmException |
                     IllegalBlockSizeException e) {
                System.out.println(e.getMessage());
                throw new RuntimeException(e);
            }
        }
    }
}
