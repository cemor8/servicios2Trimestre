package com.example.servercorreoelectronico;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class ClientHandler extends Thread {
    private Socket socket;
    private ObjectInputStream in;
    private ObjectOutputStream out;
    private ArrayList<Correo> correos; // Asegúrate de pasar esta lista al constructor si es necesario
    private HashMap<String, Socket> direcciones;

    public ClientHandler(Socket socket, ArrayList<Correo> correos, HashMap<String, Socket> direcciones) {
        this.socket = socket;
        this.correos = correos;
        this.direcciones = direcciones;
    }

    /**
     * Método que se encarga de gestionar las conexiones con cada cliente del servidor de correo
     */
    @Override
    public void run() {
        while (true) {
            try {
                in = new ObjectInputStream(socket.getInputStream());
                out = new ObjectOutputStream(socket.getOutputStream());
                Correo correo = (Correo) in.readObject();

                if (correo.getDestinatario().trim().equalsIgnoreCase("server")) {

                    if (!this.direcciones.containsKey(correo.getRemitente())) {
                        this.direcciones.put(correo.getRemitente(), socket);
                    }

                    for (Correo cadaCorreo : this.correos) {
                        if (cadaCorreo.getRemitente().equalsIgnoreCase(correo.getRemitente()) || cadaCorreo.getDestinatario().equalsIgnoreCase(correo.getRemitente())) {
                            out.writeObject(cadaCorreo);
                        }
                    }
                    out.writeObject(null);
                } else {
                    if (!this.direcciones.containsKey(correo.getDestinatario())) {

                        out.writeObject(null);
                        continue;
                    }

                    if (correo.getRemitente().equalsIgnoreCase(correo.getDestinatario())){

                        this.correos.add(correo);
                        out.writeObject(correo);
                    }else{
                        this.correos.add(correo);
                        out.writeObject(correo);
                        new ObjectOutputStream(this.direcciones.get(correo.getDestinatario()).getOutputStream()).writeObject(correo);
                    }


                }


            } catch (IOException | ClassNotFoundException e) {
                System.out.println("Error al manejar al cliente: " + e.getMessage());
            }
        }
    }
}
