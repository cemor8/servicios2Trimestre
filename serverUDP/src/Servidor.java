import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.*;
import java.nio.charset.StandardCharsets;

public class Servidor {
    private int puerto = 4321;
    //almacena paquetes
    byte[] buffer = new byte[25];
    //tiene info de ip y server

    private DatagramSocket socketUDP;
    //almacena info
    private DatagramPacket paquete;
    boolean bandera = true;


    public Servidor() throws IOException {
        socketUDP = new DatagramSocket(puerto);
        paquete = new DatagramPacket(buffer, buffer.length);
    }
    public void iniciar() throws IOException {
        while (bandera){
            //recibimos el paquete
            System.out.println("Esperando cliente");
            socketUDP.receive(paquete);
            System.out.println("paquete de cliente recibido");
            String mensaje = new String(paquete.getData(),0, paquete.getLength(), StandardCharsets.UTF_8);
            System.out.println(mensaje);
            //obtenemos puesrot de cluebte
            int puertoCliente = paquete.getPort();
            // direccion cliente
            InetAddress direccion = paquete.getAddress();

            //creamos respuesta
            System.out.println("Enviando respuesta a cliente");

            String mensaje2 = "recibido";
            buffer = mensaje2.getBytes();
            DatagramPacket respuesta = new DatagramPacket(buffer, buffer.length,direccion,puertoCliente);
            //decodificar respuesta
            String mostrar = new String(respuesta.getData(),0, respuesta.getLength(),StandardCharsets.UTF_8);
            System.out.println(mostrar);
            socketUDP.send(respuesta);
            bandera=false;

        }

    }
}
