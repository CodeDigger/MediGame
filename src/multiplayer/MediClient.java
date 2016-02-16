package multiplayer;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.io.*;
import java.net.*;
import mapBuilder.MapHandler;
import mapBuilder.MapPanel;
import mapBuilder.MenuBar;
import static menu.Main.initHeight;
import static menu.Main.initWidth;

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
    MapPanel mapPanel;
    MapHandler mapHandler;
    MenuBar menuBar;
    MultiplayerPlayer player;
    
    Dimension mapDim;
    Dimension menuDim;
    boolean gameRunning = false;
    

    public MediClient(String ip, int port) {
        hostName = ip;
        portNumber = port;
        player = new MultiplayerPlayer("Player Name");
    }

        private void setUpGame() {
        System.out.println(" - - -  SETTING UP GAME  - - - ");
        mapDim = new Dimension(initWidth, initHeight);
        menuDim = new Dimension(mapDim.width, 80);
        mapPanel = new MapPanel(mapDim);
        menuBar = new MenuBar(menuDim);
        System.out.println(" - - -  ________________  - - -");
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
                        
                        int[] packet = DataPacketHandler.handlePacket(fromServerString);
                        switch (packet[0]) {
                            case DataPacketHandler.PACKETTYPE_STATUSUPDATE:
                                //TODO Handle client status update
                                break;
                            case DataPacketHandler.PACKETTYPE_TILEDELIVERY:
                                //TODO Handle tile delivery to client
                                break;
                            case DataPacketHandler.PACKETTYPE_TILEPLACEMENT:
                                //TODO Handle players tile placement
                                break;
                            default:
                                // Do nothing (?)
                                break;
                        }
                        
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
