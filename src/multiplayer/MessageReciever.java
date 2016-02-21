/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package multiplayer;

import events.ServerMessageListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author David
 */
public class MessageReciever extends Thread {

    ServerMessageListener serverMessageListener;
    BufferedReader serverIn;

    public MessageReciever(BufferedReader in, ServerMessageListener sml) {
        this.serverIn = in;
        serverMessageListener = sml;
    }

    @Override
    public void run() {
        
        
        while (true) {

            try {
                String s = serverIn.readLine();
                serverMessageListener.serverMessage(s);
            } catch (IOException ex) {
                Logger.getLogger(MessageReciever.class.getName()).log(Level.SEVERE, null, ex);
            }

        }

        
    }

}
