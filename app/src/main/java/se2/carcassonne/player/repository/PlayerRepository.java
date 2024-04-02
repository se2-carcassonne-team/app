package se2.carcassonne.player.repository;

import androidx.lifecycle.MutableLiveData;

import lombok.RequiredArgsConstructor;
import se2.carcassonne.helper.network.WebSocketClient;
import se2.carcassonne.player.model.Player;

@RequiredArgsConstructor
public class PlayerRepository {
    private final WebSocketClient webSocketClient;
    private final MutableLiveData<String> messageLiveData = new MutableLiveData<>();
    private final MutableLiveData<String> userAlreadyExistsErrorMessage = new MutableLiveData<>();
    private final MutableLiveData<String> invalidUsernameErrorMessage = new MutableLiveData<>();
    private final PlayerApi playerApi;

    public PlayerRepository() {
        this.webSocketClient = new WebSocketClient();
        this.playerApi = new PlayerApi(this.webSocketClient);

    }

    public void connectToWebSocketServer() {
        webSocketClient.connect(this::messageReceivedFromServer);
    }

    private void messageReceivedFromServer(String message) {
        if (!userAlreadyExistsError(message)) {
            messageLiveData.postValue(message);
        }
    }

    private boolean userAlreadyExistsError(String message) {
        if (message.startsWith("A player with the username:")) {
            userAlreadyExistsErrorMessage.postValue("User with that name already exists! Try again.");
            return true;
        }
        return false;
    }

    public void createUser(Player player) {
        if (isValidUsername(player.getUsername())) {
            playerApi.createUser(player);
        } else {
            invalidUsernameErrorMessage.postValue("Invalid Username!");
        }
    }

    private boolean isValidUsername(String username) {
        String regex = "^[a-zA-Z0-9]+(?:[_ -]?[a-zA-Z0-9]+)*$";
        return username.matches(regex);
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
}
