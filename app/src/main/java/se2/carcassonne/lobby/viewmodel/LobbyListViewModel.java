package se2.carcassonne.lobby.viewmodel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import se2.carcassonne.lobby.model.Lobby;
import se2.carcassonne.lobby.repository.LobbyRepository;
import se2.carcassonne.player.model.Player;
import se2.carcassonne.player.repository.PlayerRepository;


public class LobbyListViewModel extends ViewModel {
    private final LobbyRepository lobbyRepository;

    public LobbyListViewModel(LobbyRepository lobbyRepository) {
        this.lobbyRepository = lobbyRepository;
    }

    public MutableLiveData<String> getMessageLiveDataListLobbies() {
        return lobbyRepository.getListAllLobbiesLiveData();
    }

    public MutableLiveData<String> getLobbyAlreadyExistsErrorMessage() {
        return lobbyRepository.getLobbyAlreadyExistsErrorMessage();
    }

    public MutableLiveData<String> getInvalidLobbyNameErrorMessage() {
        return lobbyRepository.getInvalidLobbyNameErrorMessage();
    }

    public MutableLiveData<String> getCreateLobbyLiveData() {
        return lobbyRepository.getCreateLobbyLiveData();
    }

    public MutableLiveData<String> getPlayerJoinsLobbyLiveData() {
        return lobbyRepository.getPlayerJoinsLobbyLiveData();
    }
    public MutableLiveData<String> getPlayerLeavesLobbyLiveData() {
        return lobbyRepository.getPlayerLeavesLobbyLiveData();
    }

    public void createLobby(Lobby lobby) {
        lobbyRepository.createLobby(lobby);
    }

    public void getAllLobbies() {
        lobbyRepository.getAllLobbies();
    }

    public void joinLobby(Lobby lobby) {
        lobbyRepository.joinLobby(lobby);
    }

    public void leaveLobby() {
        lobbyRepository.leaveLobby();
    }

    public String getLobbyName(String lobbyStringAsJson) {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode lobbyName = null;
        try {
            lobbyName = mapper.readTree(lobbyStringAsJson).get("name");
        } catch (JsonProcessingException e) {
            return null;
        }
        return lobbyName.asText();
    }

    public long getPlayerId(String playerStringAsJson) {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode playerId = null;
        try {
            playerId = mapper.readTree(playerStringAsJson).get("id");
        } catch (JsonProcessingException e) {
            return -1L;
        }
        return playerId.asLong();
    }

    public Lobby getLobbyFromPlayer(String playerStringAsJson) {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode lobbyNode = null;
        try {
            lobbyNode = mapper.readTree(playerStringAsJson).get("gameLobbyDto");
            return mapper.treeToValue(lobbyNode, Lobby.class);
        } catch (JsonProcessingException e) {
            return null;
        }
    }
}


