package se2.carcassonne.model;

import java.util.ArrayList;

import lombok.Data;

@Data
public class GameBoard {

    // best way to streamline the game-board to make the checks as fast
    // idea:
    // 1) komplette Matrix
    // 2) Positionen der gesetzten Tiles
    // 3) Positionen der freien Tiles auf die generell ein Tile gesetzt werden könnte

    private Tile[][] gameBoardMatrix = new Tile[25][25];

    private ArrayList<Tile> placedTiles = new ArrayList<>(72);

    private ArrayList<Coordinates> placeablePositions = new ArrayList<>(72);

    private ArrayList<Tile> allTiles = new ArrayList<>();

    public GameBoard() {
        initializeGameBoard();
    }

    /**
     * Initializes the game board with the start tile at the center of the board.
     * Update all Lists and the gameBoardMatrix.
     */
    private void initializeGameBoard() {
        allTiles = TileInitializer.initializeTiles();
        Tile startTile = allTiles.get(0);
        placeTile(startTile, new Coordinates(13, 13));
    }

    public void placeTile(Tile tile, Coordinates coordinates) {
        int x = coordinates.getXPosition();
        int y = coordinates.getYPosition();

        tile.setCoordinates(coordinates);

        // place the tile on the gameBoardMatrix
        gameBoardMatrix[x][y] = tile;

        // remove the just placed tile's coordinates from the list of all placeablePositions
        placeablePositions.remove(coordinates);
        placedTiles.add(tile);

        // update placeablePositions based on the just placed tile
        updatePlaceablePositions();
    }

    private void updatePlaceablePositions() {
        ArrayList<Coordinates> newPlaceablePositions = new ArrayList<>(72);

        for (Tile tile : placedTiles) {
            Coordinates placedCoordinates = tile.getCoordinates();
            boolean[] surroundingFields = checkIfSurroundingFieldsAreEmpty(placedCoordinates);

            // Check if surrounding positions are empty and add them to newPlaceablePositions if they are
            if (surroundingFields[0]) { // North
                newPlaceablePositions.add(new Coordinates(placedCoordinates.getXPosition(), placedCoordinates.getYPosition() - 1));
            }
            if (surroundingFields[1]) { // East
                newPlaceablePositions.add(new Coordinates(placedCoordinates.getXPosition() + 1, placedCoordinates.getYPosition()));
            }
            if (surroundingFields[2]) { // South
                newPlaceablePositions.add(new Coordinates(placedCoordinates.getXPosition(), placedCoordinates.getYPosition() + 1));
            }
            if (surroundingFields[3]) { // West
                newPlaceablePositions.add(new Coordinates(placedCoordinates.getXPosition() - 1, placedCoordinates.getYPosition()));
            }
        }

        // Remove positions from placeablePositions that are no longer placeable
        placeablePositions.removeIf(position -> !newPlaceablePositions.contains(position));

        // Add positions to placeablePositions that have become placeable
        newPlaceablePositions.removeAll(placeablePositions);
        placeablePositions.addAll(newPlaceablePositions);
    }

    private boolean[] checkIfSurroundingFieldsAreEmpty(Coordinates coordinates) {
        int x = coordinates.getXPosition();
        int y = coordinates.getYPosition();

        boolean[] surroundingFields = new boolean [] {false, false, false, false}; // North, East, South, West

        // Check if the surrounding fields are within the game-board and empty
        if (y + 1 < 25 && gameBoardMatrix[x][y - 1] == null) {
            surroundingFields[0] = true; // North
        }
        if (x + 1 < 25 && gameBoardMatrix[x + 1][y] == null) {
            surroundingFields[1] = true; // East
        }
        if (y - 1 >= 0 && gameBoardMatrix[x][y + 1] == null) {
            surroundingFields[2] = true; // South
        }
        if (x - 1 >= 0 && gameBoardMatrix[x - 1][y] == null) {
            surroundingFields[3] = true; // West
        }
        return surroundingFields;
    }

    public ArrayList<Coordinates> highlightValidPositions(Tile tileToPlace) {
        ArrayList<Coordinates> validPositions = new ArrayList<>(72);

        for (Coordinates position : placeablePositions) {
            // Check if the tile can be placed at the position
            if (validPlacementForTileWithCurrentRotation(tileToPlace, position)){
                validPositions.add(position);
            }
        }
        return validPositions;
    }

    private boolean validPlacementForTileWithCurrentRotation(Tile tileToPlace, Coordinates position) {
        int x = position.getXPosition();
        int y = position.getYPosition();

        // Get the tiles edges based on it's current rotation
        int [] edges = tileToPlace.rotatedEdges(tileToPlace.getRotation());

        // Check if the north edge of the tile to place matches the south edge of the tile above
        if (gameBoardMatrix[x][y-1] != null) {
            return edges[0] == gameBoardMatrix[x][y - 1].getEdges()[2];
        }
        // Check if the east edge of the tile to place matches the west edge of the tile to the right
        if (gameBoardMatrix[x+1][y] != null) { // Check east
            return edges[1] == gameBoardMatrix[x + 1][y].getEdges()[3];
        }
        // Check if the south edge of the tile to place matches the north edge of the tile below
        if (gameBoardMatrix[x][y+1] != null) { // Check south
            return edges[2] == gameBoardMatrix[x][y + 1].getEdges()[0];
        }
        // Check if the west edge of the tile to place matches the east edge of the tile to the left
        if (gameBoardMatrix[x-1][y] != null) { // Check west
            return edges[3] == gameBoardMatrix[x - 1][y].getEdges()[1];
        }
        return false;
    }
}
