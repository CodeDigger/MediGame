package multiplayer;

import java.net.*;
import java.io.*;

/**
 * The server
 * Created by Tobias on 2015-07-30.
 */
public class Server extends Thread {

    int portNumber;
    int numClients;
    boolean listening = true;
    ServerProtocol serverProtocol;
    int clientIndex;

    public Server(int portNumber, int numClients) {
        super("Server");
        this.portNumber = portNumber;
        this.numClients = numClients;
        clientIndex = 0;
        serverProtocol = new ServerProtocol();
    }
    
    public void run() {
        
        try {
            ServerSocket serverSocket = new ServerSocket(portNumber);
//            ServerSocket serverSocket = new ServerSocket(portNumber, 0, InetAddress.getByName("localhost")); //Use for only localhost
            System.out.println("SERVER: The server is running on port: " + portNumber);
            while (listening) {
                Socket socket = serverSocket.accept();
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                serverProtocol.newClientConnected(out, clientIndex);
                new ServerMessageReceiverThread(in, serverProtocol, clientIndex++).start();
                System.out.println("SERVER: Client connected! The number of clients is: " + clientIndex);
                System.out.println("***********************************");
            }
        }
        catch (IOException e) {
            System.out.println("Exception caught when trying to listen on port "
                    + portNumber + " or listening for a connection");
            System.out.println(e.getMessage());
        }
        
    }

}
