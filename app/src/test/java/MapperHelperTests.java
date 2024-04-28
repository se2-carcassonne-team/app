import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import se2.carcassonne.helper.mapper.MapperHelper;
import se2.carcassonne.model.Lobby;
import java.sql.Timestamp;

public class MapperHelperTests {
    @Test
    public void testGetPlayerId_ValidJson() {
        MapperHelper mapperHelper = new MapperHelper();
        String validJson = "{\"id\": 123}";
        long playerId = mapperHelper.getPlayerId(validJson);
        assertEquals(123L, playerId);
    }
    @Test
    public void testGetPlayerId_ValidJson3() {
        MapperHelper mapperHelper = new MapperHelper();
        String validJson = "{\"id\": 0}";
        long playerId = mapperHelper.getPlayerId(validJson);
        assertEquals(0L, playerId);
    }

    @Test
    public void testGetPlayerId_ValidJson2() {
        MapperHelper mapperHelper = new MapperHelper();
        String validJson = "{\"id\": 1234567891}";
        long playerId = mapperHelper.getPlayerId(validJson);
        assertEquals(1234567891, playerId);
    }

    @Test
    public void testGetPlayerId_InvalidJson() {
        MapperHelper mapperHelper = new MapperHelper();
        String invalidJson = "{\"invalid\": \"json\"}";
        assertThrows(RuntimeException.class,() -> mapperHelper.getPlayerId(invalidJson));
    }

    @Test
    public void testGetLobbyFromPlayer_ValidJson() {
        MapperHelper mapperHelper = new MapperHelper();

        // Create a sample player JSON string
        String playerJson = "{" +
                "\"id\":123" +
                ", \"username\":\"TestPlayer\"" +
                ", \"gameLobbyDto\":{" +
                "\"id\":456" +
                ", \"name\":\"Test Lobby\"" +
                ", \"gameStartTimestamp\":\"2024-04-25T12:00:00\"" +
                ", \"gameState\":\"LOBBY\"" +
                ", \"numPlayers\":4" +
                ", \"lobbyAdminId\":789" +
                "}" +
                "}";

        // Call the getLobbyFromPlayer method using the mapperHelper instance
        Lobby lobby = mapperHelper.getLobbyFromPlayer(playerJson);

        // Create a sample Lobby object to compare
        Lobby expectedLobby = new Lobby();
        expectedLobby.setId(456L);
        expectedLobby.setName("Test Lobby");
        expectedLobby.setGameStartTimestamp(Timestamp.valueOf("2024-04-25 14:00:00"));
        expectedLobby.setGameState("LOBBY");
        expectedLobby.setNumPlayers(4);
        expectedLobby.setLobbyAdminId(789L);

        // Compare the returned Lobby object with the expected Lobby object
        Assertions.assertEquals(expectedLobby, lobby);
    }
    @Test
    public void testGetLobbyFromPlayer_InvalidJson() {
        MapperHelper mapperHelper = new MapperHelper();

        // Create an invalid player JSON string (missing gameLobbyDto field)
        String invalidPlayerJson = "{" +
                "\"id\":123" +
                ", \"username\":\"TestPlayer\"" +
                // "gameLobbyDto" field is missing
                "}";

        assertNull(mapperHelper.getLobbyFromPlayer(invalidPlayerJson));
    }

    @Test
    public void testGetLobbyFromPlayer_MissingIdField() {
        MapperHelper mapperHelper = new MapperHelper();

        // Create an invalid player JSON string (missing id field)
        String missingIdJson = "{" +
                "\"gameLobbyDto\":{" +
                "\"name\":\"Test Lobby\"," +
                "\"gameStartTimestamp\":\"2024-04-25 12:00:00\"," +
                "\"gameState\":\"LOBBY\"," +
                "\"numPlayers\":4," +
                "\"lobbyAdminId\":456" +
                "}" +
                // "id" field is missing
                "}";

        assertThrows(RuntimeException.class,() -> mapperHelper.getPlayerId(missingIdJson));
    }

    @Test
    public void testGetLobbyFromPlayer_InvalidLobbyData() {
        MapperHelper mapperHelper = new MapperHelper();

        // Create an invalid lobby JSON string (invalid gameStartTimestamp format)
        String invalidLobbyJson = "{" +
                "\"id\":123," +
                "\"name\":\"Test Lobby\"," +
                "\"gameStartTimestamp\":\"Invalid Timestamp\"," + // Invalid timestamp format
                "\"gameState\":\"LOBBY\"," +
                "\"numPlayers\":4," +
                "\"lobbyAdminId\":456" +
                "}";

        assertNull(mapperHelper.getLobbyFromPlayer(invalidLobbyJson));
    }


    @Test
    public void testGetPlayerName_ValidJson() {
        MapperHelper mapperHelper = new MapperHelper();
        String validJson = "{\"username\": \"TestPlayer\"}";
        String playerName = mapperHelper.getPlayerName(validJson);
        assertEquals("TestPlayer", playerName);
    }




}
