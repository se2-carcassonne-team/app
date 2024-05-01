package se2.carcassonne.repository;

import androidx.lifecycle.MutableLiveData;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import se2.carcassonne.api.GameSessionApi;
import se2.carcassonne.helper.network.WebSocketClient;
import se2.carcassonne.model.NextTurn;
import se2.carcassonne.model.PlacedTileDto;

public class GameSessionRepository {

    private static GameSessionRepository instance;
    private NextTurn currentNextTurn;
    private final GameSessionApi gameSessionApi;
    private final WebSocketClient webSocketClient = WebSocketClient.getInstance();
    private final MutableLiveData<NextTurn> getNextTurnLiveData = new MutableLiveData<>();
    private final MutableLiveData<PlacedTileDto> placedTileLiveData = new MutableLiveData<>();
    private final ObjectMapper objectMapper;

    private GameSessionRepository() {
        this.gameSessionApi = new GameSessionApi();
        this.objectMapper = new ObjectMapper();
    }

    public static GameSessionRepository getInstance() {
        if (instance == null) {
            instance = new GameSessionRepository();
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
//        Draw next tile and get next player
        gameSessionApi.nextTurn(gameSessionId);
    }

    public void subscribeToNextTurn(Long gameSessionId){
        webSocketClient.subscribeToTopic("/topic/game-session-" + gameSessionId + "/next-turn-response", this::getNextTurnMessageReceived);
    }

    private void getNextTurnMessageReceived(String message) {
        try {
            currentNextTurn = objectMapper.readValue(message, NextTurn.class);
            getNextTurnLiveData.postValue(currentNextTurn);

        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public void sendPlacedTile(PlacedTileDto placedTileDto){
        gameSessionApi.sendPlacedTile(placedTileDto);
    }

    public void subscribeToPlacedTile(Long gameSessionId) {
        webSocketClient.subscribeToTopic("/topic/game-session-"+gameSessionId+"/tile", this::getPlacedTile );
        //                 "/topic/game-session-" + placedTileDto.getGameSessionId() + "/tile",
    }

    private void getPlacedTile(String message){
        try {
            PlacedTileDto placedTileDto = objectMapper.readValue(message, PlacedTileDto.class);
            placedTileLiveData.postValue(placedTileDto);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public NextTurn getCurrentNextTurn() {
        return currentNextTurn;
    }

    /**
     * Returns the next turn live data object
     * @return
     */
    public MutableLiveData<NextTurn> getNextTurnLiveData() {
        return getNextTurnLiveData;
    }


    public MutableLiveData<PlacedTileDto> getPlacedTileLiveData() {
        return placedTileLiveData;
    }
}
