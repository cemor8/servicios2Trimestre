import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class Cliente {
    //host del server
    private int puertoServer = 4321;
    //almacena paquetes
    byte[] buffer = new byte[25];
    InetAddress direccionServer;
    DatagramSocket socketUdp;


    public Cliente() throws IOException {

       direccionServer = InetAddress.getByName("localhost");
       socketUdp = new DatagramSocket();

    }
    public void  iniciarCliente() throws IOException {
        // mandar paquete a server
        String mensaje = "token";
        buffer = mensaje.getBytes();
        DatagramPacket pregunta = new DatagramPacket(buffer, buffer.length,direccionServer,puertoServer);
        socketUdp.send(pregunta);

        //recibir info
        System.out.println("Esperando respuesta del server");
        DatagramPacket peticion = new DatagramPacket(buffer, buffer.length);
        //cambiar tamaño de buffer
        buffer = peticion.getData().length;
        socketUdp.receive(peticion);
        mensaje = new String(peticion.getData(),0, peticion.getLength(), StandardCharsets.UTF_8);
        System.out.println(mensaje);
        socketUdp.close();

    }
}
