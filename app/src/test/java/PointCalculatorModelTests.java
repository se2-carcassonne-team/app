import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import se2.carcassonne.model.Coordinates;
import se2.carcassonne.model.GameBoard;
import se2.carcassonne.model.Meeple;
import se2.carcassonne.model.PlayerColour;
import se2.carcassonne.model.PointCalculator;
import se2.carcassonne.model.RoadResult;
import se2.carcassonne.model.Tile;
import se2.carcassonne.model.TileInitializer;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
public class PointCalculatorModelTests {
    private GameBoard gameBoard;
    private Tile[][] gameBoardMatrix;
    @BeforeEach
    public void setUp(){
        gameBoard = new GameBoard();
        gameBoardMatrix = gameBoard.getGameBoardMatrix();
    }

    @AfterEach
    public void tearDown() {
        // Reset game board and point calculator after each test
        gameBoard = null;
        gameBoardMatrix = null;
    }

    @Test
    public void testGetAllTilesThatArePartOfRoadForIncompleteRoadWithStartTile(){
        PointCalculator pointCalculator = new PointCalculator(gameBoard);
        RoadResult roadResult = pointCalculator.getAllTilesThatArePartOfRoad(gameBoardMatrix[12][12]);
        assertFalse(roadResult.isRoadCompleted());
    }

    @Test
    public void testGetAllTilesThatArePartOfRoadForIncompleteRoadWithStartAndOneCompletingTile(){
        // Junction to the right of start tile
        gameBoard.placeTile(gameBoard.getAllTiles().get(1), new Coordinates(13, 12));

        PointCalculator pointCalculator = new PointCalculator(gameBoard);
        RoadResult roadResult = pointCalculator.getAllTilesThatArePartOfRoad(gameBoardMatrix[13][12]);
        assertFalse(roadResult.isRoadCompleted());
    }

    @Test
    public void testGetAllTilesThatArePartOfRoadForCompleteRoadWithStartAndTwoCompletingTiles(){
        // Junction to the right of start tile
        gameBoard.placeTile(gameBoard.getAllTiles().get(1), new Coordinates(13, 12));

        // Junction to the left of start tile
        gameBoard.placeTile(gameBoard.getAllTiles().get(68), new Coordinates(11, 12));


        PointCalculator pointCalculator = new PointCalculator(gameBoard);
        RoadResult roadResult = pointCalculator.getAllTilesThatArePartOfRoad(gameBoardMatrix[11][12]);
        assertTrue(roadResult.isRoadCompleted());
    }

    @Test
    public void longerRoadCompletenessTest1(){
        // Large junction to the left of start tile
        gameBoard.placeTile(gameBoard.getAllTiles().get(1), new Coordinates(11, 12));

        // Upwards going road curve to the right of the starting tile
        Tile upwardCurve = gameBoard.getAllTiles().get(59);
        upwardCurve.setRotation(1);
        gameBoard.placeTile(upwardCurve, new Coordinates(13, 12));

        // Standard curve to the left from the new curve
        gameBoard.placeTile(gameBoard.getAllTiles().get(60), new Coordinates(13, 11));

        // Castle Wall Road, Upside Down, above start field
        Tile castleWallRoadUpsideDown = gameBoard.getAllTiles().get(42);
        castleWallRoadUpsideDown.setRotation(2);
        gameBoard.placeTile(castleWallRoadUpsideDown, new Coordinates(12, 11));

        // Downward going road curve to the left diagonal of the starting tile
        Tile downwardCurve = gameBoard.getAllTiles().get(61);
        downwardCurve.setRotation(3);
        gameBoard.placeTile(downwardCurve, new Coordinates(11, 11));

        PointCalculator pointCalculator = new PointCalculator(gameBoard);
        RoadResult roadResult = pointCalculator.getAllTilesThatArePartOfRoad(gameBoardMatrix[11][11]);
        assertTrue(roadResult.isRoadCompleted());
    }

    /**
     * Test for building a road with several sub roads and junctions
     * <a href="https://apricot-eustacia-35.tiiny.site">...</a>
     *
     */
    @Test
    public void buildRoadWithSeveralSubRoads(){
        PointCalculator calculator = new PointCalculator(gameBoard);

        // Large junction to the left of start tile
        gameBoard.placeTile(gameBoard.getAllTiles().get(1), new Coordinates(11, 12));
        calculator.setGameBoard(gameBoard);
        RoadResult result = calculator.getAllTilesThatArePartOfRoad(gameBoardMatrix[11][12]);
        // Road yet not completed
        assertFalse(result.isRoadCompleted());

        // Upwards going road curve to the right of the starting tile
        Tile upwardCurve = gameBoard.getAllTiles().get(59);
        upwardCurve.setRotation(1);
        gameBoard.placeTile(upwardCurve, new Coordinates(13, 12));
        calculator.setGameBoard(gameBoard);
        result = calculator.getAllTilesThatArePartOfRoad(gameBoardMatrix[13][12]);
        // Road still not completed
        assertFalse(result.isRoadCompleted());

        // Castle entry above road - road should be finished now
        gameBoard.placeTile(gameBoard.getAllTiles().get(3), new Coordinates(13,11));
        calculator.setGameBoard(gameBoard);
        result = calculator.getAllTilesThatArePartOfRoad(gameBoardMatrix[13][11]);
        assertTrue(result.isRoadCompleted());

        // Road to the left of large junction to the left of start tile - new sub road shall not be completed and not be part of the other road
        gameBoard.placeTile(gameBoard.getAllTiles().get(55), new Coordinates(10,12));
        calculator.setGameBoard(gameBoard);
        result = calculator.getAllTilesThatArePartOfRoad(gameBoardMatrix[10][12]);
        assertFalse(result.isRoadCompleted());

        // Small Road Junction to the left of the road above - new sub road shall be completed
        gameBoard.placeTile(gameBoard.getAllTiles().get(68), new Coordinates(9,12));
        calculator.setGameBoard(gameBoard);
        result = calculator.getAllTilesThatArePartOfRoad(gameBoardMatrix[9][12]);
        assertTrue(result.isRoadCompleted());

        // Road underneath Small Junction
        Tile road = gameBoard.getAllTiles().get(54);
        road.setRotation(1);
        gameBoard.placeTile(road, new Coordinates(9,13));
        calculator.setGameBoard(gameBoard);
        result = calculator.getAllTilesThatArePartOfRoad(gameBoardMatrix[9][13]);
        assertFalse(result.isRoadCompleted());

        // Monastery with road, upside down, underneath the road - completes road
        Tile monasteryRoad = gameBoard.getAllTiles().get(49);
        monasteryRoad.setRotation(2);
        gameBoard.placeTile(monasteryRoad, new Coordinates(9,14));
        calculator.setGameBoard(gameBoard);
        result = calculator.getAllTilesThatArePartOfRoad(gameBoardMatrix[9][14]);
        assertTrue(result.isRoadCompleted());


        gameBoard.placeTile(gameBoard.getAllTiles().get(70), new Coordinates(11,11));
        calculator.setGameBoard(gameBoard);
        result = calculator.getAllTilesThatArePartOfRoad(gameBoardMatrix[11][11]);
        assertTrue(result.isRoadCompleted());
    }

    @Test
    public void testMeepleRecognition(){
        PointCalculator calculator = new PointCalculator(gameBoard);

        // Large junction to the left of start tile
        gameBoard.placeTile(gameBoard.getAllTiles().get(1), new Coordinates(11, 12));
        calculator.setGameBoard(gameBoard);
        RoadResult result = calculator.getAllTilesThatArePartOfRoad(gameBoardMatrix[11][12]);
        // Road yet not completed
        assertFalse(result.isRoadCompleted());
        assertFalse(result.hasMeepleOnRoad());

        // Place Meeple on the road
        gameBoardMatrix[11][12].setPlacedMeeple(new Meeple(1L, PlayerColour.RED, 3L, true, new Coordinates(1,0)));

        // Place small junction above large junction
        gameBoard.placeTile(gameBoard.getAllTiles().get(70), new Coordinates(11,11));
        calculator.setGameBoard(gameBoard);
        result = calculator.getAllTilesThatArePartOfRoad(gameBoardMatrix[11][11]);
        assertTrue(result.isRoadCompleted());
        assertTrue(result.hasMeepleOnRoad());
    }


    @Test
    public void testMeepleNotOnRoadRecognition(){
        PointCalculator calculator = new PointCalculator(gameBoard);

        // Large junction to the left of start tile
        gameBoard.placeTile(gameBoard.getAllTiles().get(1), new Coordinates(11, 12));
        calculator.setGameBoard(gameBoard);
        RoadResult result = calculator.getAllTilesThatArePartOfRoad(gameBoardMatrix[11][12]);
        // Road yet not completed
        assertFalse(result.isRoadCompleted());
        assertFalse(result.hasMeepleOnRoad());

        // Place Meeple on the road
        gameBoardMatrix[11][12].setPlacedMeeple(new Meeple(1L, PlayerColour.RED, 3L, true, new Coordinates(0,0)));

        // Place small junction above large junction
        gameBoard.placeTile(gameBoard.getAllTiles().get(70), new Coordinates(11,11));
        calculator.setGameBoard(gameBoard);
        result = calculator.getAllTilesThatArePartOfRoad(gameBoardMatrix[11][11]);
        assertTrue(result.isRoadCompleted());
        assertFalse(result.hasMeepleOnRoad());
    }


    @Test
    public void testMeepleNotOnSameSubRoadRecognition(){
        PointCalculator calculator = new PointCalculator(gameBoard);

        // Large junction to the left of start tile
        gameBoard.placeTile(gameBoard.getAllTiles().get(1), new Coordinates(11, 12));
        calculator.setGameBoard(gameBoard);
        RoadResult result = calculator.getAllTilesThatArePartOfRoad(gameBoardMatrix[11][12]);
        // Road yet not completed
        assertFalse(result.isRoadCompleted());
        assertFalse(result.hasMeepleOnRoad());

        // Place Meeple on the road
        gameBoardMatrix[11][12].setPlacedMeeple(new Meeple(1L, PlayerColour.RED, 3L, true, new Coordinates(0,0)));

        // Place small junction above large junction
        gameBoard.placeTile(gameBoard.getAllTiles().get(70), new Coordinates(11,11));
        calculator.setGameBoard(gameBoard);
        result = calculator.getAllTilesThatArePartOfRoad(gameBoardMatrix[11][11]);
        assertTrue(result.isRoadCompleted());
        assertFalse(result.hasMeepleOnRoad());

        // Road to the left of large junction to the left of start tile - new sub road shall not be completed and not be part of the other road
        gameBoard.placeTile(gameBoard.getAllTiles().get(55), new Coordinates(10,12));
        calculator.setGameBoard(gameBoard);
        result = calculator.getAllTilesThatArePartOfRoad(gameBoardMatrix[10][12]);
        assertFalse(result.isRoadCompleted());
    }
}
