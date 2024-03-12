package com.example.librosserver;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ClienteThread extends Thread{
    ObjectInputStream in;
    ObjectOutputStream out;
    Socket socket;
    private int puerto = 5000;
    private String host = "localhost";
    private String correo;
    private Boolean activado = true;
    ControllerVistaLibro controllerVistaLibro;
    private Data data;

    public ClienteThread(Socket socket, ControllerVistaLibro cliente,Data data) {
        this.socket = socket;
        this.controllerVistaLibro = cliente;
        this.data = data;
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
            Libro libro;

            try {
                libro = (Libro) in.readObject();
            } catch (IOException e) {
                System.out.println(e.getMessage());
                continue;
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
            if (data.isViendo()){
                this.controllerVistaLibro.recibirLibro(libro);
            }



        }

    }
}
