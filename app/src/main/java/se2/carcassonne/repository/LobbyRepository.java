package se2.carcassonne.repository;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import java.util.Objects;
import java.util.regex.Pattern;

import lombok.RequiredArgsConstructor;
import se2.carcassonne.api.LobbyApi;
import se2.carcassonne.helper.mapper.MapperHelper;
import se2.carcassonne.helper.network.WebSocketClient;
import se2.carcassonne.model.Lobby;

@RequiredArgsConstructor
public class LobbyRepository {
    private static LobbyRepository instance;
    private final WebSocketClient webSocketClient = WebSocketClient.getInstance();
    private final MutableLiveData<String> updatedPlayerLiveData = new MutableLiveData<>();
    private final MutableLiveData<String> createLobbyLiveData = new MutableLiveData<>();
    private final MutableLiveData<String> lobbyAlreadyExistsErrorMessage = new MutableLiveData<>();
    private final MutableLiveData<String> invalidLobbyNameErrorMessage = new MutableLiveData<>();
    private final MutableLiveData<String> listAllLobbiesLiveData = new MutableLiveData<>();
    private final MutableLiveData<String> listAllPlayersLiveData = new MutableLiveData<>();
    private final MutableLiveData<String> playerJoinsOrLeavesLobbyLiveData = new MutableLiveData<>();
    private final MutableLiveData<String> playerLeavesLobbyLiveData = new MutableLiveData<>();
    private final MutableLiveData<String> playerInLobbyReceivesUpdatedLobbyLiveData = new MutableLiveData<>();
    private final MutableLiveData<String> gameToBeStartedLiveData = new MutableLiveData<>();

    private static final String QUEUE_RESPONSE = "/user/queue/response";
    private static final String QUEUE_ERRORS = "/user/queue/errors";
    private static final String TOPIC_LOBBY = "/topic/lobby-";

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

    /**
     * Subscribe to:
     * <p>- /user/queue/response -> Return Value = Created Lobby with updated ID</p>
     * <p>- /user/queue/errors -> Return Value = ErrorMessage</p>
     * <p>- /topic/lobby-$lobbyId-creator -> Return Value = Updated Player with Colour</p>
     *
     * @param lobby Lobby to create
     */
    public void createLobby(Lobby lobby){
        // TODO : ERROR HANDLING BASED ON CODES
        if (isValidLobbyName(lobby.getName())) {
            webSocketClient.subscribeToQueue(QUEUE_RESPONSE, this::createLobbyLiveData);
            webSocketClient.subscribeToQueue(QUEUE_ERRORS, this::createLobbyLiveData);

            // TODO: Add gamelobbyId in the future
            webSocketClient.subscribeToTopic("/topic/lobby-creator", this::updatePlayerAfterLobbyCreation);

            // TODO : CHECK HERE
            lobbyApi.createLobby(lobby, playerRepository.getCurrentPlayer());
        } else {
            invalidLobbyNameErrorMessage.postValue("Invalid Lobby name!");
        }
    }

    /**
     * Used to get the assigned player colour from the server when creating a lobby
     *
     * Unsubscribe from:
     * <p>- /topic/lobby-$lobbyId-creator</p>
     *
     * @param message Updated Player
     */
    private void updatePlayerAfterLobbyCreation(String message) {
        Log.d("response", message);
        PlayerRepository.getInstance().getCurrentPlayer().setPlayerColour(mapperHelper.getPlayerColour(message));
        webSocketClient.unsubscribe("/topic/lobby-creator");
    }

    /**
     * Unsubscribe from:
     * <p>- /topic/lobby-<b>lobbyIdValue</b></p>
     * <p>- /user/queue/response</p>
     *
     * Su
     * @param message Received Response from Server when creating Lobby
     */
    private void createLobbyLiveData(String message) {

        // TODO : ERROR HANDLING BASED ON CODES
        webSocketClient.unsubscribe(QUEUE_RESPONSE);
        webSocketClient.unsubscribe(QUEUE_ERRORS);
        if(lobbyAlreadyExistsError(message)){
            lobbyAlreadyExistsErrorMessage.postValue("A lobby with that name already exists! Try again.");
        } else {
            PlayerRepository.getInstance().getCurrentPlayer().setGameLobbyId(mapperHelper.getIdFromLobbyStringAsLong(message));
            webSocketClient.subscribeToTopic(TOPIC_LOBBY+(mapperHelper.getIdFromLobbyString(message)), this::playerInLobbyReceivesJoinOrLeaveMessage);
            webSocketClient.subscribeToTopic(TOPIC_LOBBY+(mapperHelper.getIdFromLobbyString(message))+"/update", this::playerInLobbyReceivesUpdatedLobbyMessage);
            webSocketClient.subscribeToTopic(TOPIC_LOBBY+(mapperHelper.getIdFromLobbyString(message))+"/game-start", this::playerReceivesGameStartMessage);
            createLobbyLiveData.postValue(message);
        }
    }

    private void playerReceivesGameStartMessage(String message) {
        gameToBeStartedLiveData.postValue(message);
    }

    private void playerInLobbyReceivesUpdatedLobbyMessage(String message) {
        if (Objects.equals(mapperHelper.getLobbyAdminIdFromLobbyString(message), PlayerRepository.getInstance().getCurrentPlayer().getId())
        && mapperHelper.getNumOfPlayersFromLobby(message) >= 2) {
            getPlayerInLobbyReceivesUpdatedLobbyLiveData().postValue(message);
        } else {
            getPlayerInLobbyReceivesUpdatedLobbyLiveData().postValue("RESET|" + message);
        }
    }

    private void playerInLobbyReceivesJoinOrLeaveMessage(String message) {
        // TODO : DOES THIS MAKE SENSE?
        getPlayerJoinsOrLeavesLobbyLiveData().postValue(message);
    }

    public void getAllLobbies() {
        // TODO : ERROR HANDLING BASED ON CODES
        webSocketClient.subscribeToTopic("/topic/lobby-list", this::getAllLobbiesLiveData);
        webSocketClient.subscribeToQueue("/user/queue/lobby-list-response", this::getAllLobbiesLiveData);
        webSocketClient.subscribeToQueue(QUEUE_ERRORS, this::getAllLobbiesLiveData);
        lobbyApi.getAllLobbies();
    }

    private void getAllLobbiesLiveData(String message){
        // TODO : ERROR HANDLING BASED ON CODES
        // STAY SUBSCRIBED FOR FUTURE UPDATES
        webSocketClient.unsubscribe("user/queue/lobby-list-response");
        webSocketClient.unsubscribe(QUEUE_ERRORS);
        if (!Objects.equals(message, "null")) {
            listAllLobbiesLiveData.postValue(message);
        } else {
            listAllLobbiesLiveData.postValue("No lobbies available");
        }
    }

    public void joinLobby(Lobby lobby){
        // TODO : ERROR HANDLING BASED ON CODES
        webSocketClient.subscribeToQueue(QUEUE_RESPONSE, this::playerJoinsLobbyMessageReceived);
        webSocketClient.subscribeToQueue(QUEUE_ERRORS, this::playerJoinsLobbyMessageReceived);
        webSocketClient.subscribeToTopic(TOPIC_LOBBY+lobby.getId(), this::playerInLobbyReceivesJoinOrLeaveMessage);
        webSocketClient.subscribeToTopic(TOPIC_LOBBY+lobby.getId()+"/update", this::playerInLobbyReceivesUpdatedLobbyMessage);
        webSocketClient.subscribeToTopic(TOPIC_LOBBY+lobby.getId()+"/game-start", this::playerReceivesGameStartMessage);
        lobbyApi.joinLobby(lobby.getId(), PlayerRepository.getInstance().getCurrentPlayer());
    }

    private void playerJoinsLobbyMessageReceived(String message) {
        // TODO : ERROR HANDLING BASED ON CODES
        webSocketClient.unsubscribe(QUEUE_RESPONSE);
        webSocketClient.unsubscribe(QUEUE_ERRORS);
        PlayerRepository.getInstance().getCurrentPlayer().setGameLobbyId(mapperHelper.getLobbyIdFromPlayer(message));
        PlayerRepository.getInstance().getCurrentPlayer().setPlayerColour(mapperHelper.getPlayerColour(message));
    }

    public void leaveLobby() {
        // TODO : ERROR HANDLING BASED ON CODES
        webSocketClient.subscribeToQueue(QUEUE_RESPONSE, this::playerLeavesLobbyMessageReceived);
        webSocketClient.subscribeToQueue(QUEUE_ERRORS, this::playerLeavesLobbyMessageReceived);
        lobbyApi.leaveLobby(PlayerRepository.getInstance().getCurrentPlayer());
    }

    private void playerLeavesLobbyMessageReceived(String message) {
        // TODO : ERROR HANDLING BASED ON CODES
        webSocketClient.unsubscribe(QUEUE_RESPONSE);
        webSocketClient.unsubscribe(QUEUE_ERRORS);
        webSocketClient.unsubscribe(TOPIC_LOBBY+PlayerRepository.getInstance().getCurrentPlayer().getGameLobbyId());
        webSocketClient.unsubscribe(TOPIC_LOBBY+(mapperHelper.getIdFromLobbyString(message))+"/update");
        webSocketClient.unsubscribe(TOPIC_LOBBY+(mapperHelper.getIdFromLobbyString(message))+"/game-start");
        PlayerRepository.getInstance().getCurrentPlayer().setGameLobbyId(null);
        playerLeavesLobbyLiveData.postValue(message);
    }

    public void getAllPlayers(Lobby lobby) {
        // TODO : ERROR HANDLING BASED ON CODES
        webSocketClient.subscribeToQueue("/user/queue/player-list-response", this::listAllPlayersReceivedFromServer);
        webSocketClient.subscribeToQueue(QUEUE_ERRORS, this::listAllPlayersReceivedFromServer);
        lobbyApi.getAllPlayers(lobby.getId());
    }

    private void listAllPlayersReceivedFromServer(String message) {
        // TODO : ERROR HANDLING BASED ON CODES
        webSocketClient.unsubscribe("/user/queue/player-list-response");
        webSocketClient.unsubscribe(QUEUE_ERRORS);
        if (!Objects.equals(message, "null")) {
            listAllPlayersLiveData.postValue(message);
        } else {
            listAllPlayersLiveData.postValue("No lobbies available");
        }
    }

    public void startGame(Long gameLobbyId) {
        lobbyApi.startGame(gameLobbyId);
    }

    private boolean lobbyAlreadyExistsError(String message) {
        return message.startsWith("ERROR: 1002");
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

    public MutableLiveData<String> getPlayerLeavesLobbyLiveData() {
        return playerLeavesLobbyLiveData;
    }

    public MutableLiveData<String> getPlayerInLobbyReceivesUpdatedLobbyLiveData() {
        return playerInLobbyReceivesUpdatedLobbyLiveData;
    }

    public MutableLiveData<String> getGameToBeStartedLiveData() {
        return gameToBeStartedLiveData;
    }

    private boolean isValidLobbyName(String lobbyName) {
        return lobbyNamePattern.matcher(lobbyName).matches();
    }
}
