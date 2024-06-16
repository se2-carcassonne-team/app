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

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
class PointCalculatorModelTests {
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
    void testGetAllTilesThatArePartOfRoadForIncompleteRoadWithStartTile(){
        PointCalculator pointCalculator = new PointCalculator(gameBoard);
        RoadResult roadResult = pointCalculator.getAllTilesThatArePartOfRoad(gameBoardMatrix[12][12]);
        assertFalse(roadResult.isRoadCompleted());
    }

    @Test
    void testGetAllTilesThatArePartOfRoadForIncompleteRoadWithStartAndOneCompletingTile(){
        // Junction to the right of start tile
        gameBoard.placeTile(gameBoard.getAllTiles().get(1), new Coordinates(13, 12));

        PointCalculator pointCalculator = new PointCalculator(gameBoard);
        RoadResult roadResult = pointCalculator.getAllTilesThatArePartOfRoad(gameBoardMatrix[13][12]);
        assertFalse(roadResult.isRoadCompleted());
    }

    @Test
    void testGetAllTilesThatArePartOfRoadForCompleteRoadWithStartAndTwoCompletingTiles(){
        // Junction to the right of start tile
        gameBoard.placeTile(gameBoard.getAllTiles().get(1), new Coordinates(13, 12));

        // Junction to the left of start tile
        gameBoard.placeTile(gameBoard.getAllTiles().get(68), new Coordinates(11, 12));


        PointCalculator pointCalculator = new PointCalculator(gameBoard);
        RoadResult roadResult = pointCalculator.getAllTilesThatArePartOfRoad(gameBoardMatrix[11][12]);
        assertTrue(roadResult.isRoadCompleted());
    }

    @Test
    void longerRoadCompletenessTest1(){
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
    void buildRoadWithSeveralSubRoads(){
        PointCalculator calculator = new PointCalculator(gameBoard);

        // Large junction to the left of start tile
        gameBoard.placeTile(gameBoard.getAllTiles().get(1), new Coordinates(11, 12));
        RoadResult result = calculator.getAllTilesThatArePartOfRoad(gameBoardMatrix[11][12]);
        // Road yet not completed
        assertFalse(result.isRoadCompleted());

        // Upwards going road curve to the right of the starting tile
        Tile upwardCurve = gameBoard.getAllTiles().get(59);
        upwardCurve.setRotation(1);
        gameBoard.placeTile(upwardCurve, new Coordinates(13, 12));
        result = calculator.getAllTilesThatArePartOfRoad(gameBoardMatrix[13][12]);
        // Road still not completed
        assertFalse(result.isRoadCompleted());

        // Castle entry above road - road should be finished now
        gameBoard.placeTile(gameBoard.getAllTiles().get(3), new Coordinates(13,11));
        result = calculator.getAllTilesThatArePartOfRoad(gameBoardMatrix[13][11]);
        assertTrue(result.isRoadCompleted());

        // Road to the left of large junction to the left of start tile - new sub road shall not be completed and not be part of the other road
        gameBoard.getAllTiles().get(55).setRotation(1);
        gameBoard.placeTile(gameBoard.getAllTiles().get(55), new Coordinates(10,12));
        result = calculator.getAllTilesThatArePartOfRoad(gameBoardMatrix[10][12]);
        assertFalse(result.isRoadCompleted());

        // Small Road Junction to the left of the road above - new sub road shall be completed
        gameBoard.placeTile(gameBoard.getAllTiles().get(68), new Coordinates(9,12));
        result = calculator.getAllTilesThatArePartOfRoad(gameBoardMatrix[9][12]);
        assertTrue(result.isRoadCompleted());

        // Road underneath Small Junction
        gameBoard.placeTile(gameBoard.getAllTiles().get(54), new Coordinates(9,13));
        result = calculator.getAllTilesThatArePartOfRoad(gameBoardMatrix[9][13]);
        assertFalse(result.isRoadCompleted());

        // Monastery with road, upside down, underneath the road - completes road
        Tile monasteryRoad = gameBoard.getAllTiles().get(49);
        monasteryRoad.setRotation(2);
        gameBoard.placeTile(monasteryRoad, new Coordinates(9,14));
        result = calculator.getAllTilesThatArePartOfRoad(gameBoardMatrix[9][14]);
        assertTrue(result.isRoadCompleted());


        gameBoard.placeTile(gameBoard.getAllTiles().get(70), new Coordinates(11,11));
        result = calculator.getAllTilesThatArePartOfRoad(gameBoardMatrix[11][11]);
        assertTrue(result.isRoadCompleted());
    }

    @Test
    void buildRoadWithSeveralSubRoadsAndMultipleJunctions() {
        PointCalculator calculator = new PointCalculator(gameBoard);

        // Large junction to the left of start tile --> [11][12]
        gameBoard.placeTile(gameBoard.getAllTiles().get(1), new Coordinates(11, 12));
        RoadResult result = calculator.getAllTilesThatArePartOfRoad(gameBoardMatrix[11][12]);
        // Road yet not completed
        assertFalse(result.isRoadCompleted());
        assertTrue(result.getAllPartsOfRoad().contains(gameBoardMatrix[11][12]));
        assertTrue(result.getAllPartsOfRoad().contains(gameBoardMatrix[12][12]));
        assertEquals(2, result.getAllPartsOfRoad().size());


        // small junction to the top of the large junction --> [11][11]
        // completes the road from the large junction at [11][12] to the small junction at [11][11]
        gameBoard.placeTile(gameBoard.getAllTiles().get(68), new Coordinates(11,11));
        result = calculator.getAllTilesThatArePartOfRoad(gameBoardMatrix[11][11]);
        assertTrue(result.isRoadCompleted());
        assertTrue(result.getAllPartsOfRoad().contains(gameBoardMatrix[11][12]));
        assertTrue(result.getAllPartsOfRoad().contains(gameBoardMatrix[11][11]));
        assertEquals(2, result.getAllPartsOfRoad().size());


        // small junction to the right of the start tile --> [13][12] with rotation 1
        // completes the road from large junction at [11][12] to the small junction at [13][12] via the start tile at [12][12]
        gameBoard.getAllTiles().get(69).setRotation(1);
        gameBoard.placeTile(gameBoard.getAllTiles().get(69), new Coordinates(13, 12));
        result = calculator.getAllTilesThatArePartOfRoad(gameBoardMatrix[13][12]);
        assertTrue(result.isRoadCompleted());
        assertTrue(result.getAllPartsOfRoad().contains(gameBoardMatrix[11][12]));
        assertTrue(result.getAllPartsOfRoad().contains(gameBoardMatrix[12][12]));
        assertTrue(result.getAllPartsOfRoad().contains(gameBoardMatrix[13][12]));
        assertEquals(3, result.getAllPartsOfRoad().size());

        // curved road above the small junction at [13][12] --> curved road at [13][11] with rotation 0
        // road is incomplete!
        gameBoard.placeTile(gameBoard.getAllTiles().get(59), new Coordinates(13, 11));
        result = calculator.getAllTilesThatArePartOfRoad(gameBoardMatrix[13][11]);
        assertFalse(result.isRoadCompleted());
        assertTrue(result.getAllPartsOfRoad().contains(gameBoardMatrix[13][12]));
        assertTrue(result.getAllPartsOfRoad().contains(gameBoardMatrix[13][11]));
        assertEquals(2, result.getAllPartsOfRoad().size());

        // straight road above the start tile --> straight road at [12][11] with rotation 1
        // completes the road from the small junction at [13][12] to the small junction at [11][11] via the curved and straight road
        gameBoard.getAllTiles().get(51).setRotation(1);
        gameBoard.placeTile(gameBoard.getAllTiles().get(51), new Coordinates(12, 11));
        result = calculator.getAllTilesThatArePartOfRoad(gameBoardMatrix[12][11]);
        assertTrue(result.isRoadCompleted());
        assertTrue(result.getAllPartsOfRoad().contains(gameBoardMatrix[12][11]));
        assertTrue(result.getAllPartsOfRoad().contains(gameBoardMatrix[11][11]));
        assertTrue(result.getAllPartsOfRoad().contains(gameBoardMatrix[13][11]));
        assertTrue(result.getAllPartsOfRoad().contains(gameBoardMatrix[13][12]));
        assertEquals(4, result.getAllPartsOfRoad().size());
    }

    @Test
    void testMeepleRecognition(){
        PointCalculator calculator = new PointCalculator(gameBoard);

        // Large junction to the left of start tile
        gameBoard.placeTile(gameBoard.getAllTiles().get(1), new Coordinates(11, 12));
        RoadResult result = calculator.getAllTilesThatArePartOfRoad(gameBoardMatrix[11][12]);
        // Road yet not completed
        assertFalse(result.isRoadCompleted());
        assertFalse(result.hasMeepleOnRoad());

        // Place Meeple on the road
        gameBoardMatrix[11][12].setPlacedMeeple(new Meeple(1L, PlayerColour.RED, 3L, true, new Coordinates(1,0)));

        // Place small junction above large junction
        gameBoard.placeTile(gameBoard.getAllTiles().get(70), new Coordinates(11,11));
        result = calculator.getAllTilesThatArePartOfRoad(gameBoardMatrix[11][11]);
        assertTrue(result.isRoadCompleted());
        assertTrue(result.hasMeepleOnRoad());
    }


    @Test
    void testMeepleNotOnRoadRecognition(){
        PointCalculator calculator = new PointCalculator(gameBoard);

        // Large junction to the left of start tile
        gameBoard.placeTile(gameBoard.getAllTiles().get(1), new Coordinates(11, 12));
        RoadResult result = calculator.getAllTilesThatArePartOfRoad(gameBoardMatrix[11][12]);
        // Road yet not completed
        assertFalse(result.isRoadCompleted());
        assertFalse(result.hasMeepleOnRoad());

        // Place Meeple on the road
        gameBoardMatrix[11][12].setPlacedMeeple(new Meeple(1L, PlayerColour.RED, 3L, true, new Coordinates(0,0)));

        // Place small junction above large junction
        gameBoard.placeTile(gameBoard.getAllTiles().get(70), new Coordinates(11,11));
        result = calculator.getAllTilesThatArePartOfRoad(gameBoardMatrix[11][11]);
        assertTrue(result.isRoadCompleted());
        assertFalse(result.hasMeepleOnRoad());
    }


    @Test
    void testMeepleNotOnSameSubRoadRecognition(){
        PointCalculator calculator = new PointCalculator(gameBoard);

        // Large junction to the left of start tile
        gameBoard.placeTile(gameBoard.getAllTiles().get(1), new Coordinates(11, 12));
        RoadResult result = calculator.getAllTilesThatArePartOfRoad(gameBoardMatrix[11][12]);
        // Road yet not completed
        assertFalse(result.isRoadCompleted());
        assertFalse(result.hasMeepleOnRoad());

        // Place Meeple on the road
        gameBoardMatrix[11][12].setPlacedMeeple(new Meeple(1L, PlayerColour.RED, 3L, true, new Coordinates(0,0)));

        // Place small junction above large junction
        gameBoard.placeTile(gameBoard.getAllTiles().get(70), new Coordinates(11,11));
        result = calculator.getAllTilesThatArePartOfRoad(gameBoardMatrix[11][11]);
        assertTrue(result.isRoadCompleted());
        assertFalse(result.hasMeepleOnRoad());

        // Road to the left of large junction to the left of start tile - new sub road shall not be completed and not be part of the other road
        gameBoard.placeTile(gameBoard.getAllTiles().get(55), new Coordinates(10,12));
        result = calculator.getAllTilesThatArePartOfRoad(gameBoardMatrix[10][12]);
        assertFalse(result.isRoadCompleted());
    }

    @Test
    void quadrupleJunction(){
        PointCalculator calculator = new PointCalculator(gameBoard);

        // Large junction to the left of start tile
        gameBoard.placeTile(gameBoard.getAllTiles().get(1), new Coordinates(11, 12));
        RoadResult result = calculator.getAllTilesThatArePartOfRoad(gameBoardMatrix[11][12]);
        // Road yet not completed
        assertFalse(result.isRoadCompleted());

        gameBoard.placeTile(gameBoard.getAllTiles().get(68), new Coordinates(11,11));
        result = calculator.getAllTilesThatArePartOfRoad(gameBoardMatrix[11][11]);
        assertTrue(result.isRoadCompleted());
        assertTrue(result.getAllPartsOfRoad().contains(gameBoardMatrix[11][12]));
        assertTrue(result.getAllPartsOfRoad().contains(gameBoardMatrix[11][11]));


        gameBoard.placeTile(gameBoard.getAllTiles().get(69), new Coordinates(10, 11));
        result = calculator.getAllTilesThatArePartOfRoad(gameBoardMatrix[10][11]);
        assertTrue(result.isRoadCompleted());
        assertTrue(result.getAllPartsOfRoad().contains(gameBoardMatrix[11][11]));
        assertTrue(result.getAllPartsOfRoad().contains(gameBoardMatrix[10][11]));

        gameBoard.getAllTiles().get(70).setRotation(2);
        gameBoard.placeTile(gameBoard.getAllTiles().get(70), new Coordinates(10, 12));
        result = calculator.getAllTilesThatArePartOfRoad(gameBoardMatrix[10][12]);
        assertTrue(result.isRoadCompleted());
        assertTrue(result.getAllPartsOfRoad().contains(gameBoardMatrix[10][12]));
        assertTrue(result.getAllPartsOfRoad().contains(gameBoardMatrix[10][11]));

        Set<Tile> completedQuadrupleJunction = new HashSet<>();
        completedQuadrupleJunction.add(gameBoardMatrix[10][12]);
        completedQuadrupleJunction.add(gameBoardMatrix[11][12]);
        completedQuadrupleJunction.add(gameBoardMatrix[10][11]);
        completedQuadrupleJunction.add(gameBoardMatrix[11][11]);
        assertTrue(calculator.getCompletedRoads().contains(completedQuadrupleJunction));

        gameBoard.placeTile(gameBoard.getAllTiles().get(51), new Coordinates(11, 13));
        result = calculator.getAllTilesThatArePartOfRoad(gameBoardMatrix[11][13]);
        assertFalse(result.isRoadCompleted());

        gameBoard.getAllTiles().get(71).setRotation(2);
        gameBoard.placeTile(gameBoard.getAllTiles().get(71), new Coordinates(11,14));
        result = calculator.getAllTilesThatArePartOfRoad(gameBoardMatrix[11][14]);
        assertTrue(result.isRoadCompleted());
    }

    @Test
    void parallelStreets(){
        PointCalculator calculator = new PointCalculator(gameBoard);

        // Large junction to the right of start tile
        gameBoard.placeTile(gameBoard.getAllTiles().get(1), new Coordinates(13, 12));
        RoadResult result = calculator.getAllTilesThatArePartOfRoad(gameBoardMatrix[13][12]);
        // Road yet not completed
        assertFalse(result.isRoadCompleted());

        gameBoard.placeTile(gameBoard.getAllTiles().get(59), new Coordinates(13, 11));
        result = calculator.getAllTilesThatArePartOfRoad(gameBoardMatrix[13][11]);
        assertFalse(result.isRoadCompleted());

        gameBoard.getAllTiles().get(51).setRotation(1);
        gameBoard.placeTile(gameBoard.getAllTiles().get(51), new Coordinates(12, 11));
        result = calculator.getAllTilesThatArePartOfRoad(gameBoardMatrix[12][11]);
        assertFalse(result.isRoadCompleted());
        assertFalse(result.getAllPartsOfRoad().contains(gameBoardMatrix[12][12]));
        assertEquals(3,result.getAllPartsOfRoad().size());
    }

    //https://aauklagenfurt-my.sharepoint.com/:b:/g/personal/philippar_edu_aau_at/EXHyN-Is1ytFs1ipZ_Yy884BWovwT5BOO17p5RwFjX3nyw?e=V90GBp
    @Test
    void hugeJunctionConnectedWithJunctionFromEachSide(){
        PointCalculator calculator = new PointCalculator(gameBoard);

        gameBoard.placeTile(gameBoard.getAllTiles().get(1), new Coordinates(13, 12));
        RoadResult result = calculator.getAllTilesThatArePartOfRoad(gameBoardMatrix[13][12]);
        assertFalse(result.isRoadCompleted());

        gameBoard.placeTile(gameBoard.getAllTiles().get(59), new Coordinates(13, 11));
        result = calculator.getAllTilesThatArePartOfRoad(gameBoardMatrix[13][11]);
        assertFalse(result.isRoadCompleted());

        gameBoard.getAllTiles().get(51).setRotation(1);
        gameBoard.placeTile(gameBoard.getAllTiles().get(51), new Coordinates(12, 11));
        result = calculator.getAllTilesThatArePartOfRoad(gameBoardMatrix[12][11]);
        assertFalse(result.isRoadCompleted());
        assertFalse(result.getAllPartsOfRoad().contains(gameBoardMatrix[12][12]));
        assertEquals(3,result.getAllPartsOfRoad().size());

        gameBoard.getAllTiles().get(61).setRotation(3);
        gameBoard.placeTile(gameBoard.getAllTiles().get(61), new Coordinates(11, 11));
        result = calculator.getAllTilesThatArePartOfRoad(gameBoardMatrix[11][11]);
        assertFalse(result.isRoadCompleted());

        gameBoard.getAllTiles().get(68).setRotation(3);
        gameBoard.placeTile(gameBoard.getAllTiles().get(68), new Coordinates(11, 12));
        result = calculator.getAllTilesThatArePartOfRoad(gameBoardMatrix[11][12]);
        assertTrue(result.isRoadCompleted());

        gameBoard.getAllTiles().get(69).setRotation(1);
        gameBoard.placeTile(gameBoard.getAllTiles().get(69), new Coordinates(14, 12));
        result = calculator.getAllTilesThatArePartOfRoad(gameBoardMatrix[14][12]);
        assertTrue(result.isRoadCompleted());

        gameBoard.placeTile(gameBoard.getAllTiles().get(49), new Coordinates(14, 11));
        result = calculator.getAllTilesThatArePartOfRoad(gameBoardMatrix[14][11]);
        assertTrue(result.isRoadCompleted());

        gameBoard.getAllTiles().get(70).setRotation(2);
        gameBoard.placeTile(gameBoard.getAllTiles().get(70), new Coordinates(13, 13));
        result = calculator.getAllTilesThatArePartOfRoad(gameBoardMatrix[13][13]);
        assertTrue(result.isRoadCompleted());

        gameBoard.getAllTiles().get(60).setRotation(1);
        gameBoard.placeTile(gameBoard.getAllTiles().get(60), new Coordinates(14, 13));
        result = calculator.getAllTilesThatArePartOfRoad(gameBoardMatrix[14][13]);
        assertTrue(result.isRoadCompleted());

        gameBoard.getAllTiles().get(52).setRotation(1);
        gameBoard.placeTile(gameBoard.getAllTiles().get(52), new Coordinates(12, 13));
        result = calculator.getAllTilesThatArePartOfRoad(gameBoardMatrix[12][13]);
        assertFalse(result.isRoadCompleted());


        gameBoard.getAllTiles().get(67).setRotation(2);
        gameBoard.placeTile(gameBoard.getAllTiles().get(67), new Coordinates(11, 13));
        result = calculator.getAllTilesThatArePartOfRoad(gameBoardMatrix[11][13]);
        assertTrue(result.isRoadCompleted());
    }

    @Test
    void testRoadMeepleRecognitionForOneMeepleOnRoad(){
        PointCalculator calculator = new PointCalculator(gameBoard);
        // Meeple on start tile
        Meeple meeple = new Meeple(1L, PlayerColour.RED, 1L, true, new Coordinates(1,1));
        gameBoardMatrix[12][12].setPlacedMeeple(meeple);

        // Large junction to the left of start tile
        gameBoard.placeTile(gameBoard.getAllTiles().get(1), new Coordinates(11, 12));
        RoadResult result = calculator.getAllTilesThatArePartOfRoad(gameBoardMatrix[11][12]);
        // Road yet not completed
        assertFalse(result.isRoadCompleted());
        assertTrue(result.hasMeepleOnRoad());

        // Place small junction to the right of start tile
        gameBoard.placeTile(gameBoard.getAllTiles().get(69), new Coordinates(13, 12));
        result = calculator.getAllTilesThatArePartOfRoad(gameBoardMatrix[13][12]);
        assertTrue(result.isRoadCompleted());
        assertTrue(result.hasMeepleOnRoad());
        assertTrue(result.getPlayersWithMeeplesOnRoad().containsKey(1L));
        assertTrue(Objects.requireNonNull(result.getPlayersWithMeeplesOnRoad().get(1L)).contains(meeple));
        assertEquals(1, Objects.requireNonNull(result.getPlayersWithMeeplesOnRoad().get(1L)).size());
    }

    @Test
    void testRoadMeepleRecognitionForTwoMeeplesOnSameRoad(){
        PointCalculator pointCalculator = new PointCalculator(gameBoard);
        // Meeple on start tile for Player 1
        Meeple meeple1 = new Meeple(1L, PlayerColour.RED, 1L, true, new Coordinates(1,1));
        gameBoardMatrix[12][12].setPlacedMeeple(meeple1);

        // Large junction to the left of start tile
        gameBoard.placeTile(gameBoard.getAllTiles().get(1), new Coordinates(11, 12));

        // Downward going road curve to the left diagonal of the starting tile
        Tile downwardCurve = gameBoard.getAllTiles().get(61);
        downwardCurve.setRotation(3);
        gameBoard.placeTile(downwardCurve, new Coordinates(11, 11));

        // Meeple on the downward going road for Player 2
        Meeple meeple2 = new Meeple(2L, PlayerColour.BLUE, 2L, true, new Coordinates(1,1));
        gameBoardMatrix[11][11].setPlacedMeeple(meeple2);

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

        RoadResult roadResult = pointCalculator.getAllTilesThatArePartOfRoad(gameBoardMatrix[12][11]);
        assertTrue(roadResult.isRoadCompleted());
        assertTrue(roadResult.hasMeepleOnRoad());
        assertEquals(2, roadResult.getPlayersWithMeeplesOnRoad().size());

        assertTrue(roadResult.getPlayersWithMeeplesOnRoad().containsKey(1L));
        assertTrue(roadResult.getPlayersWithMeeplesOnRoad().containsKey(2L));
        assertTrue(Objects.requireNonNull(roadResult.getPlayersWithMeeplesOnRoad().get(1L)).contains(meeple1));
        assertTrue(Objects.requireNonNull(roadResult.getPlayersWithMeeplesOnRoad().get(2L)).contains(meeple2));

        // Ensure both players are recognized and have received points
        assertTrue(roadResult.getPoints().containsKey(1L));
        assertTrue(roadResult.getPoints().containsKey(2L));

        assertEquals(6, (int) roadResult.getPoints().get(1L));
        assertEquals(6, (int) roadResult.getPoints().get(2L));
    }

    @Test
    void recognizeCycleThatCompletesRoad(){
        PointCalculator pointCalculator = new PointCalculator(gameBoard);
        // Downward going curve to the right of start tile
        gameBoard.placeTile(gameBoard.getAllTiles().get(59), new Coordinates(13, 12));

        // Downward going curve to the left of start tile
        gameBoard.placeTile(gameBoard.getAllTiles().get(36), new Coordinates(11, 12));

        gameBoard.getAllTiles().get(60).setRotation(2);
        gameBoard.placeTile(gameBoard.getAllTiles().get(60), new Coordinates(11, 13));

        gameBoard.getAllTiles().get(61).setRotation(1);
        gameBoard.placeTile(gameBoard.getAllTiles().get(61), new Coordinates(13, 13));

        gameBoard.getAllTiles().get(51).setRotation(1);
        gameBoard.placeTile(gameBoard.getAllTiles().get(51), new Coordinates(12, 13));

        RoadResult roadResult = pointCalculator.getAllTilesThatArePartOfRoad(gameBoardMatrix[12][13]);
        assertTrue(roadResult.isRoadCompleted());
    }

//    @Test
//    void meepleNotActuallyOnRoadButOnCompletedRoad(){
//        PointCalculator pointCalculator = new PointCalculator(gameBoard);
//
//        // Junction to the left of start tile
//        gameBoard.placeTile(gameBoard.getAllTiles().get(39), new Coordinates(11, 12));
//        Meeple meeple1 = new Meeple(1L, PlayerColour.RED, 1L, true, new Coordinates(1,0));
//        gameBoardMatrix[11][12].setPlacedMeeple(meeple1);
//
//        gameBoard.getAllTiles().get(42).setRotation(1);
//        gameBoard.placeTile(gameBoard.getAllTiles().get(42), new Coordinates(11, 13));
//        Meeple meeple2 = new Meeple(2L, PlayerColour.RED, 2L, true, new Coordinates(2,1));
//        gameBoardMatrix[11][13].setPlacedMeeple(meeple2);
//
//        gameBoard.placeTile(gameBoard.getAllTiles().get(51), new Coordinates(11, 14));
//
//        gameBoard.getAllTiles().get(49).setRotation(2);
//        gameBoard.placeTile(gameBoard.getAllTiles().get(49), new Coordinates(11, 15));
//
//        RoadResult roadResult = pointCalculator.getAllTilesThatArePartOfRoad(gameBoardMatrix[11][15]);
//        assertTrue(roadResult.isRoadCompleted());
//
//        assertFalse(roadResult.getPlayersWithMeeplesOnRoad().containsKey(1L));
//        assertFalse(roadResult.getPlayersWithMeeplesOnRoad().containsKey(2L));
//
//    }
//
//
//    @Test
//    void meepleOnMonasteryRoadRecognition() {
//        PointCalculator pointCalculator = new PointCalculator(gameBoard);
//
//        gameBoard.getAllTiles().get(49).setRotation(1);
//        gameBoard.placeTile(gameBoard.getAllTiles().get(49), new Coordinates(13, 12));
//        Meeple meeple = new Meeple(1L, PlayerColour.RED, 1L, true, new Coordinates(0,1));
//        gameBoardMatrix[13][12].setPlacedMeeple(meeple);
//
//        RoadResult result = pointCalculator.getAllTilesThatArePartOfRoad(gameBoardMatrix[13][12]);
//        assertFalse(result.isRoadCompleted());
//        assertTrue(result.hasMeepleOnRoad());
//
//    }
}
