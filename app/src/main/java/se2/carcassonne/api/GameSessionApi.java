package se2.carcassonne.api;

import com.fasterxml.jackson.databind.ObjectMapper;

import se2.carcassonne.helper.network.WebSocketClient;
import se2.carcassonne.model.PlacedTileDto;

public class GameSessionApi {
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final WebSocketClient webSocketClient = WebSocketClient.getInstance();


    public GameSessionApi(){
    }

    public void nextTurn(Long gameSessionId) {
        try {
            webSocketClient.sendMessage("/app/next-turn", objectMapper.writeValueAsString(gameSessionId));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sendPlacedTile(PlacedTileDto placedTileDto){
        try {
            webSocketClient.sendMessage("/app/place-tile", objectMapper.writeValueAsString(placedTileDto));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
