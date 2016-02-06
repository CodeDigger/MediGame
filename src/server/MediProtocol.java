package server;

/**
 * Created by Tobias on 2015-07-30.
 * Medi protocol class
 */
public class MediProtocol {
    int connectedClients;
//    public static final int CLIENT1 = 0;
//    public static final int CLIENT2 = 1;
    //public static final int CLIENT3 = 3;
    private final String DUMMYMESSAGE = "0";

    private boolean waitingForResponse = true;
    private int activeClient = 0;

    private String message1;
    private String message2;
    private String message;
    
    
    public void changeConnectedClients(int i) {
        connectedClients = i;
        System.out.println("MediProtcol: "+connectedClients+" connected");
    }
    
    public void newClientConnected() {
        connectedClients++;
        System.out.println("MediProtcol: "+connectedClients+" connected");
    }
    
    public void clientDisconected() {
        connectedClients--;
        System.out.println("MediProtcol: "+connectedClients+" connected");
    }


//    public synchronized String client2Turn(String msg) {
//        if (activeClient == CLIENT1) {
//            try {
//                wait();
//                return message1;
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//        }
//        message2 = msg;
//        activeClient = CLIENT1;
//        notify();
//        return DUMMYMESSAGE;
//    }
//
//    public synchronized String client1Turn(String msg) {
//        if (activeClient == CLIENT2) {
//            try {
//                wait();
//                return message2;
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//        }
//        message1 = msg;
//        activeClient = CLIENT2;
//        notify();
//        return DUMMYMESSAGE;
//    }

    public int getActiveClient() {
        return activeClient;
    }

    public synchronized void setMessage(String message) {
        while(!waitingForResponse){
            try {
                wait();
            } catch (InterruptedException e) {}
        }
        waitingForResponse = false;
        this.message = message;
        nextActiveClient();
        notifyAll();
    }

    public synchronized String getMessage() {
        while(waitingForResponse){
            try {
                wait();
            } catch (InterruptedException e) {}
        }
        waitingForResponse = true;
        notifyAll();
        return message;
    }

    private void nextActiveClient() {
        activeClient++;
        if (activeClient == connectedClients) {
            activeClient = 0;
        }
//        if(activeClient == CLIENT1){
//            activeClient = CLIENT2;
//        }
//        else {
//            activeClient = CLIENT1;
//        }
    }
}
