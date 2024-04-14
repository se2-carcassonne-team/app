package se2.carcassonne.lobby.repository;

import androidx.lifecycle.MutableLiveData;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Objects;
import java.util.regex.Pattern;

import lombok.RequiredArgsConstructor;
import se2.carcassonne.helper.mapper.MapperHelper;
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
    private final MutableLiveData<String> listAllPlayersLiveData = new MutableLiveData<>();
    private final MutableLiveData<String> playerJoinsLobbyLiveData = new MutableLiveData<>();
    private final MutableLiveData<String> playerLeavesLobbyLiveData = new MutableLiveData<>();
    private static final Pattern lobbyNamePattern = Pattern.compile("^[a-zA-Z0-9]+(?:[_ -]?[a-zA-Z0-9]+)*$");
    private final MapperHelper mapperHelper = new MapperHelper();

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

    public void createLobby(Lobby lobby){
        if (isValidLobbyName(lobby.getName())) {
            webSocketClient.subscribeToTopic("/topic/game-lobby-response", this::createLobbyLiveData);
            webSocketClient.subscribeToQueue("/user/queue/errors", this::createLobbyLiveData);
            playerRepository = PlayerRepository.getInstance();
            lobbyApi.createLobby(lobby, playerRepository.getCurrentPlayer());
        } else {
            invalidLobbyNameErrorMessage.postValue("Invalid Lobby name!");
        }
    }

    public void getAllLobbies() {
        webSocketClient.subscribeToTopic("/topic/lobby-response", this::listAllLobbiesReceivedFromServer);
        webSocketClient.subscribeToQueue("/user/queue/errors", this::listAllLobbiesReceivedFromServer);
        lobbyApi.getAllLobbies();
    }

    public void getAllPlayers(Lobby lobby) {
        webSocketClient.subscribeToQueue("/user/queue/player-response", this::listAllPlayersReceivedFromServer);
        webSocketClient.subscribeToQueue("/user/queue/errors", this::listAllPlayersReceivedFromServer);
        lobbyApi.getAllPlayers(lobby.getId());
    }

    public void leaveLobby() {
        webSocketClient.subscribeToTopic(
                "/topic/player-leave-lobby-"+PlayerRepository.getInstance().getCurrentPlayer().getGameLobbyId(),
                this::playerLeavesLobbyMessageReceived);
        lobbyApi.leaveLobby(PlayerRepository.getInstance().getCurrentPlayer());
    }

    public void joinLobby(Lobby lobby){
        webSocketClient.subscribeToTopic("/topic/player-join-lobby-"+lobby.getId(), this::playerJoinsLobbyMessageReceived);
        lobbyApi.joinLobby(lobby.getId(), PlayerRepository.getInstance().getCurrentPlayer());
    }

    private void createLobbyLiveData(String message) {
        if(lobbyAlreadyExistsError(message)){
            lobbyAlreadyExistsErrorMessage.postValue("A lobby with that name already exists! Try again.");
        } else {
            createLobbyLiveData.postValue(message);
            PlayerRepository.getInstance().getCurrentPlayer().setGameLobbyId(mapperHelper.getIdFromLobbyStringAsLong(message));
        }
    }

    private void listAllLobbiesReceivedFromServer(String message){
        if (!Objects.equals(message, "null")) {
            System.out.println("RECEIVED LOBBIES: " + message);
            listAllLobbiesLiveData.postValue(message);
        } else {
            listAllLobbiesLiveData.postValue("No lobbies available");
        }
    }

    private void listAllPlayersReceivedFromServer(String message) {
        if (!Objects.equals(message, "null")) {
            listAllPlayersLiveData.postValue(message);
        } else {
            listAllPlayersLiveData.postValue("No lobbies available");
        }
    }

    private void playerLeavesLobbyMessageReceived(String message) {
        playerLeavesLobbyLiveData.postValue(message);
        lobbyApi.getAllLobbies();
    }

    private void playerJoinsLobbyMessageReceived(String message) {
        playerJoinsLobbyLiveData.postValue(message);
        PlayerRepository.getInstance().getCurrentPlayer().setGameLobbyId(mapperHelper.getLobbyIdFromPlayer(message));
        lobbyApi.getAllLobbies();
    }

    private boolean lobbyAlreadyExistsError(String message) {
        return message.startsWith("ERROR: gameLobby with name");
    }

    public MutableLiveData<String> getListAllLobbiesLiveData() {
        return listAllLobbiesLiveData;
    }
    public MutableLiveData<String> getListAllPlayersLiveData() {
        return listAllPlayersLiveData;
    }

    public MutableLiveData<String> getLobbyAlreadyExistsErrorMessage() {
        return lobbyAlreadyExistsErrorMessage;
    }

    public MutableLiveData<String> getInvalidLobbyNameErrorMessage() {
        return invalidLobbyNameErrorMessage;
    }

    public MutableLiveData<String> getPlayerJoinsLobbyLiveData() {
        webSocketClient.subscribeToTopic("/topic/player-join-lobby-"+playerRepository.getCurrentPlayer().getGameLobbyId(), this::playerJoinsLobbyMessageReceived);
        webSocketClient.subscribeToQueue("/user/queue/errors", this::playerJoinsLobbyMessageReceived);
        return playerJoinsLobbyLiveData;
    }

    public MutableLiveData<String> getCreateLobbyLiveData() {
        webSocketClient.subscribeToTopic("/topic/game-lobby-response", this::createLobbyLiveData);
        return createLobbyLiveData;
    }

    public MutableLiveData<String> getPlayerLeavesLobbyLiveData() {
        webSocketClient.subscribeToTopic("/topic/player-leave-lobby-"+playerRepository.getCurrentPlayer().getGameLobbyId(), this::playerLeavesLobbyMessageReceived);
        return playerLeavesLobbyLiveData;
    }

    private boolean isValidLobbyName(String lobbyName) {
        return lobbyNamePattern.matcher(lobbyName).matches();
    }
}
