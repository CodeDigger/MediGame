package server;

/**
 * Created by Tobias on 2016-02-07.
 * This is the class for the network packet
 * sent between server and client
 */
public class DataPacketHandler {

    public static final int PACKETTYPE_TILEPLACEMENT = 0;
    public static final int PACKETTYPE_STATUSUPDATE = 1;
    public static final int PACKETTYPE_TILEREQUEST = 2;

    public static final int STATUS_WAIT = 0;
    public static final int STATUS_PLAY = 1;
    public static final int STATUS_UPDATE_BOARD = 2;



    public static String createTilePlacementPackage(int row, int col, int tileType, int tileAlignment){
        return (PACKETTYPE_TILEPLACEMENT + ":" + row + ":" + col + ":" + tileType + ":" + tileAlignment);
    }

    public static String createStatusUpdatePackage(int status){
        return (PACKETTYPE_STATUSUPDATE + ":" + status);
    }

    public static String createTileRequestPackage(){
        return String.valueOf(PACKETTYPE_TILEREQUEST);
    }

    public static int handlePacket(String packet){
        int returnInt;
        switch (Character.getNumericValue(packet.charAt(0))) {
            case PACKETTYPE_TILEPLACEMENT:
                returnInt = 0;
                //TODO Handle this
                break;
            case PACKETTYPE_STATUSUPDATE:
                returnInt = Character.getNumericValue(packet.charAt(2));
                break;
            case PACKETTYPE_TILEREQUEST:
                returnInt = 0;
                //TODO Handle this as well
                break;
            default:
                returnInt = 0;
                break;
        }
        return returnInt;
    }

}
