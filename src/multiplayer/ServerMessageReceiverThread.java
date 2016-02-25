package multiplayer;

import events.ServerMessageListener;

import java.io.BufferedReader;
import java.io.IOException;

/**
 * This is a thread that listens to a client. A new thread is created for each client that connects
 * Created by Tobias on 2016-02-23.
 */
public class ServerMessageReceiverThread extends Thread {

    int clientIndex;
    ServerMessageListener serverMessageListener;
    BufferedReader serverIn;

    public ServerMessageReceiverThread(BufferedReader in, ServerMessageListener sml, int clientIndex) {
        this.serverIn = in;
        serverMessageListener = sml;
        this.clientIndex = clientIndex;
    }

    @Override
    public void run() {

        String message;

        try {
            while ((message = serverIn.readLine()) != null) {
                serverMessageListener.receivedMessage(message, clientIndex);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
