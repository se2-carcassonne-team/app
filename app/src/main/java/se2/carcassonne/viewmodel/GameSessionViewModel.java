package se2.carcassonne.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

import java.util.List;
import java.util.Map;

import se2.carcassonne.model.FinishedTurnDto;
import se2.carcassonne.model.Meeple;
import se2.carcassonne.model.NextTurn;
import se2.carcassonne.model.PlacedTileDto;
import se2.carcassonne.model.Scoreboard;
import se2.carcassonne.repository.GameSessionRepository;
import se2.carcassonne.model.Player;

public class GameSessionViewModel extends ViewModel {

    private final GameSessionRepository gameSessionRepository;

    public GameSessionViewModel() {
        gameSessionRepository = GameSessionRepository.getInstance();
    }
    public void leavegamesession(Player player){
        gameSessionRepository.leavegamesession(player);
    }
    public void getNextTurn(Long gameSessionId) {
        gameSessionRepository.getNextTurn(gameSessionId);
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

    public MutableLiveData<Scoreboard> scoreboardLiveData(){
        return gameSessionRepository.getScoreboardLiveData();
    }

    public MutableLiveData<FinishedTurnDto> finishedTurnLiveData(){
        return gameSessionRepository.getFinishedTurnLiveData();
    }

    public void sendPointsForCompletedRoad(FinishedTurnDto finishedTurnDto) {
        gameSessionRepository.sendPointsForCompletedRoad(finishedTurnDto);
    }

}
