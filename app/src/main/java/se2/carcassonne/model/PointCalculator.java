package se2.carcassonne.model;


import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;


@Data
@Getter
@Setter
public class PointCalculator {
    private GameBoard gameBoard;
    private Tile[][] gameBoardMatrix;
    private List<Set<Tile>> completedRoads = new ArrayList<>();

    // Points per tile type
    private static final int POINTS_CITY = 2;
    private static final int POINTS_ROAD = 1;
    private static final int POINTS_MONASTERY = 1;
    private static final int POINTS_FIELD = 3;

    public PointCalculator(GameBoard gameBoard) {
        this.gameBoard = gameBoard;
        this.gameBoardMatrix = gameBoard.getGameBoardMatrix();
    }

    public void setGameBoard(GameBoard gameBoard) {
        this.gameBoard = gameBoard;
        this.gameBoardMatrix = gameBoard.getGameBoardMatrix();
    }

    public RoadResult getAllTilesThatArePartOfRoad(Tile tile) {
        List<Tile> roadTiles = new ArrayList<>();
        Set<Tile> visited = new HashSet<>();
        Set<Tile> junctionsVisited = new HashSet<>();
        int completedRoadsCount = 0;

        if (tile.isCompletesRoads()) completedRoadsCount++;

        roadTiles.add(tile);
        visited.add(tile);
        junctionsVisited.add(tile);


        // depth-first-search to find all tiles that are part of the road
        boolean isRoadCompleted = dfsCheckRoadCompleted(tile, roadTiles, visited, junctionsVisited,
                completedRoadsCount, tile.getCoordinates().getXPosition(), tile.getCoordinates().getYPosition());

        if (isRoadCompleted) {
            // Check if the completed road is a superset of any existing subroad
            for (Set<Tile> road : completedRoads) {
                if (roadTiles.containsAll(road)) {
                    // The completed road is a superset of an existing road
                    isRoadCompleted = false; // Invalid road
                    break;
                }
            }

            // Check if the completed road forms a subset of any existing subroad
            if (isRoadCompleted) {
                Set<Tile> completedRoadSet = new HashSet<>(roadTiles);
                for (Set<Tile> road : completedRoads) {
                    if (road.containsAll(completedRoadSet)) {
                        // The completed road is a subset of an existing road
                        isRoadCompleted = false; // Invalid road
                        break;
                    }
                }
            }

            // If the completed road is valid, add it to the list of completed subroads
            if (isRoadCompleted) {
                completedRoads.add(new HashSet<>(roadTiles));
            }
        }

        // Mark the tiles as road completed
        if (isRoadCompleted) {
            for (Tile roadTile : roadTiles) {
                if (!roadTile.getImageName().contains("junction")) {
                    roadTile.setRoadCompleted(true);
                }
            }
        }

        return new RoadResult(isRoadCompleted, roadTiles);
    }

    private boolean dfsCheckRoadCompleted(Tile tile, List<Tile> roadTiles, Set<Tile> visited, Set<Tile> junctionsVisited,
                                          int completedRoadsCount, int fromX, int fromY) {
        int x = tile.getCoordinates().getXPosition();
        int y = tile.getCoordinates().getYPosition();

        for (int dx = -1; dx <= 1; dx++) {
            for (int dy = -1; dy <= 1; dy++) {
                // Skip diagonals and the current tile
                if ((dx == 0 && dy == 0) || Math.abs(dx) == Math.abs(dy)) {
                    continue;
                }
                int newX = x + dx;
                int newY = y + dy;

                if (isValidPosition(newX, newY)) {
                    if (newX == fromX && newY == fromY) {
                        continue;
                    }

                    Tile neighboringTile = gameBoardMatrix[newX][newY];

                    // If the neighboring tile is part of the road and has not been visited or is a junction revisited for the second time
                    if (neighboringTile != null && hasRoad(neighboringTile) && (!visited.contains(neighboringTile)
                            || (junctionsVisited.contains(neighboringTile) && roadTiles.contains(neighboringTile)))) {

                        if (neighboringTile.isRoadCompleted()) continue;

                        roadTiles.add(neighboringTile);
                        visited.add(neighboringTile);

                        // If the neighboring tile completes roads, increment the count
                        if (neighboringTile.isCompletesRoads()) {
                            completedRoadsCount++;
                            if (completedRoadsCount == 2) {
                                return true; // Road completed
                            }
                        }

                        // Check if the neighboring tile is some sort of junction so roads can come from several directions
                        if (neighboringTile.getImageName().contains("junction")) {
                            // If it's a junction and it's already been visited once, mark it as visited again
                            if (visited.contains(neighboringTile) && !junctionsVisited.contains(neighboringTile)) {
                                junctionsVisited.add(neighboringTile);
                            }
                        }

                        // Recursively explore the neighboring tile
                        if (dfsCheckRoadCompleted(neighboringTile, roadTiles, visited, junctionsVisited, completedRoadsCount, x, y)) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    private boolean hasRoad(Tile neighboringTile) {
        boolean hasRoad = false;
        for (int edge : neighboringTile.getEdges()) {
            hasRoad = hasRoad || edge == 2;
        }
        return hasRoad;
    }

    private boolean isValidPosition(int x, int y) {
        return x >= 0 && x < 25 && y >= 0 && y < 25;
    }
}
