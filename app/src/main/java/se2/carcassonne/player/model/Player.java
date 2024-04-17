package se2.carcassonne.player.model;

public class Player {
   private Long id;
   private String username;
   private Long gameLobbyId;

   public Player(Long id, String username, Long gameLobbyId) {
      this.id = id;
      this.username = username;
      this.gameLobbyId = gameLobbyId;
   }

   public Player() {
   }

   public Long getId() {
      return id;
   }

   public void setId(Long id) {
      this.id = id;
   }

   public String getUsername() {
      return username;
   }

   public void setUsername(String username) {
      this.username = username;
   }

   public Long getGameLobbyId() {
      return gameLobbyId;
   }

   public void setGameLobbyId(Long gameLobbyId) {
      this.gameLobbyId = gameLobbyId;
   }

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


