package se2.carcassonne.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PlacedTileDto {
    // Game-Session id
    private Long gameSessionId;

    // Tile id
    private Long tileId;

    // tile coordinates
    private Coordinates coordinates;

    // tile rotation
    private Integer rotation;

    // Meeple
    private Meeple placedMeeple;
}
