package multiplayer;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
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
    private static final int RECEIVE_CLIENT = 1;
    private static final int RECEIVE_SERVER = 2;
    private Socket socket = null;
    private MediProtocol mediProtocol;
    private int clientIndex;

    public MediServerThread(Socket socket, MediProtocol mediProtocol, int clientIndex) {
        super("MediServerThread "+clientIndex);
        System.out.println("******* Client Connecting... ******");
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
                System.out.println("THREAD "+clientIndex+" - ActiveClient set to: "+activeClient);
                int state;
                
                if (activeClient == clientIndex){
                    state = RECEIVE_CLIENT;
                    out.println(DataPacketHandler.createStatusUpdatePackage(DataPacketHandler.STATUS_PLAY));
                }
                else {
                    state = RECEIVE_SERVER;
                }

                switch (state) {
                    case RECEIVE_SERVER:
                        System.out.println("THREAD "+ clientIndex + ": RECEIVE_SERVER");
                        outputLine = mediProtocol.getMessage(); //Waits for the server to give a message to client
                        out.println(outputLine); //Send the message to the client
                        try {
                            this.sleep(100);
                        } catch (InterruptedException e) {}
                        break;
                    case RECEIVE_CLIENT:
                        System.out.println("THREAD "+ clientIndex + ": RECEIVE_CLIENT");
                        inputLine = in.readLine(); //Read request from client.
                        mediProtocol.handleClientRequest(inputLine); //Give the protocol the request to handle it
                        try {
                            this.sleep(100);
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
