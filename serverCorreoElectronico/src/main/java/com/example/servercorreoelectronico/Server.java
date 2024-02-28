package com.example.servercorreoelectronico;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;

public class Server {
    private int puerto = 5000;

    private ServerSocket serverSocket;
    private boolean funcionando = true;
    private ArrayList<Correo> correos = new ArrayList<>();
    private HashMap<String, Socket> direcciones = new HashMap<>();

    public Server() throws IOException {
        serverSocket = new ServerSocket(puerto);
    }
    public static void main(String[] args) throws IOException {
        Server server = new Server();
        server.iniciar();
    }

    public void iniciar() throws IOException {
        while (funcionando) {
            System.out.println("Esperando cliente");
            // Cliente se conecta
            Socket clientSocket = serverSocket.accept();
            System.out.println("Cliente conectado");

            ClientHandler clientHandler = new ClientHandler(clientSocket, correos, direcciones);
            clientHandler.start();
        }
    }
}
