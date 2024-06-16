import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import se2.carcassonne.model.Coordinates;
import se2.carcassonne.model.FinishedTurnDto;
import se2.carcassonne.model.GameBoard;
import se2.carcassonne.model.Scoreboard;
import se2.carcassonne.model.Tile;

class GameBoardModelTests {
    @Test
    void testCorrectInitializeGameBoardSize() {
        GameBoard gameBoard = new GameBoard();
        Tile[][] gameBoardMatrix = gameBoard.getGameBoardMatrix();
        assertEquals(25, gameBoardMatrix.length);
        assertEquals(25, gameBoardMatrix[0].length);
    }

    @Test
    void testCorrectInitializeGameBoardOnlyOneTileInMiddle() {
        GameBoard gameBoard = new GameBoard();
        Tile[][] gameBoardMatrix = gameBoard.getGameBoardMatrix();
        for (int i = 0; i < gameBoardMatrix.length; i++) {
            for (int j = 0; j < gameBoardMatrix[i].length; j++) {
                if (i == 12 && j == 12) {
                    assertNotNull(gameBoardMatrix[i][j]); // start tile in the middle != null
                } else {
                    assertNull(gameBoardMatrix[i][j]); // null everywhere else
                }
            }
        }
    }

    @Test
    void testCheckGameBoardListsNotNull() {
        GameBoard gameBoard = new GameBoard();
        assertNotNull(gameBoard.getPlacedTiles());
        assertNotNull(gameBoard.getPlaceablePositions());
        assertNotNull(gameBoard.getAllTiles());
    }

    @Test
    void testCheckGameBoardTilesAfterInit() {
        Tile startTile = new Tile(0L, "castle_wall_road", new int[]{3, 2, 1, 2}, new int[]{3, 3, 3, 2, 2, 2, 1, 1, 1}, new boolean[]{false, false, false, false, false, false, false, false, false}, false);
        GameBoard gameBoard = new GameBoard();
        assertEquals(1, gameBoard.getPlacedTiles().size()); // start tile in the middle
        assertEquals(startTile.getId(), gameBoard.getGameBoardMatrix()[12][12].getId()); // tile in the middle of matrix equals start tile
        assertEquals(startTile.getId(), gameBoard.getPlacedTiles().get(0).getId()); // start tile in the list of placed tiles
        assertEquals(4, gameBoard.getPlaceablePositions().size()); // 4 placeable positions around the start tile
        assertTrue(gameBoard.getPlaceablePositions().contains(new Coordinates(12, 11))); // free north
        assertTrue(gameBoard.getPlaceablePositions().contains(new Coordinates(13, 12))); // free east
        assertTrue(gameBoard.getPlaceablePositions().contains(new Coordinates(12, 13))); // free south
        assertTrue(gameBoard.getPlaceablePositions().contains(new Coordinates(11, 12))); // free west
    }

    @Test
    void testHighlightValidPositions() {
        Tile tileToPlace = new Tile(1L, "road_junction_large", new int[]{2, 2, 2, 2}, new int[]{1, 2, 1, 2, 4, 2, 1, 2, 1}, new boolean[]{false, false, false, false, false, false, false, false, false}, true);
        GameBoard gameBoard = new GameBoard();
        assertEquals(4, gameBoard.getPlaceablePositions().size()); // 4 valid positions around the start tile
        assertEquals(1, gameBoard.getPlacedTiles().size()); // 1 tile placed on the board

        ArrayList<Coordinates> validPositions = gameBoard.highlightValidPositions(tileToPlace); // see where the tile can be placed
        assertEquals(2, validPositions.size()); // 2 valid positions around the start tile
        assertTrue(validPositions.contains(new Coordinates(13, 12))); // can be placed east
        assertTrue(validPositions.contains(new Coordinates(11, 12))); // can be placed west
    }

    @Test
    void testPlaceTile() {
        Tile tileToPlace = new Tile(1L, "road_junction_large", new int[]{2, 2, 2, 2}, new int[]{1, 2, 1, 2, 4, 2, 1, 2, 1}, new boolean[]{false, false, false, false, false, false, false, false, false}, true);
        GameBoard gameBoard = new GameBoard();
        assertEquals(4, gameBoard.getPlaceablePositions().size()); // 4 valid positions around the start tile
        assertEquals(1, gameBoard.getPlacedTiles().size()); // 1 tile placed on the board
        gameBoard.placeTile(tileToPlace, new Coordinates(13, 12)); // place the tile east from start tile
        assertEquals(6, gameBoard.getPlaceablePositions().size()); // 6 valid positions around the start tile
        assertEquals(2, gameBoard.getPlacedTiles().size()); // 2 tile placed on the board
        assertTrue(gameBoard.getPlaceablePositions().contains(new Coordinates(13, 11))); // north of new tile
        assertTrue(gameBoard.getPlaceablePositions().contains(new Coordinates(14, 12))); // east of new tile
        assertTrue(gameBoard.getPlaceablePositions().contains(new Coordinates(13, 13))); // south of new tile
        assertTrue(gameBoard.getPlaceablePositions().contains(new Coordinates(12, 11))); // north of new tile
        assertTrue(gameBoard.getPlaceablePositions().contains(new Coordinates(12, 13))); // south of new tile
        assertTrue(gameBoard.getPlaceablePositions().contains(new Coordinates(11, 12))); // west of new tile
    }

    @Test
    void noValidTilePlacementForAnyRotation() {
        GameBoard gameBoard = new GameBoard();
        gameBoard.placeTile(gameBoard.getAllTiles().get(1), new Coordinates(13, 12)); // place the tile east from start tile
        gameBoard.placeTile(gameBoard.getAllTiles().get(39), new Coordinates(11, 12)); // place the tile west from start tile
        gameBoard.placeTile(gameBoard.getAllTiles().get(1), new Coordinates(12, 13)); // place the tile south from start tile
        assertFalse(gameBoard.hasValidPositionForAnyRotation(gameBoard.getAllTiles().get(45))); // check if there is a valid position for any rotation
    }

    @Test
    void validTilePlacementsForAnyRotation() {
        GameBoard gameBoard = new GameBoard();
        assertTrue(gameBoard.hasValidPositionForAnyRotation(gameBoard.getAllTiles().get(1))); // check if there is a valid position for any rotation
    }

    @Test
    void testInitGamePoints() {
        GameBoard gameBoard = new GameBoard();
        List<Long> playerIds = Arrays.asList(1L, 2L, 3L);
        gameBoard.initGamePoints(playerIds);

        assertEquals(3, gameBoard.getPlayerWithPoints().size());
        assertEquals(0, gameBoard.getPlayerWithPoints().get(1L));
        assertEquals(0, gameBoard.getPlayerWithPoints().get(2L));
        assertEquals(0, gameBoard.getPlayerWithPoints().get(3L));
    }

    @Test
    void testGetTopThreePlayersWithLessThanThreePlayers() {
        GameBoard gameBoard = new GameBoard();
        gameBoard.initGamePoints(Arrays.asList(1L, 2L));
        gameBoard.getPlayerWithPoints().put(1L, 40);
        gameBoard.getPlayerWithPoints().put(2L, 30);

        List<Long> topPlayers = gameBoard.getTopThreePlayers();

        assertEquals(2, topPlayers.size());
        assertEquals(1L, topPlayers.get(0));
        assertEquals(2L, topPlayers.get(1));
    }

    @Test
    void testGetTopThreePlayersWithMoreThanThreePlayers() {
        GameBoard gameBoard = new GameBoard();
        gameBoard.initGamePoints(Arrays.asList(1L, 2L, 3L, 4L));
        gameBoard.getPlayerWithPoints().put(1L, 40);
        gameBoard.getPlayerWithPoints().put(2L, 30);
        gameBoard.getPlayerWithPoints().put(3L, 20);
        gameBoard.getPlayerWithPoints().put(4L, 10);
        List<Long> topPlayers = gameBoard.getTopThreePlayers();

        assertEquals(3, topPlayers.size());
        assertEquals(1L, topPlayers.get(0));
        assertEquals(2L, topPlayers.get(1));
        assertEquals(3L, topPlayers.get(2));
    }

    @Test
    void testGetTopThreePlayersWithNoPlayers() {
        GameBoard gameBoard = new GameBoard();
        List<Long> topPlayers = gameBoard.getTopThreePlayers();

        assertTrue(topPlayers.isEmpty());
    }

    @Test
    void testGetTopThreePlayersWithTie() {
        GameBoard gameBoard = new GameBoard();
        gameBoard.initGamePoints(Arrays.asList(1L, 2L, 3L, 4L));
        gameBoard.getPlayerWithPoints().put(1L, 30);
        gameBoard.getPlayerWithPoints().put(2L, 30);
        gameBoard.getPlayerWithPoints().put(3L, 30);
        gameBoard.getPlayerWithPoints().put(4L, 10);
        List<Long> topPlayers = gameBoard.getTopThreePlayers();

        assertEquals(3, topPlayers.size());
        assertTrue(topPlayers.contains(1L));
        assertTrue(topPlayers.contains(3L));
        assertTrue(topPlayers.contains(2L));
    }

    // Additional test to cover edge cases
    @Test
    void testInitGamePointsWithEmptyList() {
        List<Long> playerIds = Collections.emptyList();
        GameBoard gameBoard = new GameBoard();
        gameBoard.initGamePoints(playerIds);

        assertTrue(gameBoard.getPlayerWithPoints().isEmpty());
    }


    /////// tests for sortTopThreePlayersAfterForwarding(Scoreboard scoreboard)
    @Test
    void testSortTopThreePlayersAfterForwardingWithLessThanThreePlayers() {
        GameBoard gameBoard = new GameBoard();
        gameBoard.initGamePoints(Arrays.asList(1L, 2L));
        gameBoard.getPlayerWithPoints().put(1L, 40);
        gameBoard.getPlayerWithPoints().put(2L, 30);

        HashMap<Long, String> playerIdsWithNames = new HashMap<>();
        Scoreboard scoreboard = new Scoreboard(1L, 1L,playerIdsWithNames);

        scoreboard.getPlayerIdsWithNames().put(1L, "Player1");
        scoreboard.getPlayerIdsWithNames().put(2L, "Player2");

        List<String> topPlayers = gameBoard.sortTopThreePlayersAfterForwarding(scoreboard);

        assertEquals(2, topPlayers.size());
        assertEquals("Player1", topPlayers.get(0));
        assertEquals("Player2", topPlayers.get(1));
    }

    @Test
    void testSortTopThreePlayersAfterForwardingWithMoreThanThreePlayers() {
        GameBoard gameBoard = new GameBoard();
        gameBoard.initGamePoints(Arrays.asList(1L, 2L, 3L, 4L));
        gameBoard.getPlayerWithPoints().put(1L, 40);
        gameBoard.getPlayerWithPoints().put(2L, 30);
        gameBoard.getPlayerWithPoints().put(3L, 20);
        gameBoard.getPlayerWithPoints().put(4L, 10);

        HashMap<Long, String> playerIdsWithNames = new HashMap<>();
        Scoreboard scoreboard = new Scoreboard(1L, 1L,playerIdsWithNames);
        scoreboard.getPlayerIdsWithNames().put(1L, "Player1");
        scoreboard.getPlayerIdsWithNames().put(2L, "Player2");
        scoreboard.getPlayerIdsWithNames().put(3L, "Player3");
        scoreboard.getPlayerIdsWithNames().put(4L, "Player4");

        List<String> topPlayers = gameBoard.sortTopThreePlayersAfterForwarding(scoreboard);

        assertEquals(3, topPlayers.size());
        assertEquals("Player1", topPlayers.get(0));
        assertEquals("Player2", topPlayers.get(1));
        assertEquals("Player3", topPlayers.get(2));
    }

    @Test
    void testSortTopThreePlayersAfterForwardingWithTie() {
        GameBoard gameBoard = new GameBoard();
        gameBoard.initGamePoints(Arrays.asList(1L, 2L, 3L, 4L));
        gameBoard.getPlayerWithPoints().put(1L, 30);
        gameBoard.getPlayerWithPoints().put(2L, 30);
        gameBoard.getPlayerWithPoints().put(3L, 30);
        gameBoard.getPlayerWithPoints().put(4L, 10);

        HashMap<Long, String> playerIdsWithNames = new HashMap<>();
        Scoreboard scoreboard = new Scoreboard(1L, 1L,playerIdsWithNames);

        scoreboard.getPlayerIdsWithNames().put(1L, "Player1");
        scoreboard.getPlayerIdsWithNames().put(2L, "Player2");
        scoreboard.getPlayerIdsWithNames().put(3L, "Player3");
        scoreboard.getPlayerIdsWithNames().put(4L, "Player4");

        List<String> topPlayers = gameBoard.sortTopThreePlayersAfterForwarding(scoreboard);

        assertEquals(3, topPlayers.size());
        assertTrue(topPlayers.contains("Player1"));
        assertTrue(topPlayers.contains("Player2"));
        assertTrue(topPlayers.contains("Player3"));
    }


    //////// tests for updatePoints(FinishedTurnDto finishedTurnDto)
    @Test
    void testUpdatePointsWithNullPointsMap() {
        GameBoard gameBoard = new GameBoard();
        gameBoard.initGamePoints(Arrays.asList(1L, 2L));
        gameBoard.updatePoints(new FinishedTurnDto(null, null, null));

        assertEquals(0, gameBoard.getPlayerWithPoints().get(1L));
        assertEquals(0, gameBoard.getPlayerWithPoints().get(2L));
    }

    @Test
    void testUpdatePointsWithPointsMap() {
        GameBoard gameBoard = new GameBoard();
        gameBoard.initGamePoints(Arrays.asList(1L, 2L));

        Map<Long, Integer> points = new HashMap<>();
        points.put(1L, 10);
        points.put(2L, 20);

        gameBoard.updatePoints(new FinishedTurnDto(null, points, null));

        assertEquals(10, gameBoard.getPlayerWithPoints().get(1L));
        assertEquals(20, gameBoard.getPlayerWithPoints().get(2L));
    }



    ///// tests for finishedTurnRemoveMeeplesOnRoads(Map<Long, List<Meeple>> playersWithMeeples)

    @Test
    void testFinishedTurnRemoveMeeplesOnRoadWithNullMap() {
        GameBoard gameBoard = new GameBoard();
        gameBoard.initGamePoints(Arrays.asList(1L, 2L));
        Map<Long, Integer> removedMeeplesCount = gameBoard.finishedTurnRemoveMeeplesOnRoad(null);

        assertTrue(removedMeeplesCount.isEmpty());
    }

    @Test
    void testFinishedTurnRemoveMeeplesOnRoadWithEmptyMap() {
        GameBoard gameBoard = new GameBoard();
        gameBoard.initGamePoints(Arrays.asList(1L, 2L));
        Map<Long, Integer> removedMeeplesCount = gameBoard.finishedTurnRemoveMeeplesOnRoad(new HashMap<>());

        assertTrue(removedMeeplesCount.isEmpty());
    }

}
