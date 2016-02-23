package events;

/**
 * Created by Tobias on 2016-02-23.
 */
public interface ServerMessageListener {

    void receivedMessage(String message, int clientIndex);
}
