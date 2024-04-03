package se2.carcassonne.lobby.model;

import java.sql.Timestamp;

import lombok.Data;

@Data
public class Lobby {

    private Long id;

    private String name;

    private java.sql.Timestamp gameStartTimestamp;

    // game states: lobby, game, gameFinished
    private String gameState;

    // counter for the number of players
    private Integer numPlayers;

    private Integer maxPlayers;

    public Lobby(Long id, String name, Timestamp gameStartTimestamp, String gameState, Integer numPlayers, Integer maxPlayers) {
        this.id = id;
        this.name = name;
        this.gameStartTimestamp = gameStartTimestamp;
        this.gameState = gameState;
        this.numPlayers = numPlayers;
        this.maxPlayers = maxPlayers;
    }


}
