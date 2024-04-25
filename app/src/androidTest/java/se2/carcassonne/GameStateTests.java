package se2.carcassonne;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import se2.carcassonne.model.GameState;

public class GameStateTests {
    @Test
    public void testGameStates() {
        assertEquals(GameState.LOBBY, GameState.valueOf("LOBBY"));
        assertEquals(GameState.IN_GAME, GameState.valueOf("IN_GAME"));
        assertEquals(GameState.FINISHED, GameState.valueOf("FINISHED"));
    }
}
