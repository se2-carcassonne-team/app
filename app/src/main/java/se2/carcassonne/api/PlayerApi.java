package se2.carcassonne.api;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import se2.carcassonne.helper.network.WebSocketClient;
import se2.carcassonne.model.Player;

public class PlayerApi {
    private final ObjectMapper objectMapper;
    private final WebSocketClient webSocketClient;

    public PlayerApi(){
        objectMapper = new ObjectMapper();
        webSocketClient = WebSocketClient.getInstance();
    }

    public void createUser(Player player) {
        try {
            webSocketClient.sendMessage("/app/player-create", objectMapper.writeValueAsString(player));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    public void deleteUser(Player player) {
        try {
            webSocketClient.sendMessage("/app/player-delete", objectMapper.writeValueAsString(player));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }
}
