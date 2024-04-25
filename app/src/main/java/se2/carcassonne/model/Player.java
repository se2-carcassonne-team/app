package se2.carcassonne.model;

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

   @Override
   public String toString() {
      return "Player{" +
              "id=" + id +
              ", username='" + username + '\'' +
              ", gameLobbyDto=" + gameLobbyId +
              '}';
   }

   public String toJsonstring() {
      return "{" +
              "\"id\":" + id +
              ", \"username\":\"" + username + '\"' +
              ", \"gameLobbyDto\":" + gameLobbyId +
              '}';
   }
}


