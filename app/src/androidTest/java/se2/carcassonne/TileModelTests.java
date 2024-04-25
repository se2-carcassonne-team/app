package se2.carcassonne;


import static org.junit.Assert.assertThrows;

import org.junit.Test;

import se2.carcassonne.model.Tile;

public class TileModelTests {
    @Test
    public void testTileConstructor() {
        Tile tile = new Tile(0L,"castle_wall_road", new int[]{3,2,1,2}, new int[]{3,3,3, 2,2,2, 1,1,1});
        assert(tile.getId() == 0L);
        assert(tile.getImageName().equals("castle_wall_road"));
        assert(tile.getRotation() == 0);
        assert(tile.getEdges()[0] == 3);
        assert(tile.getEdges()[1] == 2);
        assert(tile.getEdges()[2] == 1);
        assert(tile.getEdges()[3] == 2);
        assert(tile.getFeatures()[0] == 3);
        assert(tile.getFeatures()[1] == 3);
        assert(tile.getFeatures()[2] == 3);
        assert(tile.getFeatures()[3] == 2);
        assert(tile.getFeatures()[4] == 2);
        assert(tile.getFeatures()[5] == 2);
        assert(tile.getFeatures()[6] == 1);
        assert(tile.getFeatures()[7] == 1);
        assert(tile.getFeatures()[8] == 1);
        assert(tile.getCoordinates() == null);
    }

    @Test
    public void testRotateEdgesDegree0() {
        Tile tile = new Tile(0L,"castle_wall_road", new int[]{3,2,1,2}, new int[]{3,3,3, 2,2,2, 1,1,1});
        int[] rotatedEdges = tile.rotatedEdges(0);
        assert(rotatedEdges[0] == 3);
        assert(rotatedEdges[1] == 2);
        assert(rotatedEdges[2] == 1);
        assert(rotatedEdges[3] == 2);
    }

    @Test
    public void testRotateEdgesDegree90() {
        Tile tile = new Tile(0L,"castle_wall_road", new int[]{3,2,1,2}, new int[]{3,3,3, 2,2,2, 1,1,1});
        int[] rotatedEdges = tile.rotatedEdges(1);
        assert(rotatedEdges[0] == 2);
        assert(rotatedEdges[1] == 3);
        assert(rotatedEdges[2] == 2);
        assert(rotatedEdges[3] == 1);
    }

    @Test
    public void testRotateEdgesDegree180() {
        Tile tile = new Tile(0L,"castle_wall_road", new int[]{3,2,1,2}, new int[]{3,3,3, 2,2,2, 1,1,1});
        int[] rotatedEdges = tile.rotatedEdges(2);
        assert(rotatedEdges[0] == 1);
        assert(rotatedEdges[1] == 2);
        assert(rotatedEdges[2] == 3);
        assert(rotatedEdges[3] == 2);
    }

    @Test
    public void testRotateEdgesDegree270() {
        Tile tile = new Tile(0L,"castle_wall_road", new int[]{3,2,1,2}, new int[]{3,3,3, 2,2,2, 1,1,1});
        int[] rotatedEdges = tile.rotatedEdges(3);
        assert(rotatedEdges[0] == 2);
        assert(rotatedEdges[1] == 1);
        assert(rotatedEdges[2] == 2);
        assert(rotatedEdges[3] == 3);
    }

    @Test
    public void testRotateEdgesInvalidRotation() {
        Tile tile = new Tile(0L,"castle_wall_road", new int[]{3,2,1,2}, new int[]{3,3,3, 2,2,2, 1,1,1});
        assertThrows(RuntimeException.class, () -> tile.rotatedEdges(4));
    }

    @Test
    public void testRotateFeaturesDegree0() {
        Tile tile = new Tile(0L,"castle_wall_road", new int[]{3,2,1,2}, new int[]{3,3,3, 2,2,2, 1,1,1});
        int [] rotatedFeatures = tile.rotatedFeatures(0);
        assert (rotatedFeatures[0] == 3);
        assert (rotatedFeatures[1] == 3);
        assert (rotatedFeatures[2] == 3);
        assert (rotatedFeatures[3] == 2);
        assert (rotatedFeatures[4] == 2);
        assert (rotatedFeatures[5] == 2);
        assert (rotatedFeatures[6] == 1);
        assert (rotatedFeatures[7] == 1);
        assert (rotatedFeatures[8] == 1);
    }

    @Test
    public void testRotateFeaturesDegree90() {
        Tile tile = new Tile(0L,"castle_wall_road", new int[]{3,2,1,2}, new int[]{3,3,3, 2,2,2, 1,1,1});
        int [] rotatedFeatures = tile.rotatedFeatures(1);
        assert (rotatedFeatures[0] == 1);
        assert (rotatedFeatures[1] == 2);
        assert (rotatedFeatures[2] == 3);
        assert (rotatedFeatures[3] == 1);
        assert (rotatedFeatures[4] == 2);
        assert (rotatedFeatures[5] == 3);
        assert (rotatedFeatures[6] == 1);
        assert (rotatedFeatures[7] == 2);
        assert (rotatedFeatures[8] == 3);
    }

    @Test
    public void testRotateFeaturesDegree180() {
        Tile tile = new Tile(0L,"castle_wall_road", new int[]{3,2,1,2}, new int[]{3,3,3, 2,2,2, 1,1,1});
        int [] rotatedFeatures = tile.rotatedFeatures(2);
        assert (rotatedFeatures[0] == 1);
        assert (rotatedFeatures[1] == 1);
        assert (rotatedFeatures[2] == 1);
        assert (rotatedFeatures[3] == 2);
        assert (rotatedFeatures[4] == 2);
        assert (rotatedFeatures[5] == 2);
        assert (rotatedFeatures[6] == 3);
        assert (rotatedFeatures[7] == 3);
        assert (rotatedFeatures[8] == 3);
    }

    @Test
    public void testRotateFeaturesDegree270() {
        Tile tile = new Tile(0L,"castle_wall_road", new int[]{3,2,1,2}, new int[]{3,3,3, 2,2,2, 1,1,1});
        int [] rotatedFeatures = tile.rotatedFeatures(3);
        assert (rotatedFeatures[0] == 3);
        assert (rotatedFeatures[1] == 2);
        assert (rotatedFeatures[2] == 1);
        assert (rotatedFeatures[3] == 3);
        assert (rotatedFeatures[4] == 2);
        assert (rotatedFeatures[5] == 1);
        assert (rotatedFeatures[6] == 3);
        assert (rotatedFeatures[7] == 2);
        assert (rotatedFeatures[8] == 1);
    }

    @Test
    public void testRotateFeaturesInvalidRotation() {
        Tile tile = new Tile(0L,"castle_wall_road", new int[]{3,2,1,2}, new int[]{3,3,3, 2,2,2, 1,1,1});
        assertThrows(RuntimeException.class, () -> tile.rotatedFeatures(4));
    }
}
