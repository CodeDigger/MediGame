/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package multiplayer;

import events.MessageListener;
import java.io.BufferedReader;
import java.io.IOException;

/**
 *
 * @author David
 */
public class MessageReceiverThread extends Thread {

    MessageListener messageListener;
    BufferedReader serverIn;

    public MessageReceiverThread(BufferedReader in, MessageListener sml) {
        this.serverIn = in;
        messageListener = sml;
    }

    @Override
    public void run() {

        String message;

        try {
            while ((message = serverIn.readLine()) != null) {
                    messageListener.receivedMessage(message);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
