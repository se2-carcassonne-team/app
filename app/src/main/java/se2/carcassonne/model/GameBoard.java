package se2.carcassonne.model;

import java.util.ArrayList;

public class GameBoard {

    // best way to streamline the game-board to make the checks as fast
    // idea:
    // 1) komplette Matrix
    // 2) Positionen der gesetzten Tiles
    // 3) Positionen der freien Tiles auf die generell ein Tile gesetzt werden könnte

    private Tile[][] gameBoardMatrix = new Tile[25][25];

    private ArrayList<Tile> placedTiles = new ArrayList<>(72);

    private ArrayList<Coordinates> placeablePositions = new ArrayList<>();

    public GameBoard() {
        // initialize gameBoard - same every time!
        gameBoardMatrix[12][12] = new Tile(0L, "startTile", new int[]{}, new int[]{});

        // TODO: initialize the two lists

    }

    public void placeTile(Tile tile) {
        // TODO: implement
        // update gameBoardMatrix, placedTiles, placablePositions via updatePlaceablePosition()
    }

    private void updatePlaceablePositions() {
        // TODO: update list of placeable positions after adding newly placed tile to placedTiles list
    }

    private boolean[] checkIfSurroundingFieldsAreEmpty(Coordinates coordinates) {
        // TODO: check if the four surrounding fields are empty
        // TODO: check if we are already on the edge of the gameBoard
        // return array of size four (north, east, south, west)
        return null;
    }

    public Coordinates[] highlightValidPositions(Tile tileToPlace) {
        // TODO: iterate over the placeablePositions list to find on which positions the tile can be placed (with the current rotation)
        return null;
    }


}
