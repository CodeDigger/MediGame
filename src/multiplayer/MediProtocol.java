package multiplayer;

import mapBuilder.ServerMapHandler;
import tiles.Tile;

/**
 * Created by Tobias on 2015-07-30. Medi protocol class
 */
public class MediProtocol {

    int connectedClients = 0;

    private int activeClient = 0;
    ServerMapHandler mapHandler;

    private String message;

    public MediProtocol() {
        mapHandler = new ServerMapHandler();
    }

    public synchronized String handleClientTilePlacement(String message) {
        String returnString;
        if (DataPacketHandler.handlePacket(message)[DataPacketHandler.SUBPACKET_PACKETTYPE] == DataPacketHandler.PACKETTYPE_LEAVEGAME){
            returnString = "QUIT";
        }
        else {
            returnString = "PLAYING";
        }
        this.message = message;
        System.out.println("MEDIPROTOCOL: Client " + activeClient + " placed: " + message);
        
        nextActiveClient();
        notifyAll();
        return returnString;

    }

    public synchronized String handleClientTileRequest(String message) {
        int[] decodedMessage = DataPacketHandler.handlePacket(message);
        if (decodedMessage[DataPacketHandler.SUBPACKET_PACKETTYPE] == DataPacketHandler.PACKETTYPE_LEAVEGAME){
            return "QUIT";
        }
        else {
            int stackNumber = decodedMessage[DataPacketHandler.SUBPACKET_STACKNUMBER];
            System.out.println("MEDIPROTOCOL: Client " + activeClient + " requested a tile from stack " + stackNumber);
            Tile t = mapHandler.drawLand(stackNumber);
            this.message = DataPacketHandler.createTileDrawnPackage(stackNumber);
            notifyAll();
            return DataPacketHandler.createTileDeliveryPackage(t.getType());
        }
    }

    public synchronized String getMessage() {
        try {
            wait();
        } catch (InterruptedException e) {
        }
        notifyAll();
        return message;
    }

    private void nextActiveClient() {
        activeClient++;
        if (activeClient >= connectedClients) {
            activeClient = 0;
        }
    }

    public void setConnectedClients(int i) {
        connectedClients = i;
        System.out.println("MEDIPROTOCOL: " + connectedClients + " connected");
    }

    public void newClientConnected() {
        connectedClients++;
        System.out.println("MEDIPROTOCOL: " + connectedClients + " connected");
    }

    public void clientDisconected() {
        connectedClients--;
        System.out.println("MEDIPROTOCOL: " + connectedClients + " connected");
    }

    public synchronized int getActiveClient() {
        return activeClient;
    }
}
