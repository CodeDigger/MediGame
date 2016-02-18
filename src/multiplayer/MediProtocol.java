package multiplayer;

import mapBuilder.ServerMapHandler;
import tiles.Tile;

/**
 * Created by Tobias on 2015-07-30. Medi protocol class
 */
public class MediProtocol {

    int connectedClients = 0;

//    private boolean waitingForResponse = true;
    private int activeClient = 0;
    ServerMapHandler mapHandler;

    private String message;

    public MediProtocol() {
        mapHandler = new ServerMapHandler();
    }

    public synchronized void handleClientTilePlacement(String message) {
//        waitingForResponse = false;
        this.message = message;
        System.out.println("Client: " + activeClient + " played: " + message);
        
        nextActiveClient();
        notifyAll();

    }

    public synchronized String handleClientTileRequest(String message) {
//        waitingForResponse = false;
        System.out.println("Client: " + activeClient + " played: " + message);
        int[] packet = DataPacketHandler.handlePacket(message);
        Tile t = mapHandler.drawLand(0);
        String packetToClient = DataPacketHandler.createTileDeliveryPackage(t.getType());
        return packetToClient;
    }

    public synchronized String getMessage() {
        try {
            wait();
        } catch (InterruptedException e) {
        }
//        waitingForResponse = true;
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
        System.out.println("MediProtcol: " + connectedClients + " connected");
    }

    public void newClientConnected() {
        connectedClients++;
        System.out.println("MediProtcol: " + connectedClients + " connected");
    }

    public void clientDisconected() {
        connectedClients--;
        System.out.println("MediProtcol: " + connectedClients + " connected");
    }

    public synchronized int getActiveClient() {
        return activeClient;
    }
}
