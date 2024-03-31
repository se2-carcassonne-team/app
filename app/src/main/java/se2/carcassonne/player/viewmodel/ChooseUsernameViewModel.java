package se2.carcassonne.player.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

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

    public String getPlayerName(String playerStringAsJson) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode node = mapper.readTree(playerStringAsJson);

            // Extract the username field from the JSON object
            JsonNode usernameNode = node.get("username");
            if (usernameNode != null) {
                return usernameNode.asText();
            } else {
                return null; // Username field not found
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null; // Handle parsing error
        }
    }
}
