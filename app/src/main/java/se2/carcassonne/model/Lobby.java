package se2.carcassonne.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


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
