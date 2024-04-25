package se2.carcassonne.model;

import java.sql.Timestamp;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Lobby {
    private Long id;
    private String name;
    private java.sql.Timestamp gameStartTimestamp;
    private String gameState;
    private Integer numPlayers;
    private Long lobbyAdminId;

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
                ", \"lobbyAdminId\":" + lobbyAdminId +
                '}';
    }
}
