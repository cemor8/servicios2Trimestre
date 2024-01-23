import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class MainCliente {
    public static void main(String[] args) throws IOException {
        Integer opcion = 0;
        ArrayList<Cliente> clientes = new ArrayList<>();
        while (opcion == 0){
            System.out.println("Introduce un numero de host entre 2 y 10");
            Scanner scanner = new Scanner(System.in);
            try {
                opcion = scanner.nextInt();
                if(opcion <=1 ||opcion > 10){
                    opcion = 0;
                    continue;
                }
            }catch (Exception err){
                System.out.println(err.getMessage());
            }

            for (int i = 0; i<opcion;i++){
                Cliente cliente = new Cliente(i+1,opcion);
                clientes.add(cliente);
            }
            for (Cliente cliente : clientes){
                cliente.start();
            }
        }
    }
}
