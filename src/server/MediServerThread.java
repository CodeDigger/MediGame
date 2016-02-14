package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;



/**
 * Created by Tobias on 2016-02-01.
 * Server thread class
 */
public class MediServerThread extends Thread {
    private static final int RECEIVE = 1;
    private static final int TRANSMIT = 2;
    private Socket socket = null;
    private MediProtocol mediProtocol;
    private int clientIndex;

    public MediServerThread(Socket socket, MediProtocol mediProtocol, int clientIndex) {
        super("MediServerThread");
        this.socket = socket;
        this.mediProtocol = mediProtocol;
        this.clientIndex = clientIndex;
        System.out.println("Remote socket: " + this.socket.getRemoteSocketAddress().toString());
    }

    @Override
    public void run() {
        try (
                PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))
        ) {
            String inputLine;
            String outputLine;


            while (true) {

                int activeClient = mediProtocol.getActiveClient();
                int state;
                
                if (activeClient == clientIndex){
                    state = RECEIVE;
                    out.println(DataPacketHandler.createStatusUpdatePackage(DataPacketHandler.STATUS_PLAY));
                }
                else {
                    state = TRANSMIT;
                }

                switch (state) {
                    case TRANSMIT:
                        System.out.println("Thread " + clientIndex + " in TRANSMIT");
                        outputLine = mediProtocol.getMessageFromServer(); //Waits for the server to give a message to client
                        out.println(outputLine); //Send the message to the client
                        System.out.println("Thread: " + clientIndex + " sent: " + outputLine);
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {}
                        break;
                    case RECEIVE:
                        System.out.println("Thread in RECEIVE " + clientIndex);
                        inputLine = in.readLine(); //Read request from client.
                        System.out.println("Thread: " + clientIndex + " received: " + inputLine);
                        mediProtocol.handleClientRequest(inputLine); //Give the protocol the request to handle it
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {}
                        break;
                    default:
                        break;
                }
            }
            //socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
