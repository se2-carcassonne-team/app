package se2.carcassonne.model;

import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Player {
   private Long id;
   private String username;
   private Long gameLobbyId;
   private Long gameSessionId;
   private List<Meeple> meepleList;
   private PlayerColour playerColour;

   public Player(Long id, String username, Long gameLobbyId, Long gameSessionId, PlayerColour playerColour) {
      this.id = id;
      this.username = username;
      this.gameLobbyId = gameLobbyId;
      this.gameSessionId = gameSessionId;
      this.meepleList = new ArrayList<>(7);
      this.playerColour = playerColour;
   }
}


