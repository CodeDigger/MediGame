package multiplayer;

import events.MapPanelListener;
import events.MessageListener;
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
public class Client implements MessageListener, MapPanelListener {

    String hostName;
    int portNumber;
    ClientMapPanel mapPanel;
    ClientMapHandler mapHandler;

    ClientPlayer player;
    int clientIndex = -1;
    PrintWriter outServer;


    public Client(String ip, int port, String playerName, ClientMapPanel mapPanel) {
        hostName = ip;
        portNumber = port;
        this.mapPanel = mapPanel;
        this.mapHandler = mapPanel.getMapHandler();

        player = new ClientPlayer(playerName, mapPanel.getSize());
        mapPanel.setPlayer(player);
        mapPanel.setUI(player.getUI());
        
        player.messagePlayer("Meddelande 1");
        player.messagePlayer("Meddelande 2");
        player.messagePlayer("Meddelande 3");
        player.messagePlayer("Meddelande 4");
        player.messagePlayer("Meddelande 5");

    }

    public void initServerConnection() throws IOException {
        mapPanel.addMapPanelListener(this);
        
        Socket socket = new Socket(hostName, portNumber);
        outServer = new PrintWriter(socket.getOutputStream(), true);
        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        
        new MessageReceiverThread(in, this).start();

        mapPanel.startLobby();
        
    }

    @Override
    public void receivedMessage(String serverMessage) {
        System.out.println("CLIENT - From server: " + serverMessage);

        int[] packet = DataPacketHandler.handlePacket(serverMessage);
        switch (packet[DataPacketHandler.SUBPACKET_PACKETTYPE]) {
            case DataPacketHandler.PACKETTYPE_PLAYERSTURN:
                int playersTurn = packet[DataPacketHandler.SUBPACKET_PLAYERSTURN];
                if (clientIndex == -1) {
                    clientIndex = playersTurn;
                    System.out.println("CLIENT: You have "+clientIndex+" as Client Index! :) ");
                } else if (clientIndex == playersTurn) {
                    mapPanel.permissionToPlay();
                    player.getUI().setTurn(true);
                } else {
                    player.getUI().setTurn(false);
                    //TODO Tell player who's playing
                }
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
            case DataPacketHandler.PACKETTYPE_CHATMESSAGE:
                player.messagePlayer(DataPacketHandler.getTextMessage(serverMessage));
                break;
            case DataPacketHandler.PACKETTYPE_STARTGAME:
                mapPanel.gameStartedByServer();
                break;
            case DataPacketHandler.PACKETTYPE_SERVERMESSAGE:
                //TODO fix this
                break;
            default:
                break;
        }
        mapPanel.repaint();
    }

    @Override
    public void tileRequested(int stackNumber) {
        outServer.println(DataPacketHandler.createTileRequestPackage(stackNumber)); // Request tile from server:
    }

    @Override
    public void tilePlaced(int row, int col, int tileType, int alignment) {
        outServer.println(DataPacketHandler.createTilePlacementPackage(row,col,tileType,alignment)); // Send tile placement package to server!
    }

    @Override
    public void chatMessage(String s) {
        outServer.println(DataPacketHandler.createChatMessagePackage(s));
    }

    @Override
    public void clientDisconnect() {
        outServer.println(DataPacketHandler.createLeaveGamePackage());
    }

    @Override
    public void ready() {
        outServer.println(DataPacketHandler.createPlayerReadyPackage());
    }

}
