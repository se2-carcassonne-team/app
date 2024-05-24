package se2.carcassonne.model;


import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;


@Data
@Getter
@Setter
public class PointCalculator {
    private GameBoard gameBoard;
    private List<Set<Tile>> completedRoads = new ArrayList<>();

    // Points per tile type
    private static final int POINTS_CITY = 2;
    private static final int POINTS_ROAD = 1;
    private static final int POINTS_MONASTERY = 1;
    private static final int POINTS_FIELD = 3;

    public PointCalculator(GameBoard gameBoard) {
        this.gameBoard = gameBoard;
    }

    public RoadResult getAllTilesThatArePartOfRoad(Tile tile) {
        List<Tile> roadTiles = new ArrayList<>();
        Set<Tile> visited = new HashSet<>();
        Set<Tile> junctionsVisited = new HashSet<>();
        Map<Long, List<Meeple>> playersWithMeeplesOnRoadMeeples = null;

        int[] completedRoadsCount = new int[1]; // Use an array to manage completed roads count state across recursive calls
        if (tile.isCompletesRoads()) completedRoadsCount[0]++;


        roadTiles.add(tile);
        visited.add(tile);
        junctionsVisited.add(tile);

        // Depth-first search to find all tiles that are part of the road
        boolean isRoadCompleted = dfsCheckRoadCompleted(tile, roadTiles, visited, junctionsVisited,
                completedRoadsCount, tile.getCoordinates().getXPosition(), tile.getCoordinates().getYPosition());

        Set<Tile> completedRoadSet = new HashSet<>(roadTiles);
        Map<Long, Integer> pointsForPlayers = new HashMap<>();

        if (isRoadCompleted) {
            playersWithMeeplesOnRoadMeeples = findPlayersWithMeeplesOnRoad(roadTiles);
            pointsForPlayers = calculatePointsForCompletedRoad(roadTiles, playersWithMeeplesOnRoadMeeples);

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

        return new RoadResult(isRoadCompleted, roadTiles, pointsForPlayers, playersWithMeeplesOnRoadMeeples);
    }


    private boolean dfsCheckRoadCompleted(Tile tile, List<Tile> roadTiles, Set<Tile> visited, Set<Tile> junctionsVisited,
                                          int[] completedRoadsCount, int fromX, int fromY) {
        int x = tile.getCoordinates().getXPosition();
        int y = tile.getCoordinates().getYPosition();

        boolean foundCompletedRoad = false;

        for (int dx = -1; dx <= 1; dx++) {
            for (int dy = -1; dy <= 1; dy++) {
                // Skip diagonals and the current tile
                if ((dx == 0 && dy == 0) || Math.abs(dx) == Math.abs(dy)) {
                    continue;
                }

                if (dx == 1 && tile.rotatedEdges(tile.getRotation())[1] != 2) continue;
                if (dy == 1 && tile.rotatedEdges(tile.getRotation())[2] != 2) continue;
                if (dx == -1 && tile.rotatedEdges(tile.getRotation())[3] != 2) continue;
                if (dy == -1 && tile.rotatedEdges(tile.getRotation())[0] != 2) continue;

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
                            completedRoadsCount[0]++;
                            if (completedRoadsCount[0] == 2) {
                                return true; // Road completed
                            }
                        }

                        if (neighboringTile.getImageName().contains("junction")) {
                            junctionsVisited.add(neighboringTile);
                            continue; // Skip the recursive call for this direction if it's a junction
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

    private Map<Long, Integer> calculatePointsForCompletedRoad(List<Tile> roadTiles, Map<Long, List<Meeple>> playersWithMeeples) {
        Map<Long, Integer> playerPoints = new HashMap<>();
        if (roadTiles == null || roadTiles.isEmpty()) {
            return playerPoints;
        }

        // Remove duplicates from roadTiles
        Set<Tile> uniqueTiles = new HashSet<>(roadTiles);

        int totalPoints = uniqueTiles.size() * POINTS_ROAD;

        // Distribute points to each player based on their meeples on the road
        playersWithMeeples.forEach((playerId, meeples) -> {
            playerPoints.put(playerId, totalPoints);
        });

        return playerPoints;
    }


    private Map<Long, List<Meeple>> findPlayersWithMeeplesOnRoad(List<Tile> roadTiles) {
        Map<Long, List<Meeple>> playersWithMeeples = new HashMap<>();
        for (Tile tile : roadTiles) {
            Meeple meeple = tile.getPlacedMeeple();
            if (meeple != null) {
                Long playerId = meeple.getPlayerId();
                playersWithMeeples.putIfAbsent(playerId, new ArrayList<>());
                Objects.requireNonNull(playersWithMeeples.get(playerId)).add(meeple);
            }
        }
        return playersWithMeeples;
    }



}
