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
    private StringBuilder pregunta = new StringBuilder();

    public Servidor() throws IOException {
        serverSocket = new ServerSocket(puerto);
        socket = new Socket();
    }
    /**
     * Método que se encarga de crear el servidor, escuchara peticiones de preguntas,
     * las lee carcter a caracter hasta encontrar el caracter ?, cuando acabe, comprueba
     * la pregunta y la responde si sabe la respuesta
     * */
    public void iniciar() throws IOException {
        while (funcionando) {

            System.out.println("Esperando cliente");
            //cliente se conecta
            socket = serverSocket.accept();
            System.out.println("cliente connectado");

            //recibe datos
            //offset
            in = new DataInputStream(socket.getInputStream());
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(in,"UTF-8"));

            int caracter;
            while ((caracter = bufferedReader.read()) != -1) {
                char letra = (char) caracter;
                pregunta.append(letra);
                System.out.println(letra);
                if(String.valueOf(letra).equalsIgnoreCase("?")){
                    break;
                }
            }
            out = new DataOutputStream(socket.getOutputStream());
            System.out.println(pregunta);

            if(String.valueOf(pregunta).equalsIgnoreCase("¿Cómo te llamas?")){
                out.writeUTF("Me llamo Ejercicio 2");
            }else if(String.valueOf(pregunta).equalsIgnoreCase("¿Cuántas líneas de código tienes?")){
                out.writeUTF("Tengo 61 líneas de código");
            }else {
                out.writeUTF("No he entendido la pregunta");
            }
            pregunta = new StringBuilder();
            socket.close();
        }
    }
}