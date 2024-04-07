package se2.carcassonne.helper.network;

public interface WebSocketMessageHandler<T> {

    void onMessageReceived(T message);
    
}
