package se2.carcassonne.lobby.model;

import java.sql.Timestamp;


public class Lobby {
    private Long id;
    private String name;
    private java.sql.Timestamp gameStartTimestamp;
    private String gameState;
    private Integer numPlayers;
    private Long lobbyCreatorId;

    public Lobby(Long id, String name, Timestamp gameStartTimestamp, String gameState, Integer numPlayers, Long lobbyCreatorId) {
        this.id = id;
        this.name = name;
        this.gameStartTimestamp = gameStartTimestamp;
        this.gameState = gameState;
        this.numPlayers = numPlayers;
        this.lobbyCreatorId = lobbyCreatorId;
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

    public Long getLobbyCreatorId() {
        return lobbyCreatorId;
    }

    public void setLobbyCreatorId(Long lobbyCreatorId) {
        this.lobbyCreatorId = lobbyCreatorId;
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
                ", \"lobbyCreatorId\":" + lobbyCreatorId +
                '}';
    }
}
