package se2.carcassonne.player.repository;

import androidx.lifecycle.MutableLiveData;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.regex.Pattern;

import lombok.RequiredArgsConstructor;
import se2.carcassonne.helper.network.WebSocketClient;
import se2.carcassonne.player.model.Player;

@RequiredArgsConstructor
public class PlayerRepository {
    private static PlayerRepository instance;
    private Player currentPlayer;
    private final WebSocketClient webSocketClient = WebSocketClient.getInstance();
    private final MutableLiveData<String> createPlayerLiveData = new MutableLiveData<>();
    private final MutableLiveData<String> userAlreadyExistsErrorMessage = new MutableLiveData<>();
    private final MutableLiveData<String> invalidUsernameErrorMessage = new MutableLiveData<>();
    private final PlayerApi playerApi;
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

    public void createPlayer(Player player) {
        if (isValidUsername(player.getUsername())) {
            webSocketClient.subscribeToQueue("/user/queue/response", this::createPlayerMessageReceived);
            webSocketClient.subscribeToQueue("/user/queue/errors", this::createPlayerMessageReceived);
            playerApi.createUser(player);
        } else {
            invalidUsernameErrorMessage.postValue("Invalid Username!");
        }
    }

    private void createPlayerMessageReceived(String message) {
//        webSocketClient.unsubscribeFromQueue("/user/queue/response");
//        webSocketClient.unsubscribeFromQueue("/user/queue/errors");
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
