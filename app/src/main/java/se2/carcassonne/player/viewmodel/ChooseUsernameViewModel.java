package se2.carcassonne.player.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import se2.carcassonne.player.model.Player;
import se2.carcassonne.player.repository.PlayerRepository;

public class ChooseUsernameViewModel extends ViewModel {
    final PlayerRepository playerRepository;


    public ChooseUsernameViewModel(PlayerRepository playerRepository) {
        this.playerRepository = playerRepository;
    }

    public void createPlayer(Player player) {
        playerRepository.createUser(player);
    }

    // Getter for LiveData Observable
    public LiveData<String> getMessageLiveData() {
        return playerRepository.getMessageLiveData();
    }

    public LiveData<String> getInvalidUsernameErrorMessage() {
        return playerRepository.getInvalidUsernameErrorMessage();
    }

    public LiveData<String> getUserAlreadyExistsErrorMessage() {
        return playerRepository.getUserAlreadyExistsErrorMessage();
    }

    public String getPlayerName(String playerStringAsJson) {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode username = null;
        try {
            username = mapper.readTree(playerStringAsJson).get("username");
        } catch (JsonProcessingException e) {
            return null;
        }
        return username.asText();
    }


}
