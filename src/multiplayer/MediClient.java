package multiplayer;

import mapBuilder.ClientPlayer;
import java.awt.Dimension;
import java.io.*;
import java.net.*;
import mapBuilder.ClientMapHandler;
import mapBuilder.ClientMapPanel;
import tiles.Tile;
import tiles.TileHandler;

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
    ClientMapPanel mapPanel;
    ClientMapHandler mapHandler;
    
    ClientPlayer player;
    
    Dimension mapDim;
    Dimension menuDim;
    boolean gameRunning = false;
    

    public MediClient(String ip, int port, ClientMapPanel mapPanel) {
        hostName = ip;
        portNumber = port;
        this.mapPanel = mapPanel;
        this.mapHandler = mapPanel.getMapHandler();
        
        
        player = new ClientPlayer("Player Name", new Dimension(800,600));
        mapPanel.setUI(player.getUI());
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
                        switch (packet[DataPacketHandler.SUBPACKET_PACKETTYPE]) {
                            case DataPacketHandler.PACKETTYPE_STATUSUPDATE:
                                state = packet[DataPacketHandler.SUBPACKET_STATUS];
                                break;
                            case DataPacketHandler.PACKETTYPE_TILEPLACEMENT:
                                int row = packet[DataPacketHandler.SUBPACKET_TILETOPLACE_ROW];
                                int col = packet[DataPacketHandler.SUBPACKET_TILETOPLACE_COL];
                                int type = packet[DataPacketHandler.SUBPACKET_TILETOPLACE_TYPE];
                                int alignment = packet[DataPacketHandler.SUBPACKET_TILETOPLACE_ALIGNMENT];
                                mapHandler.placeLandFromServer(row, col, type, alignment);
                                break;
                            default:
                                // Do nothing (?)
                                break;
                        }
                        
                        break;
                    case PLAY:
                        System.out.println("CLIENT - Current state is PLAY");
                        
                        waitForDraw();
                        // Request tile from server:
                        out.println(DataPacketHandler.createTileRequestPackage());
                        // Get tile from server:
                        fromServerString = in.readLine();
                        int[] tileTypeData = DataPacketHandler.handlePacket(fromServerString);
                        Tile tileToPlay = TileHandler.initLand(tileTypeData[DataPacketHandler.SUBPACKET_TILETYPETOPLAY]);
                        // Stuff:
                        player.giveTile(tileToPlay);
                        waitForPlay();
                        // Send tile placement package to server! :D
                        out.println(DataPacketHandler.createTilePlacementPackage(tileToPlay.getRow(),
                                tileToPlay.getCol(), tileToPlay.getType(), tileToPlay.getAlignment()));
                        
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
    
    private void waitForDraw() {
        while(mapPanel.getTileRequest()) {
            //Wait
        }
    }
    
    private void waitForPlay() {
        mapPanel.permissionToPlay();
        while(mapPanel.isPlaying()) {
            //Wait
        }
    }

}
