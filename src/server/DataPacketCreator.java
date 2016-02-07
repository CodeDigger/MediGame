package server;

/**
 * Created by Tobias on 2016-02-07.
 * This is the class for the network packet
 * sent between server and client
 */
public class DataPacketCreator {

    public static final int PACKETTYPE_TILEPLACEMENT = 0;
    public static final int PACKETTYPE_STATUSUPDATE = 1;
    public static final int PACKETTYPE_TILEREQUEST = 2;

    public static final int STATUS_PLAY = 0;
    public static final int STATUS_WAIT = 1;
    public static final int STATUS_UPDATE_BOARD = 1;



    static String createTilePlacementPackage(int row, int col, int tileType, int tileAlignment){
        return (PACKETTYPE_TILEPLACEMENT + ":" + row + ":" + col + ":" + tileType + ":" + tileAlignment);
    }

    static String createStatusUpdatePackage(int status){
        return (PACKETTYPE_STATUSUPDATE + ":" + status);
    }

    static String createTileRequestPackage(){
        return String.valueOf(PACKETTYPE_TILEREQUEST);
    }

}
