package server;

import java.net.*;
import java.io.*;
import java.util.HashSet;

/**
 * The server
 * Created by Tobias on 2015-07-30.
 */
public class MediServer extends Thread {

    /**
     * The names of the clients.
     */
    private static HashSet<String> names = new HashSet<>();

    /**
     * The print writers for all the clients.
     */
    private static HashSet<PrintWriter> writers = new HashSet<>();

    int portNumber;
    int numClients;
    boolean listening = true;
    MediProtocol mediProtocol;
    int clientIndex;
    
    
    public MediServer(int portNumber, int numClients) {
        this.portNumber = portNumber;
        this.numClients = numClients;
        clientIndex = 0;
        mediProtocol = new MediProtocol();
        
    }
    
    public void run() {
        
        try {
            ServerSocket serverSocket = new ServerSocket(portNumber);
            System.out.println("SERVER: The server is running on port: " + portNumber);
            while (listening) {
                new MediServerThread(serverSocket.accept(), mediProtocol, clientIndex++).start();
                mediProtocol.newClientConnected();
                System.out.println("SERVER: Client connected! The number of clients is: " + clientIndex);
            }


        }
        catch (IOException e) {
            System.out.println("Exception caught when trying to listen on port "
                    + portNumber + " or listening for a connection");
            System.out.println(e.getMessage());
        }
        
        
    }

}
