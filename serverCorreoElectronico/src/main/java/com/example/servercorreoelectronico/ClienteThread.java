package com.example.servercorreoelectronico;

import java.io.*;
import java.net.Socket;

public class ClienteThread extends Thread{
    ObjectInputStream in;
    ObjectOutputStream out;
    Socket socket;
    private int puerto = 5000;
    private String host = "localhost";
    private String correo;
    private Boolean activado = true;
    ControllerCliente cliente;

    public ClienteThread(Socket socket, ControllerCliente cliente) {
        this.socket = socket;
        this.cliente = cliente;
    }
    @Override
    public void run(){
        while (activado){
            System.out.println("escuchando");
            try {
                in = new ObjectInputStream(socket.getInputStream());
            } catch (IOException e) {
                System.out.println("error");
                System.out.println(e.getMessage());
                continue;
            }
            Correo correoRecibido;
            System.out.println("Mensaje recibido");
            try {
                correoRecibido = (Correo) in.readObject();
            } catch (IOException e) {
                System.out.println(e.getMessage());
                continue;
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
            if(correoRecibido == null){
                System.out.println("Correo inválido");
                this.cliente.modificar();
                continue;
            }
            System.out.println("recibiendo");
            this.cliente.recibirMensaje(correoRecibido);


        }

    }
}
