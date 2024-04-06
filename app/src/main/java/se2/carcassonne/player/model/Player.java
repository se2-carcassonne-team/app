package se2.carcassonne.player.model;


import se2.carcassonne.lobby.model.Lobby;

public class Player {
   private Long id;
   private String username;
   private Lobby gameLobbyDto;

   public Player(Long id, String username, Lobby gameLobby) {
      this.id = id;
      this.username = username;
      this.gameLobbyDto = gameLobby;
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

   public Lobby getGameLobbyDto() {
      return gameLobbyDto;
   }

   public void setGameLobbyDto(Lobby gameLobbyDto) {
      this.gameLobbyDto = gameLobbyDto;
   }
}


