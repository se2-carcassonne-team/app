package se2.carcassonne.repository;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;

import lombok.Getter;
import se2.carcassonne.api.GameSessionApi;
import se2.carcassonne.helper.network.WebSocketClient;
import se2.carcassonne.model.FinishedTurnDto;
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
    private Integer cheatPoints = 0;
    private final ObjectMapper objectMapper;
    private final MutableLiveData<Boolean> cheaterFound = new MutableLiveData<>(false);

    private final MutableLiveData<Boolean> iCanCheat = new MutableLiveData<>();

    private static final String TOPIC_GAMESESSION = "/topic/game-session-";

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
//        Draw next tile and get next player
        gameSessionApi.nextTurn(gameSessionId);
    }

    public void subscribeToNextTurn(Long gameSessionId) {
        webSocketClient.subscribeToTopic(TOPIC_GAMESESSION + gameSessionId + "/next-turn-response", this::getNextTurnMessageReceived);
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
        webSocketClient.subscribeToTopic(TOPIC_GAMESESSION + gameSessionId + "/tile", this::getPlacedTile);
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
        webSocketClient.subscribeToTopic(TOPIC_GAMESESSION + gameSessionId + "/game-finished", this::endGameMessageReceived);
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
        webSocketClient.subscribeToTopic(TOPIC_GAMESESSION + gameSessionId + "/points-meeples", this::getPointsForCompletedRoad);
    }

    private void getPointsForCompletedRoad(String message) {
        try {
            FinishedTurnDto finishedTurnDto = objectMapper.readValue(message, FinishedTurnDto.class);
            finishedTurnLiveData.postValue(finishedTurnDto);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to parse JSON message for points for completed roads", e);
        }
    }

    public void cheatPointsReceived(String message) {
        try {
            cheatPoints = objectMapper.readValue(message, Integer.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to parse JSON message for cheat points", e);
        }
    }

    public void sendCheatRequest(Long playerId, FinishedTurnDto finishedTurnDto) {
        gameSessionApi.sendCheatRequest(playerId, finishedTurnDto);
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


    /* This is the endpoint on the server:
        @MessageMapping("/cheat/can-i-cheat")
    @SendToUser("/queue/cheat-can-i-cheat")
    public String handleCanICheat(String playerIdString) {
        Long playerId = Long.parseLong(playerIdString);
        Boolean canCheat = cheatService.checkIsPlayerCheater(playerId);
        return canCheat.toString();
    }
     */

    // subscribe to the topic /queue/cheat-can-i-cheat to get the cheat points
    public void subscribeToCanICheat() {
        webSocketClient.subscribeToQueue("/user/queue/cheat-can-i-cheat", this::canICheatReceived);
    }

    private void canICheatReceived(String message) {
        try {
            iCanCheat.postValue(objectMapper.readValue(message, Boolean.class));
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to parse JSON message for cheat points", e);
        }
    }

    public void sendCanICheat(Long playerId) {
        gameSessionApi.sendCanICheat(playerId);
    }

    public MutableLiveData<Boolean> getICanCheat() {
        return iCanCheat;
    }

    public void sendAccuseRequest(Long myPlayerId, Long accusedPlayerId, FinishedTurnDto finishedTurnDto) {
        gameSessionApi.sendAccuseRequest(myPlayerId, accusedPlayerId, finishedTurnDto);
    }

    // endpoint on the server:
    //             this.template.convertAndSend("/topic/game-session-" + finishedTurnDto.getGameSessionId() + "/cheat-detected", objectMapper.writeValueAsString(true));
    public void subscribeToCheaterFound(Long gameSessionId) {
        webSocketClient.subscribeToTopic(TOPIC_GAMESESSION + gameSessionId + "/cheat-detected", this::cheaterFoundReceived);
    }

    private void cheaterFoundReceived(String s) {
        cheaterFound.postValue(true);
    }

    public MutableLiveData<Boolean> getCheaterFound() {
        return cheaterFound;
    }
}
