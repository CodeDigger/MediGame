
import java.net.*;
import java.io.*;
import java.util.HashSet;

/**
 * The server
 * Created by Tobias on 2015-07-30.
 */
public class MediServer {

    /**
     * The names of the clients.
     */
    private static HashSet<String> names = new HashSet<>();

    /**
     * The print writers for all the clients.
     */
    private static HashSet<PrintWriter> writers = new HashSet<>();






    public static void main(String[] args) throws Exception {

        if (args.length != 2) {
            System.err.println("Usage: java MediServer <port number> <num clients>");
            System.exit(1);
        }


        int portNumber = Integer.parseInt(args[0]);
        int numClients = Integer.parseInt(args[1]);
        int clientIndex = 1;
        boolean listening = true;
        MediProtocol mediProtocol = new MediProtocol();

        System.out.println("The server is running on port: " + portNumber);

        try {
            ServerSocket serverSocket = new ServerSocket(portNumber);
            while (listening) {
                new MediServerThread(serverSocket.accept(), mediProtocol, clientIndex++).start();
                System.out.println("Client connected! The number of clients is: " + clientIndex);
            }


        }
        catch (IOException e) {
            System.out.println("Exception caught when trying to listen on port "
                    + portNumber + " or listening for a connection");
            System.out.println(e.getMessage());
        }
    }
}
