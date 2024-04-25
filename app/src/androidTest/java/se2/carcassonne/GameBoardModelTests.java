package se2.carcassonne;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Objects;

import se2.carcassonne.model.Coordinates;
import se2.carcassonne.model.GameBoard;
import se2.carcassonne.model.Tile;

public class GameBoardModelTests {
    @Test
    public void testCorrectInitializeGameBoardSize() {
        GameBoard gameBoard = new GameBoard();
        Tile[][]gameBoardMatrix = gameBoard.getGameBoardMatrix();
        assert(gameBoardMatrix.length == 25);
        assert(gameBoardMatrix[0].length == 25);
    }

    @Test
    public void testCorrectInitializeGameBoardOnlyOneTileInMiddle() {
        GameBoard gameBoard = new GameBoard();
        Tile[][]gameBoardMatrix = gameBoard.getGameBoardMatrix();
        for (int i = 0; i < gameBoardMatrix.length; i++) {
            for (int j = 0; j < gameBoardMatrix[i].length; j++) {
                if (i == 13 && j == 13) {
                    assert(gameBoardMatrix[i][j] != null); // start tile in the middle != null
                } else {
                    assert(gameBoardMatrix[i][j] == null); // null everywhere else
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
        Tile startTile = new Tile(0L,"castle_wall_road", new int[]{3,2,1,2}, new int[]{3,3,3, 2,2,2, 1,1,1});
        GameBoard gameBoard = new GameBoard();
        assert(gameBoard.getPlacedTiles().size() == 1); // start tile in the middle
        assert(Objects.equals(gameBoard.getGameBoardMatrix()[13][13].getId(), startTile.getId())); // tile in the middle of matrix equals start tile
        assert(Objects.equals(gameBoard.getPlacedTiles().get(0).getId(), startTile.getId())); // start tile in the list of placed tiles
        assert(gameBoard.getPlaceablePositions().size() == 4); // 4 placeable positions around the start tile
        assert(gameBoard.getPlaceablePositions().contains(new Coordinates(13, 12))); // free north
        assert(gameBoard.getPlaceablePositions().contains(new Coordinates(14, 13))); // free east
        assert(gameBoard.getPlaceablePositions().contains(new Coordinates(13, 14))); // free south
        assert(gameBoard.getPlaceablePositions().contains(new Coordinates(12, 13))); // free west
    }

    @Test
    public void testHighlightValidPositions() {
        Tile tileToPlace = new Tile(1L, "road_junction_large", new int[]{2,2,2,2}, new int[]{1,2,1, 2,4,2, 1,2,1});
        GameBoard gameBoard = new GameBoard();
        assert(gameBoard.getPlaceablePositions().size() == 4); // 4 valid positions around the start tile
        assert(gameBoard.getPlacedTiles().size() == 1); // 1 tile placed on the board

        ArrayList<Coordinates> validPositions= gameBoard.highlightValidPositions(tileToPlace); // see where the tile can be placed
        assert(validPositions.size() == 2); // 2 valid positions around the start tile
        assert(validPositions.contains(new Coordinates(14, 13))); // can be placed east
        assert(validPositions.contains(new Coordinates(12, 13))); // can be placed west
    }

    @Test
    public void testPlaceTile(){
        Tile tileToPlace = new Tile(1L, "road_junction_large", new int[]{2,2,2,2}, new int[]{1,2,1, 2,4,2, 1,2,1});
        GameBoard gameBoard = new GameBoard();
        assert(gameBoard.getPlaceablePositions().size() == 4); // 4 valid positions around the start tile
        assert(gameBoard.getPlacedTiles().size() == 1); // 1 tile placed on the board
        gameBoard.placeTile(tileToPlace, new Coordinates(14, 13)); // place the tile east from start tile
        assert(gameBoard.getPlaceablePositions().size() == 6); // 6 valid positions around the start tile
        assert(gameBoard.getPlacedTiles().size() == 2); // 2 tile placed on the board
        assert(gameBoard.getPlaceablePositions().contains(new Coordinates(14, 12))); // north of new tile
        assert(gameBoard.getPlaceablePositions().contains(new Coordinates(15, 13))); // east of new tile
        assert(gameBoard.getPlaceablePositions().contains(new Coordinates(14, 14))); // south of new tile
        assert(gameBoard.getPlaceablePositions().contains(new Coordinates(13, 12))); // north of new tile
        assert(gameBoard.getPlaceablePositions().contains(new Coordinates(13, 14))); // south of new tile
        assert(gameBoard.getPlaceablePositions().contains(new Coordinates(12, 13))); // west of new tile
    }


}
