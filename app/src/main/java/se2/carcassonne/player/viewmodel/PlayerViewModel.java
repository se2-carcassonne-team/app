package se2.carcassonne.player.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import se2.carcassonne.player.model.Player;
import se2.carcassonne.player.repository.PlayerRepository;

public class PlayerViewModel extends ViewModel {
    private final PlayerRepository playerRepository = PlayerRepository.getInstance();

    public PlayerViewModel() {
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
