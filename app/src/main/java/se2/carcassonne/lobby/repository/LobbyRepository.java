package se2.carcassonne.lobby.repository;

import androidx.lifecycle.MutableLiveData;

import java.util.Objects;
import java.util.regex.Pattern;

import lombok.RequiredArgsConstructor;
import se2.carcassonne.helper.mapper.MapperHelper;
import se2.carcassonne.helper.network.WebSocketClient;
import se2.carcassonne.lobby.model.Lobby;
import se2.carcassonne.player.repository.PlayerRepository;

@RequiredArgsConstructor
public class LobbyRepository {
    private static LobbyRepository instance;
    private final WebSocketClient webSocketClient = WebSocketClient.getInstance();
    private final MutableLiveData<String> createLobbyLiveData = new MutableLiveData<>();
    private final MutableLiveData<String> lobbyAlreadyExistsErrorMessage = new MutableLiveData<>();
    private final MutableLiveData<String> invalidLobbyNameErrorMessage = new MutableLiveData<>();
    private final MutableLiveData<String> listAllLobbiesLiveData = new MutableLiveData<>();
    private final MutableLiveData<String> listAllPlayersLiveData = new MutableLiveData<>();
    private final MutableLiveData<String> playerJoinsOrLeavesLobbyLiveData = new MutableLiveData<>();
    private static final Pattern lobbyNamePattern = Pattern.compile("^[a-zA-Z0-9]+(?:[_ -]?[a-zA-Z0-9]+)*$");
    private final MapperHelper mapperHelper = new MapperHelper();
    private final LobbyApi lobbyApi;
    private final PlayerRepository playerRepository = PlayerRepository.getInstance();

    private LobbyRepository() {
        this.lobbyApi = new LobbyApi();
    }

    public static LobbyRepository getInstance() {
        if (instance == null) {
            instance = new LobbyRepository();
        }
        return instance;
    }

    public void createLobby(Lobby lobby){
        // TODO : ERROR HANDLING BASED ON CODES
        if (isValidLobbyName(lobby.getName())) {
            webSocketClient.subscribeToQueue("/user/queue/response", this::createLobbyLiveData);
            webSocketClient.subscribeToQueue("/user/queue/errors", this::createLobbyLiveData);
            // TODO : CHECK HERE
            lobbyApi.createLobby(lobby, playerRepository.getCurrentPlayer());
        } else {
            invalidLobbyNameErrorMessage.postValue("Invalid Lobby name!");
        }
    }

    private void createLobbyLiveData(String message) {
        // TODO : ERROR HANDLING BASED ON CODES
        webSocketClient.unsubscribeFromQueue("/user/queue/response");
        webSocketClient.unsubscribeFromQueue("/user/queue/errors");
        if(lobbyAlreadyExistsError(message)){
            lobbyAlreadyExistsErrorMessage.postValue("A lobby with that name already exists! Try again.");
        } else {
            PlayerRepository.getInstance().getCurrentPlayer().setGameLobbyId(mapperHelper.getIdFromLobbyStringAsLong(message));
            webSocketClient.subscribeToTopic("/topic/lobby-"+(mapperHelper.getIdFromLobbyString(message)), this::playerInLobbyReceivesJoinOrLeaveMessage);
            createLobbyLiveData.postValue(message);
        }
    }

    private void playerInLobbyReceivesJoinOrLeaveMessage(String message) {
        // TODO : DOES THIS MAKE SENSE?
        // If player already in lobby and notices a join, we just need to update the list of players
        // and not set the ID of the lobby again
        // STAY SUBSCRIBED FOR FUTURE UPDATES
        getPlayerJoinsOrLeavesLobbyLiveData().postValue(message);
    }

    public void getAllLobbies() {
        // TODO : ERROR HANDLING BASED ON CODES
        webSocketClient.subscribeToTopic("/topic/lobby-list", this::getAllLobbiesLiveData);
//        TESTING UNSUBSCRIBE
//        webSocketClient.unsubscribeFromTopic("/topic/lobby-list");

        webSocketClient.subscribeToQueue("user/queue/lobby-list-response", this::getAllLobbiesLiveData);
        webSocketClient.subscribeToQueue("/user/queue/errors", this::getAllLobbiesLiveData);
        lobbyApi.getAllLobbies();
    }

    private void getAllLobbiesLiveData(String message){
        // TODO : ERROR HANDLING BASED ON CODES
        // STAY SUBSCRIBED FOR FUTURE UPDATES
        webSocketClient.unsubscribeFromQueue("user/queue/lobby-list-response");
        webSocketClient.unsubscribeFromQueue("/user/queue/errors");
        if (!Objects.equals(message, "null")) {
            listAllLobbiesLiveData.postValue(message);
        } else {
            listAllLobbiesLiveData.postValue("No lobbies available");
        }
    }

    public void joinLobby(Lobby lobby){
        // TODO : ERROR HANDLING BASED ON CODES
        webSocketClient.subscribeToQueue("/user/queue/response", this::playerJoinsLobbyMessageReceived);
        webSocketClient.subscribeToQueue("/user/queue/errors", this::playerJoinsLobbyMessageReceived);
        webSocketClient.subscribeToTopic("/topic/lobby-"+lobby.getId(), this::playerInLobbyReceivesJoinOrLeaveMessage);
        lobbyApi.joinLobby(lobby.getId(), PlayerRepository.getInstance().getCurrentPlayer());
    }

    private void playerJoinsLobbyMessageReceived(String message) {
        // TODO : ERROR HANDLING BASED ON CODES
        webSocketClient.unsubscribeFromQueue("/user/queue/response");
        webSocketClient.unsubscribeFromQueue("/user/queue/errors");
        PlayerRepository.getInstance().getCurrentPlayer().setGameLobbyId(mapperHelper.getLobbyIdFromPlayer(message));
    }

    public void leaveLobby() {
        // TODO : ERROR HANDLING BASED ON CODES
        webSocketClient.subscribeToQueue("/user/queue/response", this::playerLeavesLobbyMessageReceived);
        webSocketClient.subscribeToQueue("/user/queue/errors", this::playerLeavesLobbyMessageReceived);
        lobbyApi.leaveLobby(PlayerRepository.getInstance().getCurrentPlayer());
    }

    private void playerLeavesLobbyMessageReceived(String message) {
        // TODO : ERROR HANDLING BASED ON CODES
        webSocketClient.unsubscribeFromQueue("/user/queue/response");
        webSocketClient.unsubscribeFromQueue("/user/queue/errors");
        webSocketClient.unsubscribeFromTopic("/topic/lobby-"+PlayerRepository.getInstance().getCurrentPlayer().getGameLobbyId());
        PlayerRepository.getInstance().getCurrentPlayer().setGameLobbyId(null);
    }

    public void getAllPlayers(Lobby lobby) {
        // TODO : ERROR HANDLING BASED ON CODES
        webSocketClient.subscribeToQueue("/user/queue/player-list-response", this::listAllPlayersReceivedFromServer);
        webSocketClient.subscribeToQueue("/user/queue/errors", this::listAllPlayersReceivedFromServer);
        lobbyApi.getAllPlayers(lobby.getId());
    }

    private void listAllPlayersReceivedFromServer(String message) {
        // TODO : ERROR HANDLING BASED ON CODES
        webSocketClient.unsubscribeFromQueue("/user/queue/player-list-response");
        webSocketClient.unsubscribeFromQueue("/user/queue/errors");
        if (!Objects.equals(message, "null")) {
            listAllPlayersLiveData.postValue(message);
        } else {
            listAllPlayersLiveData.postValue("No lobbies available");
        }
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

    public MutableLiveData<String> getCreateLobbyLiveData() {
        return createLobbyLiveData;
    }

    public MutableLiveData<String> getPlayerJoinsOrLeavesLobbyLiveData() {
        return playerJoinsOrLeavesLobbyLiveData;
    }

    private boolean isValidLobbyName(String lobbyName) {
        return lobbyNamePattern.matcher(lobbyName).matches();
    }
}
