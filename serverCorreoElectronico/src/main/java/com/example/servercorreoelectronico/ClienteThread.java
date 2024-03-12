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

    /**
     * Método que se encarga de recibir actualizaciones de correos nuevos para cargarlos en la vista
     */
    @Override
    public void run(){
        while (activado){

            try {
                in = new ObjectInputStream(socket.getInputStream());
            } catch (IOException e) {

                System.out.println(e.getMessage());
                continue;
            }
            Correo correoRecibido;

            try {
                correoRecibido = (Correo) in.readObject();
            } catch (IOException e) {
                System.out.println(e.getMessage());
                continue;
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
            if(correoRecibido == null){

                this.cliente.modificar();
                continue;
            }

            this.cliente.recibirMensaje(correoRecibido);


        }

    }
}
