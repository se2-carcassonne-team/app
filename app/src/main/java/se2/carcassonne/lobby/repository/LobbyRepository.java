package se2.carcassonne.lobby.repository;

import androidx.lifecycle.MutableLiveData;

import java.util.Objects;

import lombok.RequiredArgsConstructor;
import se2.carcassonne.helper.network.WebSocketClient;
import se2.carcassonne.lobby.model.Lobby;
import se2.carcassonne.player.model.Player;
import se2.carcassonne.player.repository.PlayerRepository;

@RequiredArgsConstructor
public class LobbyRepository {
    private final WebSocketClient webSocketClient;
    private final MutableLiveData<String> createLobbyLiveData = new MutableLiveData<>();
    private final MutableLiveData<String> lobbyAlreadyExistsErrorMessage = new MutableLiveData<>();
    private final MutableLiveData<String> invalidLobbyNameErrorMessage = new MutableLiveData<>();
    private final MutableLiveData<String> listAllLobbiesLiveData = new MutableLiveData<>();
    private final MutableLiveData<String> playerJoinsLobbyLiveData = new MutableLiveData<>();

    private final LobbyApi lobbyApi;
    private PlayerRepository playerRepository;

    public LobbyRepository(PlayerRepository playerRepository) {
        this.webSocketClient = new WebSocketClient();
        this.playerRepository = playerRepository;
        this.lobbyApi = new LobbyApi(this.webSocketClient);
    }

    public void connectToWebSocketServer() {
        webSocketClient.connect(this::createLobbyLiveData);
    }

    private void createLobbyLiveData(String message) {
        if(lobbyAlreadyExistsError(message)){
            lobbyAlreadyExistsErrorMessage.postValue("A lobby with that name already exists! Try again.");
        } else {
            createLobbyLiveData.postValue(message);
        }
    }

    private void listAllLobbiesReceivedFromServer(String message){
        if (!Objects.equals(message, "null")) {
            listAllLobbiesLiveData.postValue(message);
        } else {
            listAllLobbiesLiveData.postValue("No lobbies available");
        }
    }

    private void playerJoinsLobbyMessageReceived(String message) {
        playerJoinsLobbyLiveData.postValue(message);
    }

    private boolean lobbyAlreadyExistsError(String message) {
        return message.startsWith("A lobby with the name:");
    }

    public void createLobby(Lobby lobby){
        if (isValidLobbyName(lobby.getName())) {
            webSocketClient.subscribeToTopic("/topic/lobby-create", this::createLobbyLiveData);
            playerRepository = PlayerRepository.getInstance();
            lobbyApi.createLobby(lobby, playerRepository.getCurrentPlayer());
        } else {
            invalidLobbyNameErrorMessage.postValue("Invalid Lobby name!");
        }
    }

    public void joinLobby(Lobby lobby){
        webSocketClient.subscribeToTopic("/topic/player-lobby-response", this::playerJoinsLobbyMessageReceived);
        webSocketClient.subscribeToQueue("/user/queue/errors", this::playerJoinsLobbyMessageReceived);
        playerRepository = PlayerRepository.getInstance();
        lobbyApi.joinLobby(lobby, playerRepository.getCurrentPlayer());
    }

    public void getAllLobbies() {
        webSocketClient.subscribeToQueue("/user/queue/lobby-response", this::listAllLobbiesReceivedFromServer);
        webSocketClient.subscribeToQueue("/user/queue/errors", this::listAllLobbiesReceivedFromServer);
        lobbyApi.getAllLobbies();
    }

    public void leaveLobby(Player player) {
        //webSocketClient.subscribeToQueue("/user/queue/lobby-response", this::listAllLobbiesReceivedFromServer);
        //webSocketClient.subscribeToQueue("/user/queue/errors", this::listAllLobbiesReceivedFromServer);
        //lobbyApi.leaveLobby(player);
    }

    public MutableLiveData<String> getListAllLobbiesLiveData() {
        return listAllLobbiesLiveData;
    }

    public MutableLiveData<String> getLobbyAlreadyExistsErrorMessage() {
        return lobbyAlreadyExistsErrorMessage;
    }

    public MutableLiveData<String> getInvalidLobbyNameErrorMessage() {
        return invalidLobbyNameErrorMessage;
    }

    public MutableLiveData<String> getPlayerJoinsLobbyLiveData() {
        webSocketClient.subscribeToTopic("/topic/player-lobby-response", this::playerJoinsLobbyMessageReceived);
        webSocketClient.subscribeToQueue("/user/queue/errors", this::playerJoinsLobbyMessageReceived);
        return playerJoinsLobbyLiveData;
    }

    public MutableLiveData<String> getCreateLobbyLiveData() {
        webSocketClient.subscribeToTopic("/topic/lobby-create", this::createLobbyLiveData);
        return createLobbyLiveData;
    }

    private boolean isValidLobbyName(String lobbyName) {
        String regex = "^[a-zA-Z0-9]+(?:[_ -]?[a-zA-Z0-9]+)*$";
        return lobbyName.matches(regex);
    }
}
