package server;

/**
 * Created by Tobias on 2015-07-30.
 * Medi protocol class
 */
public class MediProtocol {
    int connectedClients = 0;

    private boolean waitingForResponse = true;
    private int activeClient = 0;

    private String message;

    public synchronized void handleClientRequest(String message) {
//        while(!waitingForResponse){
//            try {
//                wait();
//            } catch (InterruptedException e) {}
//        }
//        try {
//                wait();
//            } catch (InterruptedException e) {}
        waitingForResponse = false;
        this.message = message;
        System.out.println("Client: " + activeClient + " played " + message);
        //TODO Handle the client request
        nextActiveClient();
        notifyAll();
    }

    public synchronized String getMessage() {
//        while(waitingForResponse){
//            try {
//                wait();
//            } catch (InterruptedException e) {}
//        }
        try {
                wait();
            } catch (InterruptedException e) {}
        waitingForResponse = true;
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
        System.out.println("MediProtcol: "+connectedClients+" connected");
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
