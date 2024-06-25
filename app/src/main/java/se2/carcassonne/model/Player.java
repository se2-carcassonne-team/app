package se2.carcassonne.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Player implements Serializable {
   private Long id;
   private String username;
   private String sessionId;
   private Long gameLobbyId;
   private Long gameSessionId;
   private List<Meeple> meepleList;
   private PlayerColour playerColour;
    private int points;
    private Boolean canCheat;
    private int cheatPoints;

    public Player(Long id, String username, String sessionId, Long gameLobbyId, Long gameSessionId, PlayerColour playerColour) {
      this.id = id;
      this.username = username;
      this.sessionId = sessionId;
      this.gameLobbyId = gameLobbyId;
      this.gameSessionId = gameSessionId;
      this.meepleList = new ArrayList<>(7);
      this.playerColour = playerColour;
      this.points = 0;
      this.canCheat = false;
      this.cheatPoints = 0;
   }

    public void addPoints(int points) {
        this.points += points;
    }
}


