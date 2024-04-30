package se2.carcassonne.repository;

import se2.carcassonne.helper.network.WebSocketClient;

public class NextTurnRepository {

    private final WebSocketClient webSocketClient = WebSocketClient.getInstance();

    public NextTurnRepository(){
    }


}
