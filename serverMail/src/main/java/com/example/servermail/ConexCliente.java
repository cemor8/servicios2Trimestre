package com.example.servermail;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class ConexCliente extends Thread {
    private Socket socket;
    private DataInputStream in;
    private DataOutputStream out;
    private ArrayList<Correo> correos; // Asegúrate de pasar esta lista al constructor si es necesario
    private HashMap<String, Socket> direcciones;

    public ConexCliente(Socket socket, ArrayList<Correo> correos, HashMap<String, Socket> direcciones) {
        this.socket = socket;
        this.correos = correos;
        this.direcciones = direcciones;
    }

    @Override
    public void run() {
        while (true) {
            try {
                in = new DataInputStream(socket.getInputStream());
                out = new DataOutputStream(socket.getOutputStream());

                String mensaje = in.readUTF();
                String[] partes = mensaje.split(":");
                System.out.println(partes[0]);
                switch (partes[0]) {
                    case "LOGIN":
                        if (!this.direcciones.containsKey(partes[1])) {
                            this.direcciones.put(partes[1], socket);
                        }
                        out = new DataOutputStream(socket.getOutputStream());
                        System.out.println(Arrays.toString(partes));
                        System.out.println(partes[1]);
                        for (Correo correo : this.correos) {
                            System.out.println("Remitente : "+correo.getRemitente());
                            System.out.println("Destinatario : "+correo.getDestinatario());
                            if (correo.getRemitente().equalsIgnoreCase(partes[1])) {
                                out.writeUTF("ENVIADO:" + correo.getRemitente() + ":" + correo.getDestinatario() + ":" +
                                        correo.getMensaje());
                            } else if (correo.getDestinatario().equalsIgnoreCase(partes[1])) {
                                out.writeUTF("RECIBIDO:" + correo.getRemitente() + ":" + correo.getDestinatario() + ":" +
                                        correo.getMensaje());
                            }

                        }
                        out.writeUTF("fin");
                        break;
                    case "ENVIAR":
                        String remitente = partes[1];
                        String destinatario = partes[2];
                        out = new DataOutputStream(socket.getOutputStream());
                        if (!this.direcciones.containsKey(destinatario)) {
                            out.writeUTF("nulo");
                            break;
                        }
                        out = new DataOutputStream(this.direcciones.get(destinatario).getOutputStream());
                        Correo correo = new Correo(remitente, destinatario, partes[3]);
                        this.correos.add(correo);
                        out.writeUTF("RECIBIDO:" + remitente + ":" + destinatario + ":" + partes[3]);
                        out = new DataOutputStream(socket.getOutputStream());
                        out.writeUTF("ENVIADO:" + remitente + ":" + destinatario + ":" + partes[3]);
                        break;
                    default:
                        System.out.println(partes[0]);
                        break;
                }

            } catch (IOException e) {
                System.out.println("Error al manejar al cliente: " + e.getMessage());
            }
        }
    }
}
