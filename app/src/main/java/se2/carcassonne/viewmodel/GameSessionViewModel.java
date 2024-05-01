package se2.carcassonne.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import se2.carcassonne.model.NextTurn;
import se2.carcassonne.model.PlacedTileDto;
import se2.carcassonne.repository.GameSessionRepository;

public class GameSessionViewModel extends ViewModel {

    private final GameSessionRepository gameSessionRepository = GameSessionRepository.getInstance();

    public GameSessionViewModel() {
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

}
