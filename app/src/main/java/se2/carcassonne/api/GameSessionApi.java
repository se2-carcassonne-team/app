package se2.carcassonne.api;

import android.util.Log;

import com.fasterxml.jackson.databind.ObjectMapper;

import se2.carcassonne.helper.network.WebSocketClient;
import se2.carcassonne.model.PlacedTileDto;

public class GameSessionApi {
    private final ObjectMapper objectMapper;
    private final WebSocketClient webSocketClient;


    public GameSessionApi(){
        this.objectMapper = new ObjectMapper();
        this.webSocketClient = WebSocketClient.getInstance();
    }

    public void nextTurn(Long gameSessionId) {
        try {
            webSocketClient.sendMessage("/app/next-turn", objectMapper.writeValueAsString(gameSessionId));
        } catch (Exception e) {
            Log.e("GameSessionApi", "Error sending next turn message", e);
        }
    }

    public void sendPlacedTile(PlacedTileDto placedTileDto){
        try {
            webSocketClient.sendMessage("/app/place-tile", objectMapper.writeValueAsString(placedTileDto));
        } catch (Exception e) {
            Log.e("GameSessionApi", "Error sending placed tile message", e);
        }
    }
}
