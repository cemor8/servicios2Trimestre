package servidor;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Servidor {
    private int puerto = 5000;
    private DataInputStream in;
    private DataOutputStream out;
    private ServerSocket serverSocket;
    private Socket socket;
    private boolean funcionando = true;

    public Servidor() throws IOException {
        serverSocket = new ServerSocket(puerto);
        socket = new Socket();
    }
    public void iniciar() throws IOException {
        while (funcionando){

            System.out.println("Esperando cliente");
            //cliente se conecta
            socket = serverSocket.accept();
            System.out.println("cliente connectado");

            //recibe datos
            in = new DataInputStream(socket.getInputStream());
            //envia datos
            out = new DataOutputStream(socket.getOutputStream());
            // leer mensaje cliente
            String mensaje = in.readUTF();
            while (!mensaje.equalsIgnoreCase("fin")){
                System.out.println(mensaje);
                mensaje = in.readUTF();
            }
            // mandamos mensaje
            out.writeUTF("Hola cliente desde server");
            socket.close();
        }
    }
}
