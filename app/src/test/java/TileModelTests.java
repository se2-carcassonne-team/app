import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import se2.carcassonne.model.Tile;

class TileModelTests {
    @Test
    void testTileConstructor() {
        Tile tile = new Tile(0L, "castle_wall_road", new int[]{3, 2, 1, 2}, new int[]{3, 3, 3, 2, 2, 2, 1, 1, 1}, new boolean[]{false, false, false, false, false, false, false, false, false}, false);
        assertEquals(0L, tile.getId());
        assertEquals("castle_wall_road", tile.getImageName());
        assertEquals(0, tile.getRotation());
        assertArrayEquals(new int[]{3, 2, 1, 2}, tile.getEdges());
        assertArrayEquals(new int[]{3, 3, 3, 2, 2, 2, 1, 1, 1}, tile.getFeatures());
        assertNull(tile.getCoordinates());
    }

    @Test
    void testRotateEdgesDegree0() {
        Tile tile = new Tile(0L, "castle_wall_road", new int[]{3, 2, 1, 2}, new int[]{3, 3, 3, 2, 2, 2, 1, 1, 1},  new boolean[]{false, false, false, false, false, false, false, false, false}, false);
        int[] rotatedEdges = tile.rotatedEdges(0);
        assertArrayEquals(new int[]{3, 2, 1, 2}, rotatedEdges);
    }

    @Test
    void testRotateEdgesDegree90() {
        Tile tile = new Tile(0L, "castle_wall_road", new int[]{3, 2, 1, 2}, new int[]{3, 3, 3, 2, 2, 2, 1, 1, 1}, new boolean[]{false, false, false, false, false, false, false, false, false}, false);
        int[] rotatedEdges = tile.rotatedEdges(1);
        assertArrayEquals(new int[]{2, 3, 2, 1}, rotatedEdges);
    }

    @Test
    void testRotateEdgesDegree180() {
        Tile tile = new Tile(0L, "castle_wall_road", new int[]{3, 2, 1, 2}, new int[]{3, 3, 3, 2, 2, 2, 1, 1, 1}, new boolean[]{false, false, false, false, false, false, false, false, false}, false);
        int[] rotatedEdges = tile.rotatedEdges(2);
        assertArrayEquals(new int[]{1, 2, 3, 2}, rotatedEdges);
    }

    @Test
    void testRotateEdgesDegree270() {
        Tile tile = new Tile(0L, "castle_wall_road", new int[]{3, 2, 1, 2}, new int[]{3, 3, 3, 2, 2, 2, 1, 1, 1}, new boolean[]{false, false, false, false, false, false, false, false, false}, false);
        int[] rotatedEdges = tile.rotatedEdges(3);
        assertArrayEquals(new int[]{2, 1, 2, 3}, rotatedEdges);
    }

    @Test
    void testRotateEdgesInvalidRotation() {
        Tile tile = new Tile(0L, "castle_wall_road", new int[]{3, 2, 1, 2}, new int[]{3, 3, 3, 2, 2, 2, 1, 1, 1}, new boolean[]{false, false, false, false, false, false, false, false, false}, false);
        assertThrows(RuntimeException.class, () -> tile.rotatedEdges(4));
    }

    @Test
    void testRotateFeaturesDegree0() {
        Tile tile = new Tile(0L, "castle_wall_road", new int[]{3, 2, 1, 2}, new int[]{3, 3, 3, 2, 2, 2, 1, 1, 1}, new boolean[]{false, false, false, false, false, false, false, false, false}, false);
        int[] rotatedFeatures = tile.rotatedFeatures(0);
        assertArrayEquals(new int[]{3, 3, 3, 2, 2, 2, 1, 1, 1}, rotatedFeatures);
    }

    @Test
    void testRotateFeaturesDegree90() {
        Tile tile = new Tile(0L, "castle_wall_road", new int[]{3, 2, 1, 2}, new int[]{3, 3, 3, 2, 2, 2, 1, 1, 1}, new boolean[]{false, false, false, false, false, false, false, false, false}, false);
        int[] rotatedFeatures = tile.rotatedFeatures(1);
        assertArrayEquals(new int[]{1, 2, 3, 1, 2, 3, 1, 2, 3}, rotatedFeatures);
    }

    @Test
    void testRotateFeaturesDegree180() {
        Tile tile = new Tile(0L, "castle_wall_road", new int[]{3, 2, 1, 2}, new int[]{3, 3, 3, 2, 2, 2, 1, 1, 1}, new boolean[]{false, false, false, false, false, false, false, false, false}, false);
        int[] rotatedFeatures = tile.rotatedFeatures(2);
        assertArrayEquals(new int[]{1, 1, 1, 2, 2, 2, 3, 3, 3}, rotatedFeatures);
    }

    @Test
    void testRotateFeaturesDegree270() {
        Tile tile = new Tile(0L, "castle_wall_road", new int[]{3, 2, 1, 2}, new int[]{3, 3, 3, 2, 2, 2, 1, 1, 1}, new boolean[]{false, false, false, false, false, false, false, false, false}, false);
        int[] rotatedFeatures = tile.rotatedFeatures(3);
        assertArrayEquals(new int[]{3, 2, 1, 3, 2, 1, 3, 2, 1}, rotatedFeatures);
    }

    @Test
    void testRotateFeaturesInvalidRotation() {
        Tile tile = new Tile(0L, "castle_wall_road", new int[]{3, 2, 1, 2}, new int[]{3, 3, 3, 2, 2, 2, 1, 1, 1}, new boolean[]{false, false, false, false, false, false, false, false, false}, false);
        assertThrows(RuntimeException.class, () -> tile.rotatedFeatures(4));
    }

    @Test
    void testRotate90DegreesClockwise() {
        // Initial edges: {1, 2, 3, 4}
        Tile tile = new Tile(1L, "tile_image", new int[]{1, 2, 3, 4}, new int[]{1, 2, 3, 4, 5, 6, 7, 8, 9}, new boolean[]{false, false, false, false, false, false, false, false, false}, false);

        // Rotate 90 degrees clockwise
        tile.rotate(true);

        // After 90 degrees rotation
        assertEquals(1, tile.getRotation());
    }

    @Test
    void testRotate180DegreesClockwise() {
        // Initial edges: {1, 2, 3, 4}
        Tile tile = new Tile(1L, "tile_image", new int[]{1, 2, 3, 4}, new int[]{1, 2, 3, 4, 5, 6, 7, 8, 9}, new boolean[]{false, false, false, false, false, false, false, false, false}, false);

        // Rotate 180 degrees clockwise
        tile.rotate(true);
        tile.rotate(true);

        // After 180 degrees rotation
        assertEquals(2, tile.getRotation());
    }

    @Test
    void testRotate270DegreesClockwise() {
        // Initial edges: {1, 2, 3, 4}
        Tile tile = new Tile(1L, "tile_image", new int[]{1, 2, 3, 4}, new int[]{1, 2, 3, 4, 5, 6, 7, 8, 9}, new boolean[]{false, false, false, false, false, false, false, false, false}, false);

        // Rotate 270 degrees clockwise
        tile.rotate(true);
        tile.rotate(true);
        tile.rotate(true);

        // After 270 degrees rotation
        assertEquals(3, tile.getRotation());
    }

    @Test
    void testRotate360DegreesClockwise() {
        // Initial edges: {1, 2, 3, 4}
        Tile tile = new Tile(1L, "tile_image", new int[]{1, 2, 3, 4}, new int[]{1, 2, 3, 4, 5, 6, 7, 8, 9}, new boolean[]{false, false, false, false, false, false, false, false, false}, false);

        // Rotate 360 degrees clockwise
        tile.rotate(true);
        tile.rotate(true);
        tile.rotate(true);
        tile.rotate(true);

        // After 360 degrees rotation
        assertEquals(0, tile.getRotation());
    }

    @Test
    void testRotate90DegreesCounterClockwise() {
        // Initial edges: {1, 2, 3, 4}
        Tile tile = new Tile(1L, "tile_image", new int[]{1, 2, 3, 4}, new int[]{1, 2, 3, 4, 5, 6, 7, 8, 9}, new boolean[]{false, false, false, false, false, false, false, false, false}, false);

        // Rotate 90 degrees clockwise
        tile.rotate(false);

        // After 90 degrees rotation
        assertEquals(3, tile.getRotation());
    }

    @Test
    void testRotate180DegreesCounterClockwise() {
        // Initial edges: {1, 2, 3, 4}
        Tile tile = new Tile(1L, "tile_image", new int[]{1, 2, 3, 4}, new int[]{1, 2, 3, 4, 5, 6, 7, 8, 9}, new boolean[]{false, false, false, false, false, false, false, false, false}, false);

        // Rotate 180 degrees clockwise
        tile.rotate(false);
        tile.rotate(false);

        // After 180 degrees rotation
        assertEquals(2, tile.getRotation());
    }

    @Test
    void testRotate270DegreesCounterClockwise() {
        // Initial edges: {1, 2, 3, 4}
        Tile tile = new Tile(1L, "tile_image", new int[]{1, 2, 3, 4}, new int[]{1, 2, 3, 4, 5, 6, 7, 8, 9}, new boolean[]{false, false, false, false, false, false, false, false, false}, false);

        // Rotate 270 degrees clockwise
        tile.rotate(false);
        tile.rotate(false);
        tile.rotate(false);

        // After 270 degrees rotation
        assertEquals(1, tile.getRotation());
    }

    @Test
    void testRotate360DegreesCounterClockwise() {
        // Initial edges: {1, 2, 3, 4}
        Tile tile = new Tile(1L, "tile_image", new int[]{1, 2, 3, 4}, new int[]{1, 2, 3, 4, 5, 6, 7, 8, 9}, new boolean[]{false, false, false, false, false, false, false, false, false}, false);

        // Rotate 360 degrees clockwise
        tile.rotate(false);
        tile.rotate(false);
        tile.rotate(false);
        tile.rotate(false);

        // After 360 degrees rotation
        assertEquals(0, tile.getRotation());
    }

}
