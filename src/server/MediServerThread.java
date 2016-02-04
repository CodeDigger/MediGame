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
    }

    public void run() {
        try (
                PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                BufferedReader in = new BufferedReader(
                        new InputStreamReader(
                                socket.getInputStream()));
        ) {
            String inputLine = "0";
            String outputLine;
            outputLine = Integer.toString(clientIndex);
            out.println(outputLine);


            while (true) {

                int activeClient = mediProtocol.getActiveClient();
                int state;
                
                if (activeClient == clientIndex){
                    state = RECEIVE;
                }
                else {
                    state = TRANSMIT;
                }

                switch (state) {
                    case TRANSMIT:
                        outputLine = mediProtocol.getMessage();
                        out.println(outputLine);
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {}
                        break;
                    case RECEIVE:
                        inputLine = in.readLine();
                        mediProtocol.setMessage(inputLine);
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {}
                        break;
                    default:
                        break;
                }

                if (outputLine.equals("Bye.") || inputLine.equals("Bye."))
                    break;
            }

            /*while ((inputLine = in.readLine()) != null) {
                if(clientIndex == 1){
                    outputLine = mediProtocol.client1Turn(inputLine);
                    if (outputLine != "0"){
                        out.println(outputLine);}
                    if (outputLine.equals("Bye."))
                        break;
                }
                else if (clientIndex == 2){
                    outputLine =  mediProtocol.client2Turn(inputLine);
                    if (outputLine != "0"){
                        out.println(outputLine);}
                    if (outputLine.equals("Bye."))
                        break;
                }

            }*/
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
