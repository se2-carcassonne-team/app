package se2.carcassonne.lobby.model;

import lombok.Getter;

@Getter
public enum GameState {
    //Gamestates the lobby can be in
    LOBBY("LOBBY"),
    IN_GAME("IN_GAME"),
    FINISHED("FINISHED");

    private final String displayName;

    GameState(String displayName) {
        this.displayName = displayName;
    }
}
