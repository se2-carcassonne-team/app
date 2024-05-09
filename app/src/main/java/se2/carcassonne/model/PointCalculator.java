package se2.carcassonne.model;


import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
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
    // private Tile[][] gameBoardMatrix;
    private List<Set<Tile>> completedRoads = new ArrayList<>();

    // Points per tile type
    private static final int POINTS_CITY = 2;
    private static final int POINTS_ROAD = 1;
    private static final int POINTS_MONASTERY = 1;
    private static final int POINTS_FIELD = 3;

    public PointCalculator(GameBoard gameBoard) {
        this.gameBoard = gameBoard;
        // this.gameBoardMatrix = gameBoard.getGameBoardMatrix();
    }

    public void setGameBoard(GameBoard gameBoard) {
        this.gameBoard = gameBoard;
        // this.gameBoardMatrix = gameBoard.getGameBoardMatrix();
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

        // Depth-first search to find all tiles that are part of the road
        boolean isRoadCompleted = dfsCheckRoadCompleted(tile, roadTiles, visited, junctionsVisited,
                completedRoadsCount, tile.getCoordinates().getXPosition(), tile.getCoordinates().getYPosition());

        Set<Tile> completedRoadSet = new HashSet<>(roadTiles);
        if (isRoadCompleted) {
            // Check for merging with existing roads
            Set<Tile> mergedRoad = new HashSet<>();
            boolean isSubset = false;

            for (Iterator<Set<Tile>> iterator = completedRoads.iterator(); iterator.hasNext(); ) {
                Set<Tile> existingRoad = iterator.next();
                if (!Collections.disjoint(completedRoadSet, existingRoad)) {
                    // They share at least one common tile, merge them
                    mergedRoad.addAll(existingRoad);
                    iterator.remove();
                } else if (existingRoad.containsAll(completedRoadSet)) {
                    // The newly completed road is a subset of an existing road
                    isSubset = true;
                    break;
                }
            }

            if (!isSubset) {
                if (!mergedRoad.isEmpty()) {
                    // Add all tiles from the new road to the merged road and add it back to completedRoads
                    mergedRoad.addAll(completedRoadSet);
                    completedRoads.add(mergedRoad);
                } else {
                    // If no merging is required, add the new completed road directly
                    completedRoads.add(completedRoadSet);
                }
            }

            // Mark the tiles as road completed, except junctions
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

        boolean foundCompletedRoad = false;

        for (int dx = -1; dx <= 1; dx++) {
            for (int dy = -1; dy <= 1; dy++) {
                // Skip diagonals and the current tile
                if ((dx == 0 && dy == 0) || Math.abs(dx) == Math.abs(dy)) {
                    continue;
                }

                if (dx == 1){
                    if(tile.rotatedEdges(tile.getRotation())[1] != 2) continue;
                } else if (dy == 1) {
                    if (tile.rotatedEdges(tile.getRotation())[2] != 2) continue;
                } else if (dx == -1) {
                    if (tile.rotatedEdges(tile.getRotation())[3] != 2) continue;
                } else if (dy == -1) {
                    if (tile.rotatedEdges(tile.getRotation())[0] != 2) continue;
                }

                int newX = x + dx;
                int newY = y + dy;

                if (isValidPosition(newX, newY)) {
                    if (newX == fromX && newY == fromY) {
                        continue;
                    }

                    Tile neighboringTile = gameBoard.getGameBoardMatrix()[newX][newY];

                    // Check for valid road tile that hasn't been visited yet or is a junction that needs revisiting
                    if (neighboringTile != null && hasRoad(neighboringTile) && (!visited.contains(neighboringTile)
                            || (junctionsVisited.contains(neighboringTile) && roadTiles.contains(neighboringTile)))) {

                        if (neighboringTile.isRoadCompleted()) continue;

                        roadTiles.add(neighboringTile);
                        visited.add(neighboringTile);

                        if (neighboringTile.isCompletesRoads()) {
                            completedRoadsCount++;
                            if (completedRoadsCount == 2) {
                                return true; // Road completed
                            }
                        }

                        if (neighboringTile.getImageName().contains("junction")) {
                            if (visited.contains(neighboringTile) && !junctionsVisited.contains(neighboringTile)) {
                                junctionsVisited.add(neighboringTile);
                            }
                            // If it's a junction, skip the recursive call for this direction
                            continue;
                        }

                        // Recursively explore the neighboring tile
                        foundCompletedRoad = dfsCheckRoadCompleted(neighboringTile, roadTiles, visited, junctionsVisited, completedRoadsCount, x, y) || foundCompletedRoad;
                    }
                }
            }
        }

        return foundCompletedRoad;
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
