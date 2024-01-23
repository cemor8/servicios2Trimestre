package cliente;

import java.io.*;
import java.net.Socket;

public class Cliente {
    //host del server
    private String host = "localhost";
    //puerto del server
    private int puerto = 5000;
    Socket socket;
    DataInputStream in;
    DataOutputStream out;
    String ubicacionArchivo = "./archivo/texto.txt";

    public Cliente() throws IOException {
        //conectar al server
        socket = new Socket(host,puerto);

    }
    /**
     * Método que se encarga de iniciar el cliente, lee un archivo y manda su
     * contenido al servidor.
     * */
    public void  iniciarCliente() throws IOException {
        try {
            FileInputStream fileInputStream = new FileInputStream(ubicacionArchivo);

            BufferedInputStream bufferedInputStream = new BufferedInputStream(fileInputStream);

            byte[] buffer = new byte[4096];
            int bytesRead;

            in=new DataInputStream(socket.getInputStream());

            out = new DataOutputStream(socket.getOutputStream());

            System.out.println("leyendo archivo");

            while ((bytesRead = bufferedInputStream.read(buffer)) != -1) {
                out.write(buffer, 0, bytesRead);
            }

        }catch (Exception err){
            System.out.println(err.getMessage());

        }
        socket.close();
    }
}
