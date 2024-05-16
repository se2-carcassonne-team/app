package se2.carcassonne.model;

import java.util.HashMap;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ScoreBoard {
    private Long gameSessionId;
    private HashMap<Long, Integer> playerScores;

    public ScoreBoard() {
        this.playerScores = new HashMap<>();
    }

    public void addScore(long playerId, int score) {
        this.playerScores.put(playerId, score);
    }

    public Integer getScore(long playerId) {
        return this.playerScores.get(playerId);
    }

    public HashMap<Long, Integer> getAllScores() {
        return this.playerScores;
    }
}
