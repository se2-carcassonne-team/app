package se2.carcassonne.lobby.viewmodel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

import se2.carcassonne.lobby.model.Lobby;
import se2.carcassonne.lobby.repository.LobbyRepository;


public class LobbyListViewModel extends ViewModel {

    private LobbyRepository lobbyRepository;
    private MutableLiveData<List<Lobby>> lobbyListLiveData;

    public LobbyListViewModel() {
        lobbyRepository = new LobbyRepository();
        lobbyListLiveData = new MutableLiveData<>();
    }

    public MutableLiveData<String> getLobbyListLiveData() {
        return lobbyRepository.getMessageLiveData();
    }
//    public MutableLiveData<String> getLobbyListLiveDataListLobby() {
//        return lobbyRepository.getMessageLiveDataListLobby();
//    }

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
//    TODO: getAllLobbies
}


