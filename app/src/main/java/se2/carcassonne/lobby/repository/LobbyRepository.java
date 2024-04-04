package se2.carcassonne.lobby.repository;

import androidx.lifecycle.MutableLiveData;

import java.util.List;

import lombok.RequiredArgsConstructor;
import se2.carcassonne.helper.network.WebSocketClient;
import se2.carcassonne.lobby.model.Lobby;
import se2.carcassonne.player.repository.PlayerRepository;

@RequiredArgsConstructor
public class LobbyRepository {
    private final WebSocketClient webSocketClient;
    private final MutableLiveData<String> createLobbyLiveData = new MutableLiveData<>();
    private final MutableLiveData<String> lobbyAlreadyExistsErrorMessage = new MutableLiveData<>();
    private final MutableLiveData<String> invalidLobbyNameErrorMessage = new MutableLiveData<>();
    private final MutableLiveData<String> messageLiveData = new MutableLiveData<>();
    private final MutableLiveData<String> messageLiveDataListLobbies = new MutableLiveData<>();
    private final LobbyApi lobbyApi;
    private PlayerRepository playerRepository;

    public LobbyRepository(PlayerRepository playerRepository) {
        this.webSocketClient = new WebSocketClient();
        this.playerRepository = playerRepository;
        this.lobbyApi = new LobbyApi(this.webSocketClient);
    }

    public void connectToWebSocketServer() {
        webSocketClient.connect(this::messageReceivedFromServer);
    }
    public void subscribeToListLobbyTicket() {
        webSocketClient.subscribeToListLobbiesTopic(this::messageReceivedFromServer);
    }

    private void messageReceivedFromServer(String message) {
        if(lobbyAlreadyExistsError(message)){
            lobbyAlreadyExistsErrorMessage.postValue("A lobby with that name already exists! Try again.");
        } else {
            createLobbyLiveData.postValue(message);
        }
    }

    private boolean lobbyAlreadyExistsError(String message) {
        return message.startsWith("A lobby with the name:");
    }

    public void createLobby(Lobby lobby){
        if (isValidLobbyName(lobby.getName())) {
            webSocketClient.subscribeToTopic("/topic/create-lobby-response", this::messageReceivedFromServer);
            playerRepository = PlayerRepository.getInstance();
            lobbyApi.createLobby(lobby, playerRepository.getCurrentPlayer());
        } else {
            invalidLobbyNameErrorMessage.postValue("Invalid Lobby name!");
        }
    }

    public MutableLiveData<String> getMessageLiveData() {
        return messageLiveData;
    }
    public MutableLiveData<String> getMessageLiveDataListLobbies() {
        return messageLiveDataListLobbies;
    }

    public MutableLiveData<String> getLobbyAlreadyExistsErrorMessage() {
        return lobbyAlreadyExistsErrorMessage;
    }

    public MutableLiveData<String> getInvalidLobbyNameErrorMessage() {
        return invalidLobbyNameErrorMessage;
    }

    public MutableLiveData<String> getCreateLobbyLiveData() {
        return createLobbyLiveData;
    }

    private boolean isValidLobbyName(String lobbyName) {
        String regex = "^[a-zA-Z0-9]+(?:[_ -]?[a-zA-Z0-9]+)*$";
        return lobbyName.matches(regex);
    }

    public void fetchLobbies(LobbyRepository.LobbyListCallback lobbyListCallback) {
    }

    public interface LobbyListCallback {
        void onSuccess(List<Lobby> lobbyList);
        void onError(String errorMessage);
    }

}
