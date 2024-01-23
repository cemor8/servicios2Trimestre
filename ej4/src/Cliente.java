import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.PortUnreachableException;

public class Cliente extends Thread{
    //host del server
    private int puertoEnviar = 4321;
    //almacena paquetes
    byte[] buffer = new byte[5];
    InetAddress direccionServer;
    DatagramSocket socketUdp;
    private Integer posicion;
    private int cantidadClientes;


    public Cliente(Integer posicion,Integer cantidadClientes) throws IOException {
        this.posicion = posicion;
        this.cantidadClientes = cantidadClientes;
        direccionServer = InetAddress.getByName("localhost");
        int puertoLocal = puertoEnviar + posicion;
        socketUdp = new DatagramSocket(puertoLocal);

    }
    @Override
    public void run(){
        try {
            this.iniciarCliente();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    /**
     * Método que se encarga de iniciar un cliente, busca el puerto de destino comprobando
     * la posicion actual del cliente en el anillo, si este es el primero se encarga de enviar el token y luego
     * esperar a que se lo reenvien, en cambio si este no es el primero, se encarga de esperar su turno y luego reenviar
     * el token al siguiente cliente, una vez el token llegue al primero, se interrumpe la secuencia
     * */
    public void iniciarCliente() throws IOException {
        int siguientePosicion = (posicion % cantidadClientes) + 1;
        int puertoDestino = puertoEnviar + siguientePosicion;

        if (posicion == 1) {

            System.out.println("Cliente " + posicion + ": envía token");
            String mensaje = "token";
            buffer = mensaje.getBytes();
            DatagramPacket pregunta = new DatagramPacket(buffer, buffer.length, direccionServer, puertoDestino);
            socketUdp.send(pregunta);
        }


        System.out.println("Cliente " + posicion + ": esperando respuesta");
        DatagramPacket peticion = new DatagramPacket(buffer, buffer.length);
        socketUdp.receive(peticion);

        String mensaje = new String(peticion.getData(), 0, peticion.getLength()).trim();
        System.out.println("Cliente " + posicion + ": recibe "+mensaje);
        if (posicion != 1 || !mensaje.equalsIgnoreCase("token")) {



            System.out.println("Cliente: " + posicion + ": reenvía token");
            peticion.setPort(puertoDestino);
            socketUdp.send(peticion);
        }
        socketUdp.close();
    }
}
