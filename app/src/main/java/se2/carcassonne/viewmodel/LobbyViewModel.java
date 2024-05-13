package se2.carcassonne.viewmodel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import se2.carcassonne.model.Lobby;
import se2.carcassonne.repository.LobbyRepository;

public class LobbyViewModel extends ViewModel {
    private final LobbyRepository lobbyRepository;

    public LobbyViewModel() {
        lobbyRepository = LobbyRepository.getInstance();
    }

    public MutableLiveData<String> getListOfAllLobbiesLiveData() {
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

    public MutableLiveData<String> getPlayerJoinsOrLeavesLobbyLiveData() {
        return lobbyRepository.getPlayerJoinsOrLeavesLobbyLiveData();
    }

    public MutableLiveData<String> getPlayerLeavesLobbyLiveData() {
        return lobbyRepository.getPlayerLeavesLobbyLiveData();
    }

    public MutableLiveData<String> getPlayerInLobbyReceivesUpdatedLobbyLiveData() {
        return lobbyRepository.getPlayerInLobbyReceivesUpdatedLobbyLiveData();
    }

    public MutableLiveData<String> getGameToBeStartedLiveData() {
        return lobbyRepository.getGameToBeStartedLiveData();
    }

    public void createLobby(Lobby lobby) {
        lobbyRepository.createLobby(lobby);
    }

    public void joinLobby(Lobby lobby) {
        lobbyRepository.joinLobby(lobby);
    }

    public void leaveLobby() {
        lobbyRepository.leaveLobby();
    }

    public void getAllLobbies() {
        lobbyRepository.getAllLobbies();
    }

    public void getAllPlayers(Lobby lobby) {
        lobbyRepository.getAllPlayers(lobby);
    }

    public void startGame(Long gameLobbyId) {
        lobbyRepository.startGame(gameLobbyId);
    }



}

