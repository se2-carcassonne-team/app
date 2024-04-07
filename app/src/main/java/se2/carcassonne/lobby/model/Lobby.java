package se2.carcassonne.lobby.model;

import java.sql.Timestamp;

import lombok.Data;

public class Lobby {

    private Long id;

    private String name;

    private java.sql.Timestamp gameStartTimestamp;

    // game states: lobby, game, gameFinished
    private String gameState;

    // counter for the number of players
    private Integer numPlayers;


    public Lobby(Long id, String name, Timestamp gameStartTimestamp, String gameState, Integer numPlayers) {
        this.id = id;
        this.name = name;
        this.gameStartTimestamp = gameStartTimestamp;
        this.gameState = gameState;
        this.numPlayers = numPlayers;
    }

    public Lobby() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Timestamp getGameStartTimestamp() {
        return gameStartTimestamp;
    }

    public void setGameStartTimestamp(Timestamp gameStartTimestamp) {
        this.gameStartTimestamp = gameStartTimestamp;
    }

    public String getGameState() {
        return gameState;
    }

    public void setGameState(String gameState) {
        this.gameState = gameState;
    }

    public Integer getNumPlayers() {
        return numPlayers;
    }

    public void setNumPlayers(Integer numPlayers) {
        this.numPlayers = numPlayers;
    }

    @Override
    public String toString() {
        return "Lobby{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", gameStartTimestamp=" + gameStartTimestamp +
                ", gameState='" + gameState + '\'' +
                ", numPlayers=" + numPlayers +
                '}';
    }

    public String toJsonString() {
        return "{" +
                "\"id\":" + id +
                ", \"name\":\"" + name + '\"' +
                ", \"gameStartTimestamp\":\"" + gameStartTimestamp + '\"' +
                ", \"gameState\":\"" + gameState + '\"' +
                ", \"numPlayers\":" + numPlayers +
                '}';
    }
}
