package se2.carcassonne.player.repository;

import androidx.lifecycle.MutableLiveData;

import lombok.RequiredArgsConstructor;
import se2.carcassonne.helper.network.WebSocketClient;
import se2.carcassonne.player.model.Player;

@RequiredArgsConstructor
public class PlayerRepository {
    private final WebSocketClient webSocketClient;
    private final MutableLiveData<String> messageLiveData = new MutableLiveData<>();

    private final PlayerApi playerApi;

    public PlayerRepository() {
        this.webSocketClient = new WebSocketClient();
        this.playerApi = new PlayerApi(this.webSocketClient);

    }

    public void connectToWebSocketServer() {
        webSocketClient.connect(this::messageReceivedFromServer);
    }

    private void messageReceivedFromServer(String message) {
        messageLiveData.postValue(message);
    }

    public void createUser(Player player) {
        playerApi.createUser(player);
    }

    public MutableLiveData<String> getMessageLiveData() {
        return messageLiveData;
    }
}
