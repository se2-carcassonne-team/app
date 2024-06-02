package se2.carcassonne.model;

import java.util.List;

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
public class Scoreboard {
    private Long gameSessionId;
    private Long gameLobbyId;
    private List<Long> playerIds;
    private List<String> playerNames;
}
