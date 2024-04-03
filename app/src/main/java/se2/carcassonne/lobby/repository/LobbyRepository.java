package se2.carcassonne.lobby.repository;

import androidx.lifecycle.MutableLiveData;

import java.util.List;

import lombok.RequiredArgsConstructor;
import se2.carcassonne.helper.network.WebSocketClient;
import se2.carcassonne.lobby.model.Lobby;

@RequiredArgsConstructor
public class LobbyRepository {

    private final WebSocketClient webSocketClient;
    private final MutableLiveData<String> messageLiveData = new MutableLiveData<>();
    private final MutableLiveData<String> messageLiveDataListLobbies = new MutableLiveData<>();

    private final LobbyApi lobbyApi;

    public LobbyRepository() {
        this.webSocketClient = new WebSocketClient();
        this.lobbyApi = new LobbyApi(this.webSocketClient);
    }

    public void connectToWebSocketServer() {
        webSocketClient.connect(this::messageReceivedFromServer);
    }
    public void subscribeToListLobbyTicket() {
        webSocketClient.subscribeToListLobbiesTopic(this::messageReceivedFromServer);
    }

    private void messageReceivedFromServer(String message) {
//        if (message == "created lobby"){
//        messageLiveData.postValue(message);
//        else if (message.startsWith("all lobbies")
//            messageLiveDataListLobbies.postValue(message);
//    }
        messageLiveData.postValue(message);
    }

//    public void listLobbies(Lobby lobby) {
//        lobbyApi.requestLobbies(lobby);
//    }

    public void createLobby(Lobby lobby){
        lobbyApi.createLobby(lobby);
    }


    public MutableLiveData<String> getMessageLiveData() {
        return messageLiveData;
    }
    public MutableLiveData<String> getMessageLiveDataListLobbies() {
        return messageLiveDataListLobbies;
    }

    public void fetchLobbies(LobbyRepository.LobbyListCallback lobbyListCallback) {
    }

    public interface LobbyListCallback {
        void onSuccess(List<Lobby> lobbyList);
        void onError(String errorMessage);
    }

}
