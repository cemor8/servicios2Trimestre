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
    /**
     * Método que inicia el servidor, espera un cliente y recibe su paquete,
     * lo muestra y devuelve "recibido", para luego finalizar su ejecucion
     * */
    public void iniciar() throws IOException {
        while (bandera){
            //recibimos el paquete
            System.out.println("Esperando cliente");
            socketUDP.receive(paquete);
            System.out.println("paquete de cliente recibido");
            String mensaje = new String(paquete.getData(),0, paquete.getLength(), StandardCharsets.UTF_8);
            System.out.println(mensaje);
            //puerto de cliente
            int puertoCliente = paquete.getPort();
            // direccion cliente
            InetAddress direccion = paquete.getAddress();

            //creamos respuesta
            System.out.println("Enviando respuesta a cliente");

            String mensaje2 = "recibido";
            buffer = mensaje2.getBytes();
            DatagramPacket respuesta = new DatagramPacket(buffer, buffer.length,direccion,puertoCliente);
            socketUDP.send(respuesta);
            bandera=false;

        }

    }
}
