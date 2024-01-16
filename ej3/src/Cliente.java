import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

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
        System.out.println("Enviando mensaje");
        String mensaje = "Cliente : Hola serverdios";
        buffer = mensaje.getBytes();
        DatagramPacket pregunta = new DatagramPacket(buffer, buffer.length,direccionServer,puertoServer);
        socketUdp.send(pregunta);

        //recibir info
        System.out.println("Esperando respuesta del server");
        DatagramPacket peticion = new DatagramPacket(buffer, buffer.length);
        socketUdp.receive(peticion);
        mensaje = new String(peticion.getData());
        System.out.println(mensaje);
        socketUdp.close();

    }
}
