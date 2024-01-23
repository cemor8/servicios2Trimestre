package cliente;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class Cliente {
    //host del server
    private String host = "localhost";
    //puerto del server
    private int puerto = 5000;
    Socket socket;
    DataInputStream in;
    DataOutputStream out;
    

    public Cliente() throws IOException {
        //conectar al server
        socket = new Socket(host,puerto);

    }
    /**
     * Método que hace una pregunta al servidor y espera una respuesta a esta
     * */
    public void  iniciarCliente() throws IOException {

        out = new DataOutputStream(socket.getOutputStream());
        String pregunta = "hola";
        out.write(pregunta.getBytes());
        out.flush();
        socket.shutdownOutput();

        in=new DataInputStream(socket.getInputStream());
        System.out.println(in.readUTF());

        socket.close();
    }
}
