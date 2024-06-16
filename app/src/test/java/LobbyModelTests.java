import org.junit.jupiter.api.*;

import java.sql.Timestamp;

import se2.carcassonne.model.Lobby;

class LobbyModelTests {

    @Test
    void testToJsonString() {
        Timestamp timestamp = Timestamp.valueOf("2024-04-25 12:00:00");
        // Create a sample Lobby object
        Lobby lobby = new Lobby();
        lobby.setId(123L);
        lobby.setName("Test Lobby");
        lobby.setGameStartTimestamp(timestamp);
        lobby.setGameState("LOBBY");
        lobby.setNumPlayers(4);
        lobby.setLobbyAdminId(456L);

        // Expected JSON string
        String expectedJson = "{" +
                "\"id\":123" +
                ", \"name\":\"Test Lobby\"" +
                ", \"gameStartTimestamp\":\""+timestamp+"\"" +
                ", \"gameState\":\"LOBBY\"" +
                ", \"numPlayers\":4" +
                ", \"lobbyAdminId\":456" +
                '}';

        // Call the toJsonString() method and compare with the expected JSON string
        Assertions.assertEquals(expectedJson, lobby.toJsonString());
    }
}
