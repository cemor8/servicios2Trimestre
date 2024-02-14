package com.example.servermail;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class ClienteThread extends Thread{
    DataInputStream in;
    DataOutputStream out;
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
                in=new DataInputStream(socket.getInputStream());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            String receivedMessage = null;
            System.out.println("Mensaje recibido");
            try {
                receivedMessage = in.readUTF();
            } catch (IOException e) {
                System.out.println(e.getMessage());
                continue;
            }
            if(receivedMessage.equalsIgnoreCase("nulo")){
                System.out.println("Nulo");
                continue;
            }
            String[] partes = receivedMessage.split(":");
            String protocolo = partes[0];
            String remitente = partes[1];
            String destinatario = partes[2];
            String mensaje = partes[3];
            Correo correo = new Correo(remitente,destinatario,mensaje);
            this.cliente.recibirMensaje(correo,protocolo);

        }

    }
}
