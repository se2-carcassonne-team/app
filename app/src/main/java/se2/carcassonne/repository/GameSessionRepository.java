package se2.carcassonne.repository;

import androidx.lifecycle.MutableLiveData;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;

import lombok.Getter;

import lombok.Getter;
import se2.carcassonne.api.GameSessionApi;
import se2.carcassonne.helper.network.WebSocketClient;
import se2.carcassonne.model.FinishedTurnDto;
import se2.carcassonne.model.Meeple;
import se2.carcassonne.model.NextTurn;
import se2.carcassonne.model.PlacedTileDto;
import se2.carcassonne.model.Scoreboard;

@Getter
public class GameSessionRepository {

    private static GameSessionRepository instance;
    private NextTurn currentNextTurn;
    private final GameSessionApi gameSessionApi;
    private final WebSocketClient webSocketClient = WebSocketClient.getInstance();
    private final MutableLiveData<NextTurn> getNextTurnLiveData = new MutableLiveData<>();
    private final MutableLiveData<PlacedTileDto> placedTileLiveData = new MutableLiveData<>();
    private final MutableLiveData<List<Long>> allPlayersLiveData = new MutableLiveData<>();
    private final MutableLiveData<Boolean> gameEndedLiveData = new MutableLiveData<>();
    private final MutableLiveData<Scoreboard> scoreboardLiveData = new MutableLiveData<>();
    private final MutableLiveData<FinishedTurnDto> finishedTurnLiveData = new MutableLiveData<>();
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

    public void subscribeToNextTurn(Long gameSessionId) {
        webSocketClient.subscribeToTopic("/topic/game-session-" + gameSessionId + "/next-turn-response", this::getNextTurnMessageReceived);
    }

    public void subscribeToGetAllPlayersInLobby(Long gameLobbyId) {
        webSocketClient.subscribeToTopic("/topic/lobby-" + gameLobbyId + "/player-list", this::getAllPlayersInLobby);
    }

    public void subscribeToForwardedScoreboard(Long gameSessionId) {
        webSocketClient.subscribeToTopic("/topic/game-end-" + gameSessionId + "/scoreboard", this::getScoreboard);
    }

    private void getScoreboard(String message) {
        try {
            Scoreboard scoreboard = objectMapper.readValue(message, Scoreboard.class);
            scoreboardLiveData.postValue(scoreboard);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    private void getAllPlayersInLobby(String message) {
        try {
            List<Long> allPlayerIds = objectMapper.readValue(message, new TypeReference<List<Long>>() {
            });
            allPlayersLiveData.postValue(allPlayerIds);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    private void getNextTurnMessageReceived(String message) {
        try {
            currentNextTurn = objectMapper.readValue(message, NextTurn.class);
            getNextTurnLiveData.postValue(currentNextTurn);

        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public void sendPlacedTile(PlacedTileDto placedTileDto) {
        gameSessionApi.sendPlacedTile(placedTileDto);
    }

    public void forwardScoreboard(Scoreboard scoreboard) {
        gameSessionApi.forwardScoreboard(scoreboard);
    }

    public void subscribeToPlacedTile(Long gameSessionId) {
        webSocketClient.subscribeToTopic("/topic/game-session-" + gameSessionId + "/tile", this::getPlacedTile);
    }

    private void getPlacedTile(String message) {
        try {
            PlacedTileDto placedTileDto = objectMapper.readValue(message, PlacedTileDto.class);
            placedTileLiveData.postValue(placedTileDto);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public void subscribeToGameFinished(Long gameSessionId) {
        webSocketClient.subscribeToTopic("/topic/game-session-" + gameSessionId + "/game-finished", this::endGameMessageReceived);
    }

    private void endGameMessageReceived(String message) {
        if (message.equals("FINISHED")) {
            gameEndedLiveData.postValue(true);
        }
    }

    public void sendPointsForCompletedRoad(FinishedTurnDto finishedTurnDto) {
        gameSessionApi.sendPointsForCompletedRoad(finishedTurnDto);
    }

    public void subscribeToPointsForCompletedRoad(Long gameSessionId){
        webSocketClient.subscribeToTopic("/topic/game-session-" + gameSessionId + "/points-meeples", this::getPointsForCompletedRoad);
    }

    private void getPointsForCompletedRoad(String message) {
        try {
            FinishedTurnDto finishedTurnDto = objectMapper.readValue(message, FinishedTurnDto.class);
            finishedTurnLiveData.postValue(finishedTurnDto);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to parse JSON message for points for completed roads", e);
        }
    }

    /**
     * Returns the next turn live data object
     *
     * @return
     */
    public MutableLiveData<NextTurn> getNextTurnLiveData() {
        return getNextTurnLiveData;
    }


    public MutableLiveData<PlacedTileDto> getPlacedTileLiveData() {
        return placedTileLiveData;
    }



}
