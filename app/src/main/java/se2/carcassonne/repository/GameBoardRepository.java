package se2.carcassonne.repository;

import androidx.lifecycle.MutableLiveData;

import se2.carcassonne.helper.network.WebSocketClient;

public class GameBoardRepository {

    private static GameBoardRepository instance;
    private final WebSocketClient webSocketClient = WebSocketClient.getInstance();

    private final MutableLiveData<String> getNextTurnLiveData = new MutableLiveData<>();

    private GameBoardRepository() {
    }
    public static GameBoardRepository getInstance() {
        if (instance == null) {
            instance = new GameBoardRepository();
        }
        return instance;
    }

//    private void getNextTurnLiveData(String message){
//        webSocketClient.subscribeToTopic();
//    }

    public MutableLiveData<String> getNextTurnLiveData() {
        return getNextTurnLiveData;
    }

}
