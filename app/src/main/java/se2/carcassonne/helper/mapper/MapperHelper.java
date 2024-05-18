package se2.carcassonne.helper.mapper;

import android.util.Log;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;


import se2.carcassonne.model.Lobby;
import se2.carcassonne.model.Player;
import se2.carcassonne.model.PlayerColour;

// fixme why is this manual mapping needed? use automatic serialization with oop
public class MapperHelper {

    // constants for logging:
    private static final String MAPPING_EXCEPTION = "Mapping Exception";
    private static final String JSON_ERROR_GETIDFROMLOBBYSTRING ="Error processing JSON in getIdFromLobbyString: ";
    public Player getPlayer(String playerStringAsJson) {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode player = null;
        try {
            player = mapper.readTree(playerStringAsJson);
            return mapper.treeToValue(player, Player.class);
        } catch (JsonProcessingException e) {
            Log.e(MAPPING_EXCEPTION, "Error processing JSON in getPlayer: " + e.getMessage());
            return null;
        }
    }

    public PlayerColour getPlayerColour(String playerStringAsJson) {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode playerColour = null;
        try {
            playerColour = mapper.readTree(playerStringAsJson).get("playerColour");
        } catch (JsonProcessingException e) {
            Log.e(MAPPING_EXCEPTION, "Error processing JSON in playerColour: " + e.getMessage());
            return null;
        }
        return PlayerColour.valueOf(playerColour.asText());
    }

    public long getPlayerId(String playerStringAsJson) {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode playerId = null;
        try {
            playerId = mapper.readTree(playerStringAsJson).get("id");
        } catch (JsonProcessingException e) {
            Log.e(MAPPING_EXCEPTION, "Error processing JSON in getPlayerId: " + e.getMessage());
            return -1L;
        }
        return playerId.asLong();
    }

    public Lobby getLobbyFromPlayer(String playerStringAsJson) {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode lobbyNode = null;
        try {
            lobbyNode = mapper.readTree(playerStringAsJson).get("gameLobbyDto");
            return mapper.treeToValue(lobbyNode, Lobby.class);
        } catch (JsonProcessingException e) {
            Log.e(MAPPING_EXCEPTION, "Error processing JSON in getPlayerId: " + e.getMessage());
            return null;
        }
    }

    public String getPlayerName(String playerStringAsJson) {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode username = null;
        try {
            username = mapper.readTree(playerStringAsJson).get("username");
        } catch (JsonProcessingException e) {
            return null;
        }
        return username.asText();
    }


    // LobbyMapping
    public String getLobbyName(String lobbyStringAsJson) {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode lobbyName = null;
        try {
            lobbyName = mapper.readTree(lobbyStringAsJson).get("name");
        } catch (JsonProcessingException e) {
            Log.e(MAPPING_EXCEPTION, "Error processing JSON in getLobbyName: " + e.getMessage());
            return null;
        }
        return lobbyName.asText();
    }

    public Lobby getLobbyFromJsonString(String lobbyStringAsJson) {
        ObjectMapper mapper = new ObjectMapper();
        int startIndex = lobbyStringAsJson.indexOf("\"gameStartTimestamp\":\"") + "\"gameStartTimestamp\":\"".length();
        int endIndex = lobbyStringAsJson.indexOf("\"", startIndex);

        if (startIndex != -1 && endIndex != -1) { // If start and end indices are found
            String timestampSubstring = lobbyStringAsJson.substring(startIndex, endIndex);
            timestampSubstring = timestampSubstring.replace(" ", "T");
            lobbyStringAsJson = lobbyStringAsJson.substring(0, startIndex) + timestampSubstring + lobbyStringAsJson.substring(endIndex);
        }
        try {
            return mapper.readValue(lobbyStringAsJson, Lobby.class);
        } catch (JsonProcessingException e) {
            Log.e(MAPPING_EXCEPTION, "Error processing JSON in getLobbyFromJsonString: " + e.getMessage());
            return null;
        }
    }

    public String getIdFromLobbyString(String lobbyStringAsJson) {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode lobbyId = null;
        try {
            lobbyId = mapper.readTree(lobbyStringAsJson).get("id");
        } catch (JsonProcessingException e) {
            Log.e(MAPPING_EXCEPTION, JSON_ERROR_GETIDFROMLOBBYSTRING + e.getMessage());
            return null;
        }
        return lobbyId.asText();
    }

    public Long getLobbyIdFromPlayer(String playerAsJsonString) {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode lobbyId = null;
        try {
            lobbyId = mapper.readTree(playerAsJsonString).get("gameLobbyId");
        } catch (JsonProcessingException e) {
            Log.e(MAPPING_EXCEPTION, JSON_ERROR_GETIDFROMLOBBYSTRING + e.getMessage());
            return null;
        }
        return lobbyId.asLong();
    }

    public Long getIdFromLobbyStringAsLong(String lobbyStringAsJson) {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode lobbyId = null;
        try {
            lobbyId = mapper.readTree(lobbyStringAsJson).get("id");
        } catch (JsonProcessingException e) {
            Log.e(MAPPING_EXCEPTION, JSON_ERROR_GETIDFROMLOBBYSTRING + e.getMessage());
            return null;
        }
        return lobbyId.asLong();
    }

    public Long getLobbyAdminIdFromLobbyString(String newGameLobbyStringAsJson) {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode lobbyAdminId = null;
        try {
            lobbyAdminId = mapper.readTree(newGameLobbyStringAsJson).get("lobbyAdminId");
        } catch (JsonProcessingException e) {
            Log.e(MAPPING_EXCEPTION, JSON_ERROR_GETIDFROMLOBBYSTRING + e.getMessage());
            return null;
        }
        return lobbyAdminId.asLong();
    }

    public Integer getNumOfPlayersFromLobby(String message) {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode numOfPlayers = null;
        try {
            numOfPlayers = mapper.readTree(message).get("numPlayers");
        } catch (JsonProcessingException e) {
            Log.e(MAPPING_EXCEPTION, JSON_ERROR_GETIDFROMLOBBYSTRING + e.getMessage());
            return null;
        }
        return numOfPlayers.asInt();
    }
}