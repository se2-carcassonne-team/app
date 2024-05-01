package se2.carcassonne.model;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Data
public class NextTurn {
    private Long playerId;
    private Long tileId;
}
