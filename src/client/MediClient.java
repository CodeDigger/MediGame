package client;

import java.io.*;
import java.net.*;

/**
 * Created by Tobias on 2015-07-30.
 * Medi client class
 */
public class MediClient extends Thread {
    private static final int WAIT = 1;
    private static final int WRITE = 2;
    
    String hostName;
    int portNumber;
    
    public MediClient(String ip, int port) {

        hostName = ip;
        portNumber = port;
        this.start();
    }
    
    @Override
    public void run() {
        
        String clientIndex;
        int state = WAIT;
        
        try (
                Socket socket = new Socket(hostName, portNumber);
                PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                BufferedReader in = new BufferedReader(
                        new InputStreamReader(socket.getInputStream()));) {
            BufferedReader stdIn
                    = new BufferedReader(new InputStreamReader(System.in));
            String fromServer;
            String fromUser = "0";

            fromServer = in.readLine();
            System.out.println("You are client number: " + fromServer);
            clientIndex = fromServer;

            if (clientIndex.equals("1")) {
                state = WRITE;
            } else if (clientIndex.equals("2")) {
                state = WAIT;
            }

            while (true) {
                switch (state) {
                    case WAIT:
                        //while ((fromServer = in.readLine()) != null) {
                        fromServer = in.readLine();
                        System.out.println("Other guy: " + fromServer);
                        if (fromServer.equals("Bye.")) {
                            break;
                        }
                        //}
                        state = WRITE;
                        break;
                    case WRITE:
                        fromUser = stdIn.readLine();
                        if (fromUser != null) {
                            System.out.println("You: " + fromUser);
                            out.println(fromUser);
                        } else {
                            fromUser = "DUMDUM";
                            out.println(fromUser);
                        }
                        state = WAIT;
                        break;
                    default:
                        break;
                }
                if (fromServer.equals("Bye.") || fromUser.equals("Bye.")) {
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
