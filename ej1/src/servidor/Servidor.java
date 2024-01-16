package servidor;

import java.io.*;
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
        while (funcionando) {

            System.out.println("Esperando cliente");
            //cliente se conecta
            socket = serverSocket.accept();
            System.out.println("cliente connectado");

            //recibe datos
            in = new DataInputStream(socket.getInputStream());

            BufferedReader leer = new BufferedReader(new InputStreamReader(in));

            String linea;
            while ((linea = leer.readLine()) != null) {
                System.out.println(linea);
            }

            socket.close();
        }
    }
}
