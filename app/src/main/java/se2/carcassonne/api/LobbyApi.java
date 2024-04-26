package se2.carcassonne.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import se2.carcassonne.helper.network.WebSocketClient;
import se2.carcassonne.model.Lobby;
import se2.carcassonne.model.Player;

public class LobbyApi {
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final WebSocketClient webSocketClient = WebSocketClient.getInstance();
    public LobbyApi() {

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

    public void getAllPlayers(Long lobbyId) {
        try {
            String message = objectMapper.writeValueAsString(lobbyId);
            webSocketClient.sendMessage("/app/player-list", message);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public void joinLobby(Long lobbyId, Player playerToJoin) {
        try {
            String message = objectMapper.writeValueAsString(lobbyId) + "|" + objectMapper.writeValueAsString(playerToJoin);
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

    public void startGame(Long gameLobbyId) {
        webSocketClient.sendMessage("/app/game-start", gameLobbyId.toString());
    }
}
