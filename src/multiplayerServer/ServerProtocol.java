package multiplayerServer;

import events.ServerMessageListener;
import multiplayerMode.ServerMapHandler;
import tiles.Tile;

import java.io.PrintWriter;
import java.util.ArrayList;

/**
 * Created by Tobias on 2015-07-30. Medi protocol class
 */
public class ServerProtocol implements ServerMessageListener {

    //int connectedClients = 0;
    ArrayList<PrintWriter> outList;

    ArrayList<ClientInfo> connectedClients;

    private int activeClient = 0;
    ServerMapHandler mapHandler;

    public ServerProtocol() {
        mapHandler = new ServerMapHandler();
        outList = new ArrayList<>();
        connectedClients = new ArrayList<>();
    }

    @Override
    public synchronized void receivedMessage(String message, int clientIndex) {
        int[] packet = DataPacketHandler.handlePacket(message);
        switch (packet[DataPacketHandler.SUBPACKET_PACKETTYPE]){
            case DataPacketHandler.PACKETTYPE_TILEREQUEST:
                int stackNumber = packet[DataPacketHandler.SUBPACKET_STACKNUMBER];
                System.out.println("MEDIPROTOCOL: Client " + activeClient + " requested a tile from stack " + stackNumber);
                Tile t = mapHandler.drawLand(stackNumber);
                notifyClientByIndex(DataPacketHandler.createTileDeliveryPackage(t.getType()), clientIndex);
                notifyComplementClientsByIndex(DataPacketHandler.createTileDrawnPackage(stackNumber), clientIndex);
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
                String s = DataPacketHandler.getTextMessage(message);
                System.out.println("Client Message: "+s);
                notifyAllClients(message); //Just forward the same message that client sent to all clients
                break;
            case DataPacketHandler.PACKETTYPE_PLAYERREADY: //The client are ready to start play
                notifyAllClients(DataPacketHandler.createServerMessage(connectedClients.get(clientIndex).getName()+ " is ready!"));
                connectedClients.get(getListIndexByClientIndex(clientIndex)).ready2Play = true;
                if (allPlayersReady2Play()){
                    startGame();
                }
                break;
            case DataPacketHandler.PACKETTYPE_CLIENTINIT:
                String name = DataPacketHandler.getTextMessage(message);
                connectedClients.get(clientIndex).setName(name);
                notifyAllClients(DataPacketHandler.createServerMessage(name+" connected!"));
                break;
            default:
                break;
        }
    }

    private void startGame() {
        notifyAllClients(DataPacketHandler.createStartGamePackage());
        notifyAllClients(DataPacketHandler.createPlayerTurnPackage(activeClient, connectedClients.get(activeClient).getName()));
        notifyAllClients(DataPacketHandler.createServerMessage("           |:|   LET THE CNARCASONNING BEGIN   |:|"));
    }

    private boolean allPlayersReady2Play() {

        for (ClientInfo clientInfo: connectedClients){
            if (!clientInfo.ready2Play){
                return false;
            }
        }
        return true;
    }

    private void notifyInactiveClients(String message) {
        outList.forEach(out->{
            if(outList.indexOf(out) != activeClient){
                out.println(message);
            }
        });
    }
    
    private void notifyAllClients(String message) {
        outList.forEach(out -> out.println(message));
    }

    private void notifyActiveClient(String message) {
        outList.get(activeClient).println(message);
    }

    private void notifyClientByIndex(String message, int clientIndex){
        outList.get(clientIndex).println(message);
    }

    private void notifyComplementClientsByIndex(String message, int clientIndex){
        outList.forEach(out->{
            if(outList.indexOf(out) != clientIndex){
                out.println(message);
            }
        });
    }

    public void nextActiveClient() {
        activeClient++;
        if (activeClient > connectedClients.size()-1) {
            activeClient = 0;
        }
//        notifyActiveClient(DataPacketHandler.createStartTurnPackage());
        notifyAllClients(DataPacketHandler.createPlayerTurnPackage(activeClient, connectedClients.get(activeClient).getName()));
    }

    public void newClientConnected(PrintWriter out, int clientIndex) {
        connectedClients.add(new ClientInfo(clientIndex, false, true));
        outList.add(out);
        notifyClientByIndex(DataPacketHandler.createPlayerTurnPackage(clientIndex,null), clientIndex);
        System.out.println("MEDIPROTOCOL: " + connectedClients + " connected");
    }

    public void clientDisconnected(int clientIndex) {
        //connectedClients--;
        //TODO fix this in some smart way
        System.out.println("MEDIPROTOCOL: " + connectedClients + " connected");
    }

    private int getListIndexByClientIndex(int clientIndex) {
        int listIndex = -1;
        for (int i = 0; i < connectedClients.size(); i++) {
            if(connectedClients.get(i).clientIndex == clientIndex){
                listIndex = i;
            }
        }
        return listIndex;
    }

    private class ClientInfo {
        int clientIndex;
        boolean ready2Play;
        boolean connected;
        String name = "NoName";
        //TODO add ip;

        public ClientInfo(int clientIndex, boolean ready2Play, boolean connected) {
            this.clientIndex = clientIndex;
            this.ready2Play = ready2Play;
            this.connected = connected;
        }
        
        public void setName(String name) {
            this.name = name;
            System.out.println("SERVER: Client name is: "+name);
        }
        
        public String getName() {
            return name;
        }
        
    }
}
