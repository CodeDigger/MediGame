package multiplayer;

/**
 * Created by Tobias on 2016-02-07.
 * This is the class for the network packet
 * sent between server and client
 */
public class DataPacketHandler {

    public static final int PACKETTYPE_STATUSUPDATE = 0;
    public static final int PACKETTYPE_TILEREQUEST = 1;
    public static final int PACKETTYPE_TILEDELIVERY = 2;
    public static final int PACKETTYPE_TILEPLACEMENT = 3;
    
    public static final int STATUS_WAIT = 0;
    public static final int STATUS_PLAY = 1;
    public static final int STATUS_UPDATEBOARD = 2;



    public static String createTilePlacementPackage(int row, int col, int tileType, int tileAlignment){
        return (PACKETTYPE_TILEPLACEMENT + ":" + row + ":" + col + ":" + tileType + ":" + tileAlignment);
    }

    public static String createStatusUpdatePackage(int status){
        return (PACKETTYPE_STATUSUPDATE + ":" + status);
    }

    public static String createTileRequestPackage(){
        return String.valueOf(PACKETTYPE_TILEREQUEST);
    }

    public static int[] handlePacket(String packet){
        int[] returnInt;
        switch (Character.getNumericValue(packet.charAt(0))) {
            case PACKETTYPE_STATUSUPDATE:
                returnInt = new int[]{PACKETTYPE_STATUSUPDATE, Character.getNumericValue(packet.charAt(2))};
                break;
            case PACKETTYPE_TILEREQUEST:
                returnInt = new int[]{PACKETTYPE_TILEREQUEST, 0};
                break;
            case PACKETTYPE_TILEDELIVERY:
                int tileDeliveryType = Integer.getInteger(packet.substring(2, packet.length()-1));
                returnInt = new int[]{PACKETTYPE_TILEDELIVERY, tileDeliveryType};
            case PACKETTYPE_TILEPLACEMENT:
                
                int[] divider = new int[4];
                int a = 0;
                for (int i = 0; i < packet.length(); i++) {
                    if (packet.charAt(i) == ':') {
                        divider[a] = i;
                        a++;
                    }
                }
                int row = Integer.getInteger(packet.substring(divider[0]+1, divider[1]-1));
                int col = Integer.getInteger(packet.substring(divider[1]+1, divider[2]-1));
                int tileType = Integer.getInteger(packet.substring(divider[2]+1, divider[3]-1));
                int tileAlignment = Integer.getInteger(packet.substring(divider[2]+1, divider[3]-1));
                returnInt = new int[]{PACKETTYPE_TILEPLACEMENT,row,col,tileType,tileAlignment};
                
                break;
            
            default:
                returnInt = new int[]{0,0};
                break;
        }
        return returnInt;
    }

}
