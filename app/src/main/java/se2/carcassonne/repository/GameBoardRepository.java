package se2.carcassonne.repository;

import androidx.lifecycle.MutableLiveData;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import se2.carcassonne.api.NextTurnApi;
import se2.carcassonne.helper.network.WebSocketClient;
import se2.carcassonne.model.NextTurn;

public class GameBoardRepository {

    private static GameBoardRepository instance;
    private NextTurn currentNextTurn;
    private final NextTurnApi nextTurnApi;
    private final WebSocketClient webSocketClient = WebSocketClient.getInstance();
    private final MutableLiveData<String> getNextTurnLiveData = new MutableLiveData<>();

    private GameBoardRepository() {
        this.nextTurnApi = new NextTurnApi();
    }

    public static GameBoardRepository getInstance() {
        if (instance == null) {
            instance = new GameBoardRepository();
        }
        return instance;
    }

    /**
     * Calls the repository to get the current next turn
     * Subscribes to the topic /topic/game-session-{gameSessionId}/next-turn-response to get the next player and next card
     * posts value to the live data object
     */
    public void getNextTurn(Long gameSessionId) {
//        TODO check if already subscribed
        webSocketClient.subscribeToTopic("/topic/game-session-" + gameSessionId + "/next-turn-response", this::getNextTurnMessageReceived);
//        Draw next tile and get next player
        nextTurnApi.nextTurn(gameSessionId);
    }

    private void getNextTurnMessageReceived(String message) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            currentNextTurn = mapper.readValue(message, NextTurn.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        getNextTurnLiveData.postValue(message);
    }

    public NextTurn getCurrentNextTurn() {
        return currentNextTurn;
    }

    /**
     * Returns the next turn live data object
     * @return
     */
    public MutableLiveData<String> getNextTurnLiveData() {
        return getNextTurnLiveData;
    }

}
