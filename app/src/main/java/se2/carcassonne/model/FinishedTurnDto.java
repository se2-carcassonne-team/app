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
public class FinishedTurnDto {
    private Long gameSessionId;
    private Map<Long, Integer> points;
    private Map<Long, List<Meeple>> playersWithMeeples;
}
