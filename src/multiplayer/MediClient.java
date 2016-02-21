package multiplayer;

import events.MapPanelListener;
import events.ServerMessageListener;
import java.io.BufferedReader;
import mapBuilder.ClientPlayer;
import mapBuilder.ClientMapHandler;
import mapBuilder.ClientMapPanel;
import tiles.Tile;
import tiles.TileHandler;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * Created by Tobias on 2015-07-30.
 * Medi client class
 */
public class MediClient implements ServerMessageListener, MapPanelListener {

    private static final int WAIT = 0;
    private static final int PLAY = 1;

    String hostName;
    int portNumber;
    ClientMapPanel mapPanel;
    ClientMapHandler mapHandler;

    ClientPlayer player;
    PrintWriter outServer;
    BufferedReader inServer;


    public MediClient(String ip, int port, ClientMapPanel mapPanel) {
        hostName = ip;
        portNumber = port;
        this.mapPanel = mapPanel;
        this.mapHandler = mapPanel.getMapHandler();

        player = new ClientPlayer("Player Name", mapPanel.getSize());
        mapPanel.setPlayer(player);
        mapPanel.setUI(player.getUI());
        
        player.messagePlager("Meddelande 1");
        player.messagePlager("Meddelande 2");
        player.messagePlager("Meddelande 3");
        player.messagePlager("Meddelande 4");
        player.messagePlager("Meddelande 5");
        
        
        mapPanel.runGame();
    }

    public void initServerConnection() throws IOException {
        mapPanel.addMapPanelListener(this);
        
        Socket socket = new Socket(hostName, portNumber);
        PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        
        outServer = out;
        
        new MessageReciever(in, this).start();
        
    }

    @Override
    public void serverMessage(String serverMessage) {
        System.out.println("CLIENT - From server: " + serverMessage);

        int[] packet = DataPacketHandler.handlePacket(serverMessage);
        switch (packet[DataPacketHandler.SUBPACKET_PACKETTYPE]) {
            case DataPacketHandler.PACKETTYPE_STARTPLAY:
                mapPanel.permissionToPlay();
                break;
            case DataPacketHandler.PACKETTYPE_TILEPLACEMENT:
                int row = packet[DataPacketHandler.SUBPACKET_TILETOPLACE_ROW];
                int col = packet[DataPacketHandler.SUBPACKET_TILETOPLACE_COL];
                int type = packet[DataPacketHandler.SUBPACKET_TILETOPLACE_TYPE];
                int alignment = packet[DataPacketHandler.SUBPACKET_TILETOPLACE_ALIGNMENT];
                mapHandler.placeLandFromServer(row, col, type, alignment);
                break;
            case DataPacketHandler.PACKETTYPE_TILEDRAWN:
                int stackNumber = packet[DataPacketHandler.SUBPACKET_STACKNUMBER];
                mapHandler.removeTileFromStack(stackNumber);
                break;
            case DataPacketHandler.PACKETTYPE_TILEDELIVERY:
                Tile tileToPlay = TileHandler.initLand(packet[DataPacketHandler.SUBPACKET_TILETYPETOPLAY]); //Create a tile from tile data
                player.giveTile(tileToPlay);
                break;
            default:
                break;
        }
        mapPanel.repaint();
    }

    @Override
    public void tileDrawn(int stackNumber) {
        outServer.println(DataPacketHandler.createTileRequestPackage(stackNumber)); // Request tile from server:
    }

    @Override
    public void tilePlaced(int row, int col, int tileType, int alignment) {
        outServer.println(DataPacketHandler.createTilePlacementPackage(row,col,tileType,alignment)); // Send tile placement package to server!
    }

    @Override
    public void chatMessage(String s) {
        //TODO Implement Chat
    }

    @Override
    public void clientDisconnect() {
        outServer.println(DataPacketHandler.createLeaveGamePackage());
    }

}
