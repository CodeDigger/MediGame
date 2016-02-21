package multiplayer;

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
    private static final int CLIENT_PLAYING = 1;
    private static final int CLIENT_UPDATE = 2;
    private Socket socket = null;
    private MediProtocol mediProtocol;
    private int clientIndex;
    private boolean clientConnected = false;

    public MediServerThread(Socket socket, MediProtocol mediProtocol, int clientIndex) {
        super("MediServerThread "+clientIndex);
        System.out.println("******* Client Connecting... ******");
        this.socket = socket;
        this.mediProtocol = mediProtocol;
        this.clientIndex = clientIndex;
        clientConnected = true;
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
                    state = CLIENT_PLAYING;
                    out.println(DataPacketHandler.createStatusUpdatePackage(DataPacketHandler.STATUS_PLAY));
                }
                else {
                    state = CLIENT_UPDATE;
                }

                switch (state) {
                    case CLIENT_UPDATE:
                        System.out.println("THREAD "+ clientIndex + ": RECEIVE_SERVER");
                        outputLine = mediProtocol.getMessage(); //Waits for the server to give a message to client
                        out.println(outputLine); //Send the message to the client
                        try {
                            Thread.sleep(10);
                        } catch (InterruptedException e) {}
                        break;
                    case CLIENT_PLAYING:
                        System.out.println("THREAD "+ clientIndex + ": RECEIVE_CLIENT");
                        inputLine = in.readLine(); //Read tile request from client.
                        String tilePacket = mediProtocol.handleClientTileRequest(inputLine); //Give the protocol the request to handle it
                        if (tilePacket.equals("QUIT")){
                            clientDisconnected();
                            break;
                        }
                        out.println(tilePacket);
                        inputLine = in.readLine(); //Read tile placement from client.
                        String clientStatus = mediProtocol.handleClientTilePlacement(inputLine);
                        if (clientStatus.equals("QUIT")){
                            clientDisconnected();
                            break;
                        }
                        try {
                            Thread.sleep(10);
                        } catch (InterruptedException e) {}
                        break;
                    default:
                        break;
                }
                if (!clientConnected){
                    System.out.println("THREAD "+ clientIndex + ": CLIENT DISCONNECTED");
                    break;
                }
            }
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void clientDisconnected() {
        clientConnected = false;
        mediProtocol.clientDisconected();
    }
}
