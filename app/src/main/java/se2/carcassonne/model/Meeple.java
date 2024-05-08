package se2.carcassonne.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Meeple {
    Long id;
    PlayerColour color;
    Long playerId;
    boolean placed;
    Coordinates coordinates;
}
