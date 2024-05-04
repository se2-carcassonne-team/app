package se2.carcassonne.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor

// This is included to ignore the "availableColours" parameter that comes from the backend
@JsonIgnoreProperties(ignoreUnknown = true)
public class Lobby {
    private Long id;
    private String name;
    private java.sql.Timestamp gameStartTimestamp;
    private String gameState;
    private Integer numPlayers;
    private Long lobbyAdminId;
/*
    @JsonIgnore
    private List<String> availableColours;*/

    public String toJsonString() {
        return "{" +
                "\"id\":" + id +
                ", \"name\":\"" + name + '\"' +
                ", \"gameStartTimestamp\":\"" + gameStartTimestamp + '\"' +
                ", \"gameState\":\"" + gameState + '\"' +
                ", \"numPlayers\":" + numPlayers +
                ", \"lobbyAdminId\":" + lobbyAdminId +
                '}';
    }
}
