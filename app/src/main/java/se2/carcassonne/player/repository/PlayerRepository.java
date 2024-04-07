package se2.carcassonne.player.repository;

import androidx.lifecycle.MutableLiveData;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;
import se2.carcassonne.helper.network.WebSocketClient;
import se2.carcassonne.lobby.model.Lobby;
import se2.carcassonne.player.model.Player;

@RequiredArgsConstructor
public class PlayerRepository {
    private static PlayerRepository instance;
    private Player currentPlayer;
    private final WebSocketClient webSocketClient;
    private final MutableLiveData<String> messageLiveData = new MutableLiveData<>();
    private final MutableLiveData<String> userAlreadyExistsErrorMessage = new MutableLiveData<>();
    private final MutableLiveData<String> invalidUsernameErrorMessage = new MutableLiveData<>();
    private final PlayerApi playerApi;

    private PlayerRepository() {
        this.webSocketClient = new WebSocketClient();
        this.playerApi = new PlayerApi(this.webSocketClient);
    }

    // Using Singleton pattern
    public static PlayerRepository getInstance() {
        if (instance == null) {
            instance = new PlayerRepository();
        }
        return instance;
    }

    public void connectToWebSocketServer() {
        webSocketClient.connect(this::messageReceivedFromServer);
    }

    private void messageReceivedFromServer(String message) {
        if (userAlreadyExistsError(message)) {
            userAlreadyExistsErrorMessage.postValue("User with that name already exists! Try again.");
        } else {
            try {
                ObjectMapper mapper = new ObjectMapper();
                currentPlayer = mapper.readValue(message, Player.class);
            } catch (Exception e) {
                e.printStackTrace();
            }
            messageLiveData.postValue(message);
        }
    }

    private boolean userAlreadyExistsError(String message) {
        return message.startsWith("ERROR:");
    }

    public void createPlayer(Player player) {
        if (isValidUsername(player.getUsername())) {
            webSocketClient.subscribeToQueue("/user/queue/player-response", this::messageReceivedFromServer);
            webSocketClient.subscribeToQueue("/user/queue/errors", this::messageReceivedFromServer);
            playerApi.createUser(player);
        } else {
            invalidUsernameErrorMessage.postValue("Invalid Username!");
        }
    }

    private boolean isValidUsername(String username) {
        String regex = "^[a-zA-Z0-9]+(?:[_ -]?[a-zA-Z0-9]+)*$";
        return username.matches(regex);
    }

    public Player getCurrentPlayer() {
        return currentPlayer;
    }
    public void setCurrentPlayer(Player player) {
        this.currentPlayer = player;
    }

    public MutableLiveData<String> getMessageLiveData() {
        return messageLiveData;
    }

    public MutableLiveData<String> getUserAlreadyExistsErrorMessage() {
        return userAlreadyExistsErrorMessage;
    }

    public MutableLiveData<String> getInvalidUsernameErrorMessage() {
        return invalidUsernameErrorMessage;
    }

    public void updateCurrentPlayerLobby(Lobby lobby){
        currentPlayer.setGameLobbyDto(lobby);
    }
}
