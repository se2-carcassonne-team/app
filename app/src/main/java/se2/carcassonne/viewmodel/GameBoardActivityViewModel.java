package se2.carcassonne.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import se2.carcassonne.repository.GameBoardRepository;

public class GameBoardActivityViewModel extends ViewModel {

    private final GameBoardRepository gameBoardRepository = GameBoardRepository.getInstance();

    public GameBoardActivityViewModel() {
    }

    public void getNextTurn(Long gameSessionId) {
        gameBoardRepository.getNextTurn(gameSessionId);
    }

    /**
     * Calls the repository to get the current next turn
     */
    public void getCurrentNextTurn() {
        gameBoardRepository.getCurrentNextTurn();
    }

    public LiveData<String> getNextTurnMessageLiveData() {
        return gameBoardRepository.getNextTurnLiveData();
    }



}
