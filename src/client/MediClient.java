package client;

import server.DataPacketHandler;

import java.io.*;
import java.net.*;

/**
 * Created by Tobias on 2015-07-30.
 * Medi client class
 */
public class MediClient extends Thread {
    private static final int WAIT = 0;
    private static final int PLAY = 1;
    private static final int UPDATE_BOARD = 2;

    String hostName;
    int portNumber;

    public MediClient(String ip, int port) {

        hostName = ip;
        portNumber = port;
    }

    @Override
    public void run() {

        int state = WAIT;

        try (
                Socket socket = new Socket(hostName, portNumber);
                PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                BufferedReader in = new BufferedReader(
                        new InputStreamReader(socket.getInputStream()))
        ) {
            BufferedReader stdIn
                    = new BufferedReader(new InputStreamReader(System.in));
            String fromServerString = "0";
            String fromUserString = "0";

            while (true) {
                switch (state) {
                    case WAIT:
                        System.out.println("CLIENT - Current state is WAIT");
                        fromServerString = in.readLine();
                        System.out.println("CLIENT - From server: " + fromServerString);
                        //TODO Handle the server message. For now the server sets the state
                        state = DataPacketHandler.handlePacket(fromServerString);
                        break;
                    case PLAY:
                        System.out.println("CLIENT - Current state is PLAY");
                        fromUserString = stdIn.readLine();
                        if (fromUserString != null) {
                            out.println(fromUserString);
                        } else {
                            fromUserString = "DUMDUM";
                            out.println(fromUserString);
                        }
                        state = WAIT;
                        break;
                    case UPDATE_BOARD:
                        //TODO Update the board
                        state = WAIT;
                        break;
                    default:
                        break;
                }
            }

        } catch (UnknownHostException e) {
            System.err.println("Don't know about host " + hostName);
            System.exit(1);
        } catch (IOException e) {
            System.err.println("Couldn't get I/O for the connection to "
                    + hostName);
            e.printStackTrace();
            System.exit(1);
        }
    }
}
