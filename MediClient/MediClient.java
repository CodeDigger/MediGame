
import java.io.*;
import java.net.*;

/**
 * Created by Tobias on 2015-07-30.
 * Medi client class
 */
public class MediClient {
    private static final int WAIT = 1;
    private static final int WRITE = 2;

    public static void main(String[] args) throws IOException {

        String clientIndex;
        int state = WAIT;


        if (args.length != 2) {
            System.err.println(
                    "Usage: java MediClient <host name> <port number>");
            System.exit(1);
        }

        String hostName = args[0];
        int portNumber = Integer.parseInt(args[1]);

        try (
                Socket socket = new Socket(hostName, portNumber);
                PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                BufferedReader in = new BufferedReader(
                        new InputStreamReader(socket.getInputStream()));
        ) {
            BufferedReader stdIn =
                    new BufferedReader(new InputStreamReader(System.in));
            String fromServer;
            String fromUser = "0";


            fromServer = in.readLine();
            System.out.println("You are client number: " + fromServer);
            clientIndex = fromServer;

            if (clientIndex.equals("1")){
                state = WRITE;
            }
            else if (clientIndex.equals("2")){
                state = WAIT;
            }

            while (true) {
                switch (state) {
                    case WAIT:
                        //System.out.println("Waiting...");
                        //while ((fromServer = in.readLine()) != null) {
                        fromServer = in.readLine();
                        System.out.println("Other guy: " + fromServer);
                        if (fromServer.equals("Bye."))
                            break;
                        //}
                        //System.out.println("Done waiting!");
                        state = WRITE;
                        break;
                    case WRITE:
                        //System.out.println("Writing...");
                        fromUser = stdIn.readLine();
                        if (fromUser != null) {
                            System.out.println("You: " + fromUser);
                            out.println(fromUser);
                        }
                        else {
                            fromUser = "DUMDUM";
                            out.println(fromUser);
                        }
                        //System.out.println("Done writing!");
                        state = WAIT;
                        break;
                    default:
                        break;
                }
                if (fromServer.equals("Bye.") || fromUser.equals("Bye."))
                    break;
            }

        } catch (UnknownHostException e) {
            System.err.println("Don't know about host " + hostName);
            System.exit(1);
        } catch (IOException e) {
            System.err.println("Couldn't get I/O for the connection to " +
                    hostName);
            System.exit(1);
        }
    }
}
