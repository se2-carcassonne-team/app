package se2.carcassonne.lobby.viewmodel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import se2.carcassonne.lobby.model.Lobby;
import se2.carcassonne.lobby.repository.LobbyRepository;
import se2.carcassonne.player.model.Player;


public class LobbyListViewModel extends ViewModel {
    private final LobbyRepository lobbyRepository;

    public LobbyListViewModel(LobbyRepository lobbyRepository) {
        this.lobbyRepository = lobbyRepository;
    }

    public MutableLiveData<String> getMessageLiveDataListLobbies() {
        return lobbyRepository.getListAllLobbiesLiveData();
    }
    public MutableLiveData<String> getMessageLiveDataListPlayers() {
        return lobbyRepository.getListAllPlayersLiveData();
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

    public void createLobby(Lobby lobby) {
        lobbyRepository.createLobby(lobby);
    }

    public void getAllLobbies() {
        lobbyRepository.getAllLobbies();
    }
    public void getAllPlayers(Lobby lobby) {
        lobbyRepository.getAllPlayers(lobby);
    }

    public void joinLobby(Lobby lobby) {
        lobbyRepository.joinLobby(lobby);
    }

    public void leaveLobby(Player player) {
        lobbyRepository.leaveLobby(player);
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

    public Lobby getLobbyFromJsonString(String lobbyStringAsJson) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.readValue(lobbyStringAsJson, Lobby.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace(); // Handle or log the exception
            return null;
        }
    }
}


