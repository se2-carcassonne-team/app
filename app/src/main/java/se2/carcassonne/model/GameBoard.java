package se2.carcassonne.model;

import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    private HashMap<Long, Integer> playerWithPoints = new HashMap<>();

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
        placeTile(startTile, new Coordinates(12, 12));
    }

    public void placeTile(Tile tile, Coordinates coordinates) {
        int x = coordinates.getXPosition();
        int y = coordinates.getYPosition();

        tile.setCoordinates(coordinates);

        /*
        // place the tile on the gameBoardMatrix with respect to rotation
         */

        //tile.setFeatures(tile.rotatedFeatures(tile.getRotation()));

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

    public boolean hasValidPositionForAnyRotation(Tile tile) {
        // Check for all rotations if there is a valid position
        for (int rotation = 0; rotation < 4; rotation++) {
            tile.setRotation(rotation);
            ArrayList<Coordinates> validPositions = highlightValidPositions(tile);
            if (!validPositions.isEmpty()) {
                tile.setRotation(0);
                return true;
            }
        }
        tile.setRotation(0);
        return false;
    }

    private boolean validPlacementForTileWithCurrentRotation(Tile tileToPlace, Coordinates position) {
        int x = position.getXPosition();
        int y = position.getYPosition();

        // Get the tiles edges based on it's current rotation
        int [] edges = tileToPlace.rotatedEdges(tileToPlace.getRotation());

        boolean northEdgeMatches = false;
        boolean eastEdgeMatches = false;
        boolean southEdgeMatches = false;
        boolean westEdgeMatches = false;

        // Check if the north edge of the tile to place matches the south edge of the tile above
        if (gameBoardMatrix[x][y-1] != null) {
            northEdgeMatches = edges[0] == gameBoardMatrix[x][y - 1].rotatedEdges(gameBoardMatrix[x][y - 1].getRotation())[2];
        } else {
            northEdgeMatches =true;
        }
        // Check if the east edge of the tile to place matches the west edge of the tile to the right
        if (gameBoardMatrix[x+1][y] != null) { // Check east
            eastEdgeMatches = edges[1] == gameBoardMatrix[x + 1][y].rotatedEdges(gameBoardMatrix[x + 1][y].getRotation())[3];
        } else {
            eastEdgeMatches =true;
        }
        // Check if the south edge of the tile to place matches the north edge of the tile below
        if (gameBoardMatrix[x][y+1] != null) { // Check south
            southEdgeMatches = edges[2] == gameBoardMatrix[x][y + 1].rotatedEdges(gameBoardMatrix[x][y + 1].getRotation())[0];
        } else {
            southEdgeMatches =true;
        }
        // Check if the west edge of the tile to place matches the east edge of the tile to the left
        if (gameBoardMatrix[x-1][y] != null) { // Check west
            westEdgeMatches = edges[3] == gameBoardMatrix[x - 1][y].rotatedEdges(gameBoardMatrix[x - 1][y].getRotation())[1];
        } else {
            westEdgeMatches =true;
        }

        return northEdgeMatches && eastEdgeMatches && southEdgeMatches && westEdgeMatches;
    }

    public void initGamePoints(List<Long> allPlayerIds){
        for (Long playerId : allPlayerIds) {
            playerWithPoints.put(playerId, 0);
        }
    }

    public void updatePoints(FinishedTurnDto finishedTurnDto) {
        // Check if the DTO contains any points data
        if (finishedTurnDto.getPoints() != null) {
            for (Map.Entry<Long, Integer> entry : finishedTurnDto.getPoints().entrySet()) {
                Long playerId = entry.getKey();
                Integer pointsToAdd = entry.getValue();

                // Check if the player already exists in the playerWithPoints map
                if (playerWithPoints.containsKey(playerId)) {
                    // If the player exists, add the new points to the existing ones
                    playerWithPoints.put(playerId, (playerWithPoints.get(playerId) + pointsToAdd));
                }
            }
        }
    }

    public Map<Long, Integer> finishedTurnRemoveMeeplesOnRoad(Map<Long, List<Meeple>> playersWithMeeples) {
        Map<Long, Integer> removedMeeplesCount = new HashMap<>();
        if (playersWithMeeples == null) {
            return removedMeeplesCount;
        }

        for (Map.Entry<Long, List<Meeple>> entry : playersWithMeeples.entrySet()) {
            Long playerId = entry.getKey();
            List<Meeple> meeplesToRemove = entry.getValue();
            int count = 0;

            for (Tile tile : placedTiles) {
                Meeple meeple = tile.getPlacedMeeple();
                if (meeple != null && meeplesToRemove.contains(meeple)){
                    tile.removeMeeple();  // Remove the meeple from the tile
                    count++;
                }
            }
            if (count > 0) {
                removedMeeplesCount.put(playerId, count);
            }
        }

        return removedMeeplesCount;
    }
}
