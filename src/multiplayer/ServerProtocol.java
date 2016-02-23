package multiplayer;

import events.ServerMessageListener;
import mapBuilder.ServerMapHandler;
import tiles.Tile;

import java.io.PrintWriter;
import java.util.ArrayList;

/**
 * Created by Tobias on 2015-07-30. Medi protocol class
 */
public class ServerProtocol implements ServerMessageListener {

    int connectedClients = 0;
    ArrayList<PrintWriter> outList;

    private int activeClient = 0;
    ServerMapHandler mapHandler;

    private String message;

    public ServerProtocol() {
        mapHandler = new ServerMapHandler();
        outList = new ArrayList<>();
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

    @Override
    public synchronized void receivedMessage(String message, int clientIndex) {
        int[] packet = DataPacketHandler.handlePacket(message);
        switch (packet[DataPacketHandler.SUBPACKET_PACKETTYPE]){
            case DataPacketHandler.PACKETTYPE_TILEREQUEST:
                int stackNumber = packet[DataPacketHandler.SUBPACKET_STACKNUMBER];
                System.out.println("MEDIPROTOCOL: Client " + activeClient + " requested a tile from stack " + stackNumber);
                Tile t = mapHandler.drawLand(stackNumber);
                notifyActiveClient(DataPacketHandler.createTileDeliveryPackage(t.getType()));
                notifyInactiveClients(DataPacketHandler.createTileDrawnPackage(stackNumber));
            break;
            case DataPacketHandler.PACKETTYPE_TILEPLACEMENT:
                notifyInactiveClients(message);
                System.out.println("MEDIPROTOCOL: Client " + activeClient + " placed: " + message);
                nextActiveClient();
                break;
            case DataPacketHandler.PACKETTYPE_LEAVEGAME:
                //TODO handle this
                break;
            case DataPacketHandler.PACKETTYPE_CHATMESSAGE:
                //TODO handle this too
                break;
            default:
                break;
        }
    }

    private void notifyInactiveClients(String message) {
        outList.forEach(out->{
            if(outList.indexOf(out) != activeClient){
                out.println(message);
            }
        });
    }

    private void notifyActiveClient(String message) {
        outList.get(activeClient).println(message);
    }

    public void nextActiveClient() {
        activeClient++;
        if (activeClient >= connectedClients) {
            activeClient = 0;
        }
        notifyActiveClient(DataPacketHandler.createStatusUpdatePackage(DataPacketHandler.STATUS_PLAY));
    }

    public void newClientConnected(PrintWriter out) {
        connectedClients++;
        outList.add(out);
        System.out.println("MEDIPROTOCOL: " + connectedClients + " connected");
    }

    public void clientDisconnected() {
        connectedClients--;
        System.out.println("MEDIPROTOCOL: " + connectedClients + " connected");
    }
}
