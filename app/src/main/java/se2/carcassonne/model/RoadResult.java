package se2.carcassonne.model;

import java.util.List;
import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RoadResult {
    private boolean isRoadCompleted;
    private List<Tile> allPartsOfRoad;
    private Map<Long, Integer> points;
    private Map<Long, List<Meeple>> playersWithMeeplesOnRoad;

    public boolean hasMeepleOnRoad() {
        for (Tile tile : allPartsOfRoad) {
            Meeple potentialMeepleOnRoad = tile.getPlacedMeeple();
            if (potentialMeepleOnRoad != null) {
                int meeplePosition = (potentialMeepleOnRoad.getCoordinates().getYPosition() * 3) + potentialMeepleOnRoad.getCoordinates().getXPosition();
                // Meeple is on a road
                if (tile.getFeatures()[meeplePosition] == 2) {
                    return true;
                }
            }
        }
        return false;
    }
}
