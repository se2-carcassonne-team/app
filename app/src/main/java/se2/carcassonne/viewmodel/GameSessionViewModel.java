package se2.carcassonne.viewmodel;

import androidx.compose.runtime.snapshots.Snapshot;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

import se2.carcassonne.model.FinishedTurnDto;
import se2.carcassonne.model.NextTurn;
import se2.carcassonne.model.PlacedTileDto;
import se2.carcassonne.model.Scoreboard;
import se2.carcassonne.repository.GameSessionRepository;

public class GameSessionViewModel extends ViewModel {

    private final GameSessionRepository gameSessionRepository;

    public GameSessionViewModel() {
        gameSessionRepository = GameSessionRepository.getInstance();
    }

    public void getNextTurn(Long gameSessionId) {
        gameSessionRepository.getNextTurn(gameSessionId);
    }

    public Integer getCheatPoints() {
        return gameSessionRepository.getCheatPoints();
    }

    public MutableLiveData<NextTurn> getNextTurnMessageLiveData() {
        return gameSessionRepository.getNextTurnLiveData();
    }

    public MutableLiveData<PlacedTileDto> getPlacedTileLiveData(){
        return gameSessionRepository.getPlacedTileLiveData();
    }

    public void sendPlacedTile(PlacedTileDto placedTileDto) {
        gameSessionRepository.sendPlacedTile(placedTileDto);
    }

    public MutableLiveData<Boolean> gameEndedLiveData(){
        return gameSessionRepository.getGameEndedLiveData();
    }

    public void sendGetAllPlayersInLobby(Long gameLobbyId){
        gameSessionRepository.subscribeToGetAllPlayersInLobby(gameLobbyId);
    }

    public MutableLiveData<List<Long>> allPlayersInLobbyLiveData(){
        return gameSessionRepository.getAllPlayersLiveData();
    }

    public void sendScoreboardRequest(Scoreboard scoreboard){
        gameSessionRepository.forwardScoreboard(scoreboard);
    }

    public void sendCheatRequest(Long playerId, FinishedTurnDto finishedTurnDto) {
        gameSessionRepository.sendCheatRequest(playerId, finishedTurnDto);
    }

    public MutableLiveData<Scoreboard> scoreboardLiveData(){
        return gameSessionRepository.getScoreboardLiveData();
    }

    public MutableLiveData<FinishedTurnDto> finishedTurnLiveData(){
        return gameSessionRepository.getFinishedTurnLiveData();
    }

    public void sendPointsForCompletedRoad(FinishedTurnDto finishedTurnDto) {
        gameSessionRepository.sendPointsForCompletedRoad(finishedTurnDto);
    }

    public void sendCanICheat(Long playerId) {
        gameSessionRepository.sendCanICheat(playerId);
    }


    public MutableLiveData<Boolean> getICanCheat() {
        return gameSessionRepository.getICanCheat();
    }

    public void sendAccuseRequest(Long myPlayerId, Long accusedPlayerId, FinishedTurnDto finishedTurnDto){
        gameSessionRepository.sendAccuseRequest(myPlayerId, accusedPlayerId, finishedTurnDto);
    }

    public MutableLiveData<Boolean> getCheaterFound(){
        return gameSessionRepository.getCheaterFound();
    }
}
