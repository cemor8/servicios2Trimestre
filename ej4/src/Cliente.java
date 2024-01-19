import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.PortUnreachableException;

public class Cliente {
    //host del server
    private int puertoEnviar = 4321;
    //almacena paquetes
    byte[] buffer = new byte[25];
    InetAddress direccionServer;
    DatagramSocket socketUdp;
    private Integer posicion;


    public Cliente(Integer posicion) throws IOException {
        this.posicion = posicion;
       direccionServer = InetAddress.getByName("localhost");
       socketUdp = new DatagramSocket();

    }
    public void  iniciarCliente() throws IOException {
        if(posicion == 1){
            System.out.println("Cliente "+ posicion+ ": envia token");
            String mensaje = "token";
            buffer = mensaje.getBytes();
            DatagramPacket pregunta = new DatagramPacket(buffer, buffer.length,direccionServer,puertoEnviar+posicion);
            socketUdp.send(pregunta);
        }else {
            System.out.println("Esperando respuesta");
            DatagramPacket peticion = new DatagramPacket(buffer, buffer.length);
            socketUdp.receive(peticion);
            System.out.println("Cliente "+this.posicion+" recibe token");
            String mensaje = new String(peticion.getData());
            System.out.println(mensaje);
            System.out.println("Cliente "+this.posicion+" recibe token");

            if (this.posicion != 1 || !mensaje.equalsIgnoreCase("token")){

            }



            socketUdp.close();
        }

        // mandar paquete a server


        //recibir info


    }
}
