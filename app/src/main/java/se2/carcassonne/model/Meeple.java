package se2.carcassonne.model;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Meeple implements Serializable {
    Long id;
    PlayerColour color;
    Long playerId;
    boolean placed;
    Coordinates coordinates;
}
