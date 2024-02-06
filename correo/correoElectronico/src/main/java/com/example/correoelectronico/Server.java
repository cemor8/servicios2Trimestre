package com.example.correoelectronico;

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

    public void iniciar() throws IOException {
        while (funcionando) {
            System.out.println("Esperando cliente");
            //cliente se conecta
            socket = serverSocket.accept();
            System.out.println("cliente connectado");

            //recibe datos
            in = new DataInputStream(socket.getInputStream());
            String mensaje = in.readUTF();
            String[] partes = mensaje.split(":");

            switch (partes[0]) {
                case "LOGIN":
                    if(!this.direcciones.containsKey(partes[1])){
                        this.direcciones.put(partes[1],socket);
                    }
                    out = new DataOutputStream(socket.getOutputStream());

                    for (Correo correo : this.correos) {
                        if (correo.getRemitente().equalsIgnoreCase(partes[1])) {
                            out.writeUTF("ENVIADO:" + correo.getRemitente() + ":" + correo.getDestinatario() + ":" +
                                    correo.getMensaje());
                        }else if(correo.getDestinatario().equalsIgnoreCase(partes[1])){
                            out.writeUTF("RECIBIDO:" +correo.getRemitente()+ ":" + correo.getDestinatario()+ ":" +
                                    correo.getMensaje());
                        }

                    }
                    out.writeUTF("fin");
                    break;
                case "ENVIAR":
                    String remitente = partes[1];
                    String destinatario = partes[2];
                    out = new DataOutputStream(socket.getOutputStream());
                    if(!this.direcciones.containsKey(destinatario)){
                        out.writeUTF("nulo");
                        break;
                    }
                    out = new DataOutputStream(this.direcciones.get(destinatario).getOutputStream());
                    Correo correo =new Correo(remitente,destinatario,partes[3]);
                    this.correos.add(correo);
                    out.writeUTF("RECIBIDO:" + remitente+":"+partes[3]);
                    break;
                default:
                    break;
            }

            socket.close();
        }
    }
}
