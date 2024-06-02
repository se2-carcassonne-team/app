package se2.carcassonne.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

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

    public LiveData<NextTurn> getNextTurnMessageLiveData() {
        return gameSessionRepository.getNextTurnLiveData();
    }

    public LiveData<PlacedTileDto> getPlacedTileLiveData(){
        return gameSessionRepository.getPlacedTileLiveData();
    }

    public void sendPlacedTile(PlacedTileDto placedTileDto) {
        gameSessionRepository.sendPlacedTile(placedTileDto);
    }

    public LiveData<Boolean> gameEndedLiveData(){
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

    public LiveData<Scoreboard> scoreboardLiveData(){
        return gameSessionRepository.getScoreboardLiveData();
    }

}
