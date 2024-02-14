package com.example.servermail;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;

public class Server {
    private int puerto = 5000;
    private DataInputStream in;
    private DataOutputStream out;
    private ServerSocket serverSocket;
    private Socket socket;
    private boolean funcionando = true;
    private ArrayList<Correo> correos = new ArrayList<>();
    private HashMap<String, Socket> direcciones = new HashMap<>();

    public Server() throws IOException {
        serverSocket = new ServerSocket(puerto);
        socket = new Socket();
    }
    public static void main(String[] args) throws IOException {
        Server server = new Server();
        server.iniciar();
    }

    public void iniciar() throws IOException {
        while (funcionando) {
            System.out.println("Esperando cliente");
            // Cliente se conecta
            Socket clientSocket = serverSocket.accept(); // Usa una variable local para el socket del cliente
            System.out.println("Cliente conectado");

            // Crea un nuevo hilo para manejar al cliente
            ClientHandler clientHandler = new ClientHandler(clientSocket, correos, direcciones);
            clientHandler.start();
        }
    }
}
