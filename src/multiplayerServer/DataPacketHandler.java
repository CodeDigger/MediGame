package multiplayerServer;

/**
 * Created by Tobias on 2016-02-07.
 * This is the class for the network packet
 * sent between server and client
 */
public class DataPacketHandler {

    public static final int PACKETTYPE_PLAYERREADY = 0;
    public static final int PACKETTYPE_TILEREQUEST = 1;
    public static final int PACKETTYPE_TILEDELIVERY = 2;
    public static final int PACKETTYPE_TILEPLACEMENT = 3;
    public static final int PACKETTYPE_LEAVEGAME = 4;
    public static final int PACKETTYPE_TILEDRAWN = 5;
    public static final int PACKETTYPE_SERVERMESSAGE = 6;
    public static final int PACKETTYPE_CHATMESSAGE = 7;
    public static final int PACKETTYPE_STARTGAME = 8;
    public static final int PACKETTYPE_PLAYERSTURN = 9;
    public static final int PACKETTYPE_CLIENTINIT = 10;

    public static final int SUBPACKET_PACKETTYPE = 0;
    public static final int SUBPACKET_TILETYPETOPLAY = 1;
    public static final int SUBPACKET_TILETOPLACE_ROW = 1;
    public static final int SUBPACKET_TILETOPLACE_COL = 2;
    public static final int SUBPACKET_TILETOPLACE_TYPE = 3;
    public static final int SUBPACKET_TILETOPLACE_ALIGNMENT = 4;
    public static final int SUBPACKET_STACKNUMBER = 1;
    public static final int SUBPACKET_PLAYERSTURN_INDEX = 1;
    public static final int SUBPACKET_PLAYERSTURN_NAME = 2;
    
    public static String createTilePlacementPackage(int row, int col, int tileType, int tileAlignment){
        return (PACKETTYPE_TILEPLACEMENT + ":" + row + ":" + col + ":" + tileType + ":" + tileAlignment);
    }
    
    public static String createTileDeliveryPackage(int tileType) {
        return (PACKETTYPE_TILEDELIVERY + ":" + tileType);
    }

    public static String createPlayerReadyPackage(){
        return String.valueOf(PACKETTYPE_PLAYERREADY+":");
    }

    public static String createTileRequestPackage(int stackNumber){
        return (PACKETTYPE_TILEREQUEST + ":" + stackNumber);
    }

    public static String createLeaveGamePackage(){
        return String.valueOf(PACKETTYPE_LEAVEGAME+":");
    }

    public static String createTileDrawnPackage(int stackNumber){
        return (PACKETTYPE_TILEDRAWN + ":" + stackNumber);
    }

    public static String createServerMessage(String message){
        return (PACKETTYPE_SERVERMESSAGE + ":" + message);
    }
    
    public static String createChatMessagePackage(String message) {
        return (PACKETTYPE_CHATMESSAGE + ":"+message);
    }

    public static String createStartGamePackage() {
        return String.valueOf(PACKETTYPE_STARTGAME+":");
    }
    
    public static String createPlayerTurnPackage(int playerIndex, String name) {
        return (PACKETTYPE_PLAYERSTURN + ":" + playerIndex+":"+name);
    }
    
    public static String createClientInitPackage(String name) {
        return (PACKETTYPE_CLIENTINIT+":"+name);
    }



    public static int[] handlePacket(String packet){
        int[] returnInt;
        int packetInt = 0;
        int readFrom = 2;
        
        
        if (packet.substring(1, 2).equals(":")) {
            packetInt = Integer.parseInt(packet.substring(0, 1));
        } else if (packet.substring(2, 3).equals(":")) {
            packetInt = Integer.parseInt(packet.substring(0, 2));
            readFrom = 3;
        }
        
        switch (packetInt) { //The first character in the packet is the package type
            case PACKETTYPE_PLAYERREADY:
                returnInt = new int[]{PACKETTYPE_PLAYERREADY};
                break;
            case PACKETTYPE_TILEREQUEST:
                int stackNumber = Integer.parseInt(packet.substring(readFrom, packet.length()));
                returnInt = new int[]{PACKETTYPE_TILEREQUEST, stackNumber};
                break;
            case PACKETTYPE_TILEDELIVERY:
                int tileDeliveryType = Integer.parseInt(packet.substring(readFrom, packet.length()));
                returnInt = new int[]{PACKETTYPE_TILEDELIVERY, tileDeliveryType};
                break;
            case PACKETTYPE_TILEPLACEMENT:

                int[] divider = new int[4];
                int a = 0;
                for (int i = 0; i < packet.length(); i++) {
                    if (packet.charAt(i) == ':') {
                        divider[a] = i;
                        a++;
                    }
                }
                int row = Integer.parseInt(packet.substring(divider[0] + 1, divider[1]));
                int col = Integer.parseInt(packet.substring(divider[1] + 1, divider[2]));
                int tileType = Integer.parseInt(packet.substring(divider[2] + 1, divider[3]));
                int tileAlignment = Integer.parseInt(packet.substring(divider[3] + 1, packet.length()));
                returnInt = new int[]{PACKETTYPE_TILEPLACEMENT,row,col,tileType,tileAlignment};

                break;
            case PACKETTYPE_LEAVEGAME:
                returnInt = new int[]{PACKETTYPE_LEAVEGAME};
                break;
            case PACKETTYPE_TILEDRAWN:
                int stackDrawnNumber = Integer.parseInt(packet.substring(readFrom, packet.length()));
                returnInt = new int[]{PACKETTYPE_TILEDRAWN, stackDrawnNumber};
                break;
            case PACKETTYPE_SERVERMESSAGE:
                returnInt = new int[]{PACKETTYPE_SERVERMESSAGE};
                break;
            case PACKETTYPE_CHATMESSAGE:
                returnInt = new int[]{PACKETTYPE_CHATMESSAGE};
                break;
            case PACKETTYPE_STARTGAME:
                returnInt = new int[]{PACKETTYPE_STARTGAME};
                break;
            case PACKETTYPE_PLAYERSTURN:
                int clientIndex = Integer.parseInt(packet.substring(readFrom, readFrom+1));
                returnInt = new int[]{PACKETTYPE_PLAYERSTURN, clientIndex};
                break;
            case PACKETTYPE_CLIENTINIT:
                returnInt = new int[]{PACKETTYPE_CLIENTINIT};
                break;
            default:
                returnInt = new int[]{0,0};
                System.out.println("DPH: [WARNING] Unidentified Packet");
                break;
        }
        return returnInt;
    }

    public static String getTextMessage(String message){
        String s = null;
        if (message.substring(1, 2).equals(":")) {
            s = message.substring(2, message.length());
        } else if (message.substring(2, 3).equals(":")) {
            s = message.substring(3, message.length());
        }
        return s;
    }
    
    /**
     * HANDLE WITH CARE! This function is dangerous and should not be used without correct safety precautions.
     * @param message
     * @param subPacket
     * @return 
     */
    public static String getSubMessage(String message, int subPacket){
        // TODO Take care of unsafe function
        int sub = 0;
        int charA = 0;
        int charB = message.length();
        
        for (int i = 0; i < message.length(); i++) {
            if (message.charAt(i) == ':') {
                sub++;
                if (charA != 0) {
                    charB = i;
                    break;
                }
                if (sub == subPacket) {
                    charA = i+1;
                }
            }
        }
        return message.substring(charA, charB);
        
    }
}
