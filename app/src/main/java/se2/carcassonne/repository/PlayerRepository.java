package se2.carcassonne.repository;

import androidx.lifecycle.MutableLiveData;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.regex.Pattern;

import lombok.RequiredArgsConstructor;
import se2.carcassonne.helper.network.WebSocketClient;
import se2.carcassonne.model.Player;
import se2.carcassonne.api.PlayerApi;

@RequiredArgsConstructor
public class PlayerRepository {
    private static PlayerRepository instance;
    private Player currentPlayer;
    private final WebSocketClient webSocketClient = WebSocketClient.getInstance();
    private final MutableLiveData<String> createPlayerLiveData = new MutableLiveData<>();
    private final MutableLiveData<String> userAlreadyExistsErrorMessage = new MutableLiveData<>();
    private final MutableLiveData<String> invalidUsernameErrorMessage = new MutableLiveData<>();
    private final PlayerApi playerApi;

    private static final String QUEUE_RESPONSE = "/user/queue/response";
    private static final String QUEUE_ERRORS = "/user/queue/errors";
    private static final Pattern playerNamePattern = Pattern.compile("^[a-zA-Z0-9]+(?:[_ -]?[a-zA-Z0-9]+)*$");

    private PlayerRepository() {
        this.playerApi = new PlayerApi();
    }

    public static PlayerRepository getInstance() {
        if (instance == null) {
            instance = new PlayerRepository();
        }
        return instance;
    }

    public static void resetInstance() {
        instance = null;
    }

    public void createPlayer(Player player) {
        if (isValidUsername(player.getUsername())) {
            webSocketClient.subscribeToQueue(QUEUE_RESPONSE, this::createPlayerMessageReceived);
            webSocketClient.subscribeToQueue(QUEUE_ERRORS, this::createPlayerMessageReceived);
            playerApi.createUser(player);
        } else {
            invalidUsernameErrorMessage.postValue("Invalid Username!");
        }
    }

    private void createPlayerMessageReceived(String message) {
        webSocketClient.unsubscribe(QUEUE_RESPONSE);
        webSocketClient.unsubscribe(QUEUE_ERRORS);
        if (userAlreadyExistsError(message)) {
            userAlreadyExistsErrorMessage.postValue("User with that name already exists! Try again.");
        } else {
            try {
                ObjectMapper mapper = new ObjectMapper();
                currentPlayer = mapper.readValue(message, Player.class);
            } catch (Exception e) {
                e.printStackTrace();
            }
            createPlayerLiveData.postValue(message);
        }
    }

    private boolean userAlreadyExistsError(String message) {
        return message.startsWith("ERROR:");
    }

    private boolean isValidUsername(String username) {
        return playerNamePattern.matcher(username).matches();
    }

    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    public void resetCurrentPlayer() {
        currentPlayer.setGameLobbyId(null);
        currentPlayer.setGameSessionId(null);
    }

    public MutableLiveData<String> getCreatePlayerLiveData() {
        return createPlayerLiveData;
    }

    public MutableLiveData<String> getUserAlreadyExistsErrorMessage() {
        return userAlreadyExistsErrorMessage;
    }

    public MutableLiveData<String> getInvalidUsernameErrorMessage() {
        return invalidUsernameErrorMessage;
    }
}
