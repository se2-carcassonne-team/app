package se2.carcassonne.api;

import android.util.Log;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import se2.carcassonne.helper.network.WebSocketClient;
import se2.carcassonne.model.FinishedTurnDto;
import se2.carcassonne.model.Meeple;
import se2.carcassonne.model.PlacedTileDto;
import se2.carcassonne.model.Scoreboard;
import se2.carcassonne.model.Player;

public class GameSessionApi {
    private final ObjectMapper objectMapper;
    private final WebSocketClient webSocketClient;


    public GameSessionApi() {
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
    public void leaveGame(Player player) {
        try {
            String message = objectMapper.writeValueAsString(player);
            webSocketClient.sendMessage("/app/player-leave-gamesession", message);
        } catch (Exception e) {
            Log.e("GameSessionApi", "Error leave game message", e);
        }
    }
    public void sendPlacedTile(PlacedTileDto placedTileDto){
        try {
            webSocketClient.sendMessage("/app/place-tile", objectMapper.writeValueAsString(placedTileDto));
        } catch (Exception e) {
            Log.e("GameSessionApi", "Error sending placed tile message", e);
        }
    }

    public void sendPointsForCompletedRoad(FinishedTurnDto finishedTurnDto) {
        try {
            webSocketClient.sendMessage("/app/update-points-meeples", objectMapper.writeValueAsString(finishedTurnDto));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    public void forwardScoreboard(Scoreboard scoreboard) {
        try {
            webSocketClient.sendMessage("/app/scoreboard", objectMapper.writeValueAsString(scoreboard));
        } catch (Exception e) {
            Log.e("GameSessionApi", "Error sending scoreboard message", e);
        }
    }
}
