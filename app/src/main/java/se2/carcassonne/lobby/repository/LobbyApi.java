package se2.carcassonne.lobby.repository;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import se2.carcassonne.helper.network.WebSocketClient;
import se2.carcassonne.lobby.model.Lobby;
import se2.carcassonne.player.model.Player;

public class LobbyApi {
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final WebSocketClient webSocketClient;

    public LobbyApi(WebSocketClient webSocketClient) {
        this.webSocketClient = webSocketClient;
    }

    public void createLobby(Lobby lobby, Player currentPlayer){
        try {
            String message = objectMapper.writeValueAsString(lobby) + "|" + objectMapper.writeValueAsString(currentPlayer);
            webSocketClient.sendMessage("/app/lobby-create", message);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public void getAllLobbies() {
        webSocketClient.sendMessage("/app/lobby-list", "send all lobbies");
    }

    public void getAllPlayers(Lobby lobby) {
        try {
            String message = objectMapper.writeValueAsString(lobby);
            webSocketClient.sendMessage("/app/player-list", message);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public void joinLobby(Lobby lobby, Player playerToJoin) {
        try {
            String message = objectMapper.writeValueAsString(lobby) + "|" + objectMapper.writeValueAsString(playerToJoin);
            webSocketClient.sendMessage("/app/player-join-lobby", message);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public void leaveLobby(Player player) {
        try {
            String message = objectMapper.writeValueAsString(player);
            webSocketClient.sendMessage("/app/player-leave-lobby", message);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
