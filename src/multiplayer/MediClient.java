package multiplayer;

import mapBuilder.ClientPlayer;
import java.awt.Dimension;
import java.io.*;
import java.net.*;
import java.util.logging.Level;
import java.util.logging.Logger;
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

    String hostName;
    int portNumber;
    ClientMapPanel mapPanel;
    ClientMapHandler mapHandler;

    ClientPlayer player;


    public MediClient(String ip, int port, ClientMapPanel mapPanel) {
        hostName = ip;
        portNumber = port;
        this.mapPanel = mapPanel;
        this.mapHandler = mapPanel.getMapHandler();


        player = new ClientPlayer("Player Name", new Dimension(800,600));
        mapPanel.setPlayer(player);
        mapPanel.setUI(player.getUI());
        mapPanel.start();
    }

    @Override
    public void run() {

        try (
                Socket socket = new Socket(hostName, portNumber);
                PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                BufferedReader in = new BufferedReader(
                        new InputStreamReader(socket.getInputStream()))
        ) {

            String fromServerString;
            int state = WAIT;

            while (true) {
                switch (state) {
                    case WAIT:
                        System.out.println("CLIENT - Current state is WAIT");
                        fromServerString = in.readLine();
                        System.out.println("CLIENT - From server: " + fromServerString);

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
                                break;
                        }
                        break;
                    case PLAY:
                        System.out.println("CLIENT - Current state is PLAY");

                        waitForDraw(); //Waiting for player to request tile from stack
                        if (player.hasLeftGame()) break; //Quit if player has left game

                        out.println(DataPacketHandler.createTileRequestPackage()); // Request tile from server:

                        fromServerString = in.readLine(); // Get tile from server:
                        int[] tileTypeData = DataPacketHandler.handlePacket(fromServerString); //Extract tile data
                        Tile tileToPlay = TileHandler.initLand
                                (tileTypeData[DataPacketHandler.SUBPACKET_TILETYPETOPLAY]); //Create a tile from tile data

                        player.giveTile(tileToPlay); //Give player the tile to play with
                        waitForPlay(); //Wait for player to make its move...
                        if (player.hasLeftGame()) break; //Quit if player has left game
                        player.takeTile(); //... and take the tile from the player
                        // Send tile placement package to server!
                        out.println(DataPacketHandler.createTilePlacementPackage(tileToPlay.getRow(),
                                tileToPlay.getCol(), tileToPlay.getType(), tileToPlay.getAlignment()));

                        state = WAIT;

                        break;
                    default:
                        break;
                }
                if (player.hasLeftGame()) {
                    out.println(DataPacketHandler.createLeaveGamePackage()); //Tell server you have left game
                    break;
                }
            }

        } catch (UnknownHostException e) {
            System.err.println("Don't know about host " + hostName);
            System.exit(1);
        } catch (IOException e) {
            System.err.println("Couldn't get I/O for the connection to "
                    + hostName);
            System.exit(1);
        }
    }

    private void waitForDraw() {
        while(mapPanel.getTileRequest()) {
            if (player.hasLeftGame()) {
                break;
            }
            try {
                //Wait
                Thread.sleep(100);
            } catch (InterruptedException ex) {
                Logger.getLogger(MediClient.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    private void waitForPlay() {
        mapPanel.permissionToPlay();
        while(mapPanel.isPlaying()) {
            if (player.hasLeftGame()) {
                break;
            }
            try {
                //Wait
                Thread.sleep(100);
            } catch (InterruptedException ex) {
                Logger.getLogger(MediClient.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

}
