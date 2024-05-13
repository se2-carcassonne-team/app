package se2.carcassonne.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import se2.carcassonne.model.Player;
import se2.carcassonne.repository.PlayerRepository;

public class PlayerViewModel extends ViewModel {
    private final PlayerRepository playerRepository;

    public PlayerViewModel() {
        playerRepository = PlayerRepository.getInstance();
    }

    public void createPlayer(Player player) {
        playerRepository.createPlayer(player);
    }

    public LiveData<String> getMessageLiveData() {
        return playerRepository.getCreatePlayerLiveData();
    }

    public LiveData<String> getInvalidUsernameErrorMessage() {
        return playerRepository.getInvalidUsernameErrorMessage();
    }

    public LiveData<String> getUserAlreadyExistsErrorMessage() {
        return playerRepository.getUserAlreadyExistsErrorMessage();
    }
}
