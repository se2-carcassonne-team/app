package se2.carcassonne.gameboard.model;

public class GameBoard {

    int id;

    int gameSessionId;

    public GameBoard() {
    }

    public GameBoard(int id, int gameSessionId) {
        this.id = id;
        this.gameSessionId = gameSessionId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getGameSessionId() {
        return gameSessionId;
    }

    public void setGameSessionId(int gameSessionId) {
        this.gameSessionId = gameSessionId;
    }
}
