package se2.carcassonne.lobby.viewmodel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;

import se2.carcassonne.lobby.model.Lobby;
import se2.carcassonne.lobby.repository.LobbyRepository;


public class LobbyListViewModel extends ViewModel {

    private LobbyRepository lobbyRepository;
    private MutableLiveData<List<Lobby>> lobbyListLiveData;

    public LobbyListViewModel(LobbyRepository lobbyRepository) {
        this.lobbyRepository = lobbyRepository;
        lobbyListLiveData = new MutableLiveData<>();
    }

    public MutableLiveData<String> getLobbyListLiveData() {
        return lobbyRepository.getMessageLiveData();
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

    public void createLobby(Lobby lobby) {
        lobbyRepository.createLobby(lobby);
    }

    public void fetchLobbies() {
        lobbyRepository.fetchLobbies(new LobbyRepository.LobbyListCallback() {
            @Override
            public void onSuccess(List<Lobby> lobbyList) {
                lobbyListLiveData.postValue(lobbyList);
            }

            @Override
            public void onError(String errorMessage) {
                // Handle error
            }
        });
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
}


