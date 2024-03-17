package com.example.librosserver;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Server {
    private int puerto = 5000;

    private ServerSocket serverSocket;
    private boolean funcionando = true;
    private ArrayList<Libro> libros = new ArrayList<>();
    private HashMap<String, Socket> direcciones = new HashMap<>();
    private HashMap<String, PublicKey> llaves = new HashMap<>();
    private KeyPair keyPair;

    public Server() throws IOException, NoSuchAlgorithmException {
        serverSocket = new ServerSocket(puerto);
        KeyPairGeneratorExample keyPairGenerator = new KeyPairGeneratorExample();
        KeyPair keyPair = keyPairGenerator.generateKeyPair();
        this.keyPair = keyPair;
        this.libros.add(new Libro("Test","Test","src/main/resources/images/ili.jpg","Descripcion de prueba para el libro de prueba"));
    }
    public static void main(String[] args) throws IOException, NoSuchAlgorithmException {
        Server server = new Server();
        server.iniciar();
    }

    public void iniciar() throws IOException {
        while (funcionando) {

            Socket clientSocket = serverSocket.accept();


            ClientHandler clientHandler = new ClientHandler(clientSocket, libros, direcciones,llaves,keyPair);
            clientHandler.start();
        }
    }
}
