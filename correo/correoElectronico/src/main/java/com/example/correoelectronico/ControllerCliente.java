package com.example.correoelectronico;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;

public class ControllerCliente {
    private String correo;
    private ClienteThread hilo;
    private String host = "localhost";
    //puerto del server
    private int puerto = 5000;
    Socket socket;
    DataInputStream in;
    DataOutputStream out;
    private ArrayList<Correo> correosRecibidos = new ArrayList<>();
    private ArrayList<Correo> correosEnviados = new ArrayList<>();
    public void recibirCorreo(String mensaje) throws IOException {
        this.correo = mensaje;
        socket = new Socket(host,puerto);

        out = new DataOutputStream(socket.getOutputStream());
        out.writeUTF("LOGIN:"+this.correo);

        in=new DataInputStream(socket.getInputStream());
        String correoRecibido = in.readUTF();
        while (!correoRecibido.equalsIgnoreCase("fin")){
            String[] partes = mensaje.split(":");
            switch (partes[0]) {
                case "ENVIADO":
                    this.correosEnviados.add(new Correo(partes[1],partes[2],partes[3]));
                    break;
                case "RECIBIDO":
                    this.correosRecibidos.add(new Correo(partes[1],partes[2],partes[3]));
                    break;
                default:
                    break;
            }
            correoRecibido = in.readUTF();
        }

    }
    public void cargarEnviados(){
        if(this.correosEnviados.isEmpty()){
            return;
        }
        for(Correo correo : this.correosEnviados){

        }
    }
    public void cargarRecibidos(){
        if(this.correosRecibidos.isEmpty()){
            return;
        }
        for(Correo correo : this.correosRecibidos){

        }
    }

}
