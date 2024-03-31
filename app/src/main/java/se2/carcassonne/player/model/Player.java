package se2.carcassonne.player.model;


import se2.carcassonne.gamelobby.model.GameLobby;

public class Player {
   private Long id;
   private String username;
   private GameLobby gameLobby;

   public Player(Long id, String username, GameLobby gameLobby) {
      this.id = id;
      this.username = username;
      this.gameLobby = gameLobby;
   }
}
