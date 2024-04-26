import org.junit.jupiter.api.Test;

import se2.carcassonne.model.Coordinates;
import se2.carcassonne.model.GameBoard;
import se2.carcassonne.model.Tile;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class GameBoardModelTests {
    @Test
    public void testCorrectInitializeGameBoardSize() {
        GameBoard gameBoard = new GameBoard();
        Tile[][] gameBoardMatrix = gameBoard.getGameBoardMatrix();
        assertEquals(25, gameBoardMatrix.length);
        assertEquals(25, gameBoardMatrix[0].length);
    }

    @Test
    public void testCorrectInitializeGameBoardOnlyOneTileInMiddle() {
        GameBoard gameBoard = new GameBoard();
        Tile[][] gameBoardMatrix = gameBoard.getGameBoardMatrix();
        for (int i = 0; i < gameBoardMatrix.length; i++) {
            for (int j = 0; j < gameBoardMatrix[i].length; j++) {
                if (i == 13 && j == 13) {
                    assertNotNull(gameBoardMatrix[i][j]); // start tile in the middle != null
                } else {
                    assertNull(gameBoardMatrix[i][j]); // null everywhere else
                }
            }
        }
    }

    @Test
    public void testCheckGameBoardListsNotNull() {
        GameBoard gameBoard = new GameBoard();
        assertNotNull(gameBoard.getPlacedTiles());
        assertNotNull(gameBoard.getPlaceablePositions());
        assertNotNull(gameBoard.getAllTiles());
    }

    @Test
    public void testCheckGameBoardTilesAfterInit() {
        Tile startTile = new Tile(0L, "castle_wall_road", new int[]{3, 2, 1, 2}, new int[]{3, 3, 3, 2, 2, 2, 1, 1, 1});
        GameBoard gameBoard = new GameBoard();
        assertEquals(1, gameBoard.getPlacedTiles().size()); // start tile in the middle
        assertEquals(startTile.getId(), gameBoard.getGameBoardMatrix()[13][13].getId()); // tile in the middle of matrix equals start tile
        assertEquals(startTile.getId(), gameBoard.getPlacedTiles().get(0).getId()); // start tile in the list of placed tiles
        assertEquals(4, gameBoard.getPlaceablePositions().size()); // 4 placeable positions around the start tile
        assertTrue(gameBoard.getPlaceablePositions().contains(new Coordinates(13, 12))); // free north
        assertTrue(gameBoard.getPlaceablePositions().contains(new Coordinates(14, 13))); // free east
        assertTrue(gameBoard.getPlaceablePositions().contains(new Coordinates(13, 14))); // free south
        assertTrue(gameBoard.getPlaceablePositions().contains(new Coordinates(12, 13))); // free west
    }

    @Test
    public void testHighlightValidPositions() {
        Tile tileToPlace = new Tile(1L, "road_junction_large", new int[]{2, 2, 2, 2}, new int[]{1, 2, 1, 2, 4, 2, 1, 2, 1});
        GameBoard gameBoard = new GameBoard();
        assertEquals(4, gameBoard.getPlaceablePositions().size()); // 4 valid positions around the start tile
        assertEquals(1, gameBoard.getPlacedTiles().size()); // 1 tile placed on the board

        ArrayList<Coordinates> validPositions = gameBoard.highlightValidPositions(tileToPlace); // see where the tile can be placed
        assertEquals(2, validPositions.size()); // 2 valid positions around the start tile
        assertTrue(validPositions.contains(new Coordinates(14, 13))); // can be placed east
        assertTrue(validPositions.contains(new Coordinates(12, 13))); // can be placed west
    }

    @Test
    public void testPlaceTile() {
        Tile tileToPlace = new Tile(1L, "road_junction_large", new int[]{2, 2, 2, 2}, new int[]{1, 2, 1, 2, 4, 2, 1, 2, 1});
        GameBoard gameBoard = new GameBoard();
        assertEquals(4, gameBoard.getPlaceablePositions().size()); // 4 valid positions around the start tile
        assertEquals(1, gameBoard.getPlacedTiles().size()); // 1 tile placed on the board
        gameBoard.placeTile(tileToPlace, new Coordinates(14, 13)); // place the tile east from start tile
        assertEquals(6, gameBoard.getPlaceablePositions().size()); // 6 valid positions around the start tile
        assertEquals(2, gameBoard.getPlacedTiles().size()); // 2 tile placed on the board
        assertTrue(gameBoard.getPlaceablePositions().contains(new Coordinates(14, 12))); // north of new tile
        assertTrue(gameBoard.getPlaceablePositions().contains(new Coordinates(15, 13))); // east of new tile
        assertTrue(gameBoard.getPlaceablePositions().contains(new Coordinates(14, 14))); // south of new tile
        assertTrue(gameBoard.getPlaceablePositions().contains(new Coordinates(13, 12))); // north of new tile
        assertTrue(gameBoard.getPlaceablePositions().contains(new Coordinates(13, 14))); // south of new tile
        assertTrue(gameBoard.getPlaceablePositions().contains(new Coordinates(12, 13))); // west of new tile
    }
}
