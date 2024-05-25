package se2.carcassonne.model;

import java.util.ArrayList;

public class TileInitializer {

    private TileInitializer() {
        // private constructor to hide the implicit public one
        throw new IllegalStateException("Utility class");
    }

    // constants for tile imageNames that occur multiple times
    private static final String CASTLE_WALL_ROAD = "castle_wall_road";
    private static final String CASTLE_CENTER_ENTRY = "castle_center_entry";
    private static final String CASTLE_CENTER_SIDE = "castle_center_side";
    private static final String CASTLE_EDGE = "castle_edge";
    private static final String CASTLE_EDGE_ROAD = "castle_edge_road";
    private static final String CASTLE_WALL = "castle_wall";
    private static final String ROAD = "road";
    private static final String ROAD_CURVE = "road_curve";
    private static final String ROAD_JUNCTION_SMALL = "road_junction_small";

    private static final String ROAD_JUNCTION_LARGE = "road_junction_large";
    private static final String CASTLE_CENTER = "castle_center";
    private static final String MONASTERY = "monastery";
    private static final String MONASTERY_ROAD = "monastery_road";
    private static final String CASTLE_SIDES = "castle_sides";
    private static final String CASTLE_SIDES_EDGE = "castle_sides_edge";
    private static final String CASTLE_TUBE = "castle_tube";
    private static final String CASTLE_WALL_CURVE_LEFT = "castle_wall_curve_left";
    private static final String CASTLE_WALL_CURVE_RIGHT = "castle_wall_curve_right";
    private static final String CASTLE_WALL_JUNCTION = "castle_wall_junction";

    public static ArrayList<Tile> initializeTiles() {

        ArrayList<Tile> tiles = new ArrayList<>(72);

        //1x castle_wall_road (4 in total but that's the start tile)
        tiles.add(new Tile(0L,CASTLE_WALL_ROAD, new int[]{3,2,1,2}, new int[]{3,3,3, 2,2,2, 1,1,1}, new boolean[]{false, false, false, false, false, false, false, false, false}, false));

        //1x road_junction_large
        tiles.add(new Tile(1L, ROAD_JUNCTION_LARGE, new int[]{2,2,2,2}, new int[]{1,2,1, 2,4,2, 1,2,1}, new boolean[]{false, true, false, true, false, true, false, true, false}, true));

        //1x castle_center
        tiles.add(new Tile(2L, CASTLE_CENTER, new int[]{3,3,3,3}, new int[]{3,3,3, 3,3,3, 3,3,3}, new boolean[]{false, false, false, false, true, false, false, false, false}, false));

        //3x castle_center_entry
        tiles.add(new Tile(3L, CASTLE_CENTER_ENTRY, new int[]{3,3,2,3}, new int[]{3,3,3, 3,3,3, 3,2,3}, new boolean[]{false, false, false, false, true, false, false, true, false}, true));
        tiles.add(new Tile(4L, CASTLE_CENTER_ENTRY, new int[]{3,3,2,3}, new int[]{3,3,3, 3,3,3, 3,2,3}, new boolean[]{false, false, false, false, true, false, false, true, false}, true));
        tiles.add(new Tile(5L, CASTLE_CENTER_ENTRY, new int[]{3,3,2,3}, new int[]{3,3,3, 3,3,3, 3,2,3}, new boolean[]{false, false, false, false, true, false, false, true, false}, true));

        //4x castle_center_side
        tiles.add(new Tile(6L, CASTLE_CENTER_SIDE, new int[]{3,3,1,3}, new int[]{3,3,3, 3,3,3, 3,1,3}, new boolean[]{false, false, false, false, true, false, false, true, false}, false));
        tiles.add(new Tile(7L, CASTLE_CENTER_SIDE, new int[]{3,3,1,3}, new int[]{3,3,3, 3,3,3, 3,1,3}, new boolean[]{false, false, false, false, true, false, false, true, false}, false));
        tiles.add(new Tile(8L, CASTLE_CENTER_SIDE, new int[]{3,3,1,3}, new int[]{3,3,3, 3,3,3, 3,1,3}, new boolean[]{false, false, false, false, true, false, false, true, false}, false));
        tiles.add(new Tile(9L, CASTLE_CENTER_SIDE, new int[]{3,3,1,3}, new int[]{3,3,3, 3,3,3, 3,1,3}, new boolean[]{false, false, false, false, true, false, false, true, false}, false));

        //5x castle_edge
        tiles.add(new Tile(10L,CASTLE_EDGE, new int[]{3,3,1,1}, new int[]{3,3,3, 1,1,3, 1,1,3}, new boolean[]{false, false, true, false, false, false, true, false, false}, false));
        tiles.add(new Tile(11L,CASTLE_EDGE, new int[]{3,3,1,1}, new int[]{3,3,3, 1,1,3, 1,1,3}, new boolean[]{false, false, true, false, false, false, true, false, false}, false));
        tiles.add(new Tile(12L,CASTLE_EDGE, new int[]{3,3,1,1}, new int[]{3,3,3, 1,1,3, 1,1,3}, new boolean[]{false, false, true, false, false, false, true, false, false}, false));
        tiles.add(new Tile(13L,CASTLE_EDGE, new int[]{3,3,1,1}, new int[]{3,3,3, 1,1,3, 1,1,3}, new boolean[]{false, false, true, false, false, false, true, false, false}, false));
        tiles.add(new Tile(14L,CASTLE_EDGE, new int[]{3,3,1,1}, new int[]{3,3,3, 1,1,3, 1,1,3}, new boolean[]{false, false, true, false, false, false, true, false, false}, false));

        //5x castle_edge_road
        tiles.add(new Tile(15L, CASTLE_EDGE_ROAD, new int[]{3,3,2,2}, new int[]{3,3,3, 2,2,3, 1,2,3}, new boolean[]{false, false, true, false, true, false, true, false, false}, false));
        tiles.add(new Tile(16L, CASTLE_EDGE_ROAD, new int[]{3,3,2,2}, new int[]{3,3,3, 2,2,3, 1,2,3}, new boolean[]{false, false, true, false, true, false, true, false, false}, false));
        tiles.add(new Tile(17L, CASTLE_EDGE_ROAD, new int[]{3,3,2,2}, new int[]{3,3,3, 2,2,3, 1,2,3}, new boolean[]{false, false, true, false, true, false, true, false, false}, false));
        tiles.add(new Tile(18L, CASTLE_EDGE_ROAD, new int[]{3,3,2,2}, new int[]{3,3,3, 2,2,3, 1,2,3}, new boolean[]{false, false, true, false, true, false, true, false, false}, false));
        tiles.add(new Tile(19L, CASTLE_EDGE_ROAD, new int[]{3,3,2,2}, new int[]{3,3,3, 2,2,3, 1,2,3}, new boolean[]{false, false, true, false, true, false, true, false, false}, false));

        //3x castle_sides
        tiles.add(new Tile(20L,CASTLE_SIDES, new int[]{3,1,3,1}, new int[]{3,3,3, 1,1,1, 3,3,3}, new boolean[]{false, true, false, false, true, false, false, true, false}, false));
        tiles.add(new Tile(21L,CASTLE_SIDES, new int[]{3,1,3,1}, new int[]{3,3,3, 1,1,1, 3,3,3}, new boolean[]{false, true, false, false, true, false, false, true, false}, false));
        tiles.add(new Tile(22L,CASTLE_SIDES, new int[]{3,1,3,1}, new int[]{3,3,3, 1,1,1, 3,3,3}, new boolean[]{false, true, false, false, true, false, false, true, false}, false));

        //2x castle_sides_edge
        tiles.add(new Tile(23L,CASTLE_SIDES_EDGE, new int[]{3,1,1,3}, new int[]{3,3,3, 3,1,1, 3,1,1}, new boolean[]{false, true, false, true, true, false, false, false, false}, false));
        tiles.add(new Tile(24L,CASTLE_SIDES_EDGE, new int[]{3,1,1,3}, new int[]{3,3,3, 3,1,1, 3,1,1}, new boolean[]{false, true, false, true, true, false, false, false, false}, false));

        //3x castle_tube
        tiles.add(new Tile(25L,CASTLE_TUBE, new int[]{1,3,1,3}, new int[]{3,1,3, 3,3,3, 3,1,3}, new boolean[]{false, true, false, false, true, false, false, true, false}, false));
        tiles.add(new Tile(26L,CASTLE_TUBE, new int[]{1,3,1,3}, new int[]{3,1,3, 3,3,3, 3,1,3}, new boolean[]{false, true, false, true, true, false, false, false, false}, false));
        tiles.add(new Tile(27L,CASTLE_TUBE, new int[]{1,3,1,3}, new int[]{3,1,3, 3,3,3, 3,1,3}, new boolean[]{false, true, false, true, true, false, false, false, false}, false));

        //5x castle_wall
        tiles.add(new Tile(28L,CASTLE_WALL, new int[]{3,1,1,1}, new int[]{3,3,3, 1,1,1, 1,1,1}, new boolean[]{false, true, false, false, false, false, false, true, false}, false));
        tiles.add(new Tile(29L,CASTLE_WALL, new int[]{3,1,1,1}, new int[]{3,3,3, 1,1,1, 1,1,1}, new boolean[]{false, true, false, false, false, false, false, true, false}, false));
        tiles.add(new Tile(30L,CASTLE_WALL, new int[]{3,1,1,1}, new int[]{3,3,3, 1,1,1, 1,1,1}, new boolean[]{false, true, false, false, false, false, false, true, false}, false));
        tiles.add(new Tile(31L,CASTLE_WALL, new int[]{3,1,1,1}, new int[]{3,3,3, 1,1,1, 1,1,1}, new boolean[]{false, true, false, false, false, false, false, true, false}, false));
        tiles.add(new Tile(32L,CASTLE_WALL, new int[]{3,1,1,1}, new int[]{3,3,3, 1,1,1, 1,1,1}, new boolean[]{false, true, false, false, false, false, false, true, false}, false));

        //3x castle_wall_curve_left
        tiles.add(new Tile(33L,CASTLE_WALL_CURVE_LEFT, new int[]{3,1,2,2}, new int[]{3,3,3, 2,2,1, 1,2,1}, new boolean[]{false, true, false, false, false, true, false, true, false}, false));
        tiles.add(new Tile(34L,CASTLE_WALL_CURVE_LEFT, new int[]{3,1,2,2}, new int[]{3,3,3, 2,2,1, 1,2,1}, new boolean[]{false, true, false, false, false, true, false, true, false}, false));
        tiles.add(new Tile(35L,CASTLE_WALL_CURVE_LEFT, new int[]{3,1,2,2}, new int[]{3,3,3, 2,2,1, 1,2,1}, new boolean[]{false, true, false, false, false, true, false, true, false}, false));

        //3x castle_wall_curve_right
        tiles.add(new Tile(36L,CASTLE_WALL_CURVE_RIGHT, new int[]{3,2,2,1}, new int[]{3,3,3, 1,2,2, 1,2,1}, new boolean[]{false, true, false, true, false, false, false, true, false}, false));
        tiles.add(new Tile(37L,CASTLE_WALL_CURVE_RIGHT, new int[]{3,2,2,1}, new int[]{3,3,3, 1,2,2, 1,2,1}, new boolean[]{false, true, false, true, false, false, false, true, false}, false));
        tiles.add(new Tile(38L,CASTLE_WALL_CURVE_RIGHT, new int[]{3,2,2,1}, new int[]{3,3,3, 1,2,2, 1,2,1}, new boolean[]{false, true, false, true, false, false, false, true, false}, false));

        //3x castle_wall_junction
        tiles.add(new Tile(39L,CASTLE_WALL_JUNCTION, new int[]{3,2,2,2}, new int[]{3,3,3, 2,4,2, 1,2,1}, new boolean[]{false, true, false, true, false, true, true, true, true}, true));
        tiles.add(new Tile(40L,CASTLE_WALL_JUNCTION, new int[]{3,2,2,2}, new int[]{3,3,3, 2,4,2, 1,2,1}, new boolean[]{false, true, false, true, false, true, true, true, true}, true));
        tiles.add(new Tile(41L,CASTLE_WALL_JUNCTION, new int[]{3,2,2,2}, new int[]{3,3,3, 2,4,2, 1,2,1}, new boolean[]{false, true, false, true, false, true, true, true, true}, true));

        //3x castle_wall_road (1 more but that's the start tile)
        tiles.add(new Tile(42L,CASTLE_WALL_ROAD, new int[]{3,2,1,2}, new int[]{3,3,3, 2,2,2, 1,1,1}, new boolean[]{false, true, false, false, true, false, false, true, false}, false));
        tiles.add(new Tile(43L,CASTLE_WALL_ROAD, new int[]{3,2,1,2}, new int[]{3,3,3, 2,2,2, 1,1,1}, new boolean[]{false, true, false, false, true, false, false, true, false}, false));
        tiles.add(new Tile(44L,CASTLE_WALL_ROAD, new int[]{3,2,1,2}, new int[]{3,3,3, 2,2,2, 1,1,1}, new boolean[]{false, true, false, false, true, false, false, true, false}, false));

        //4x monastery
        tiles.add(new Tile(45L,MONASTERY, new int[]{1,1,1,1}, new int[]{1,1,1, 1,5,1, 1,1,1}, new boolean[]{false, true, false, true, true, true, false, true, false}, false));
        tiles.add(new Tile(46L,MONASTERY, new int[]{1,1,1,1}, new int[]{1,1,1, 1,5,1, 1,1,1}, new boolean[]{false, true, false, true, true, true, false, true, false}, false));
        tiles.add(new Tile(47L,MONASTERY, new int[]{1,1,1,1}, new int[]{1,1,1, 1,5,1, 1,1,1}, new boolean[]{false, true, false, true, true, true, false, true, false}, false));
        tiles.add(new Tile(48L,MONASTERY, new int[]{1,1,1,1}, new int[]{1,1,1, 1,5,1, 1,1,1}, new boolean[]{false, true, false, true, true, true, false, true, false}, false));

        //2x monastery_road
        tiles.add(new Tile(49L,MONASTERY_ROAD, new int[]{1,1,2,1}, new int[]{1,1,1, 1,5,1, 1,2,1}, new boolean[]{false, true, false, true, true, true, false, true, false}, true));
        tiles.add(new Tile(50L,MONASTERY_ROAD, new int[]{1,1,2,1}, new int[]{1,1,1, 1,5,1, 1,2,1}, new boolean[]{false, true, false, true, true, true, false, true, false}, true));

        //8x road
        tiles.add(new Tile(51L,ROAD, new int[]{2,1,2,1}, new int[]{1,2,1, 1,2,1, 1,2,1}, new boolean[]{true, false, false, false, true, false, false, false, true}, false));
        tiles.add(new Tile(52L,ROAD, new int[]{2,1,2,1}, new int[]{1,2,1, 1,2,1, 1,2,1}, new boolean[]{true, false, false, false, true, false, false, false, true}, false));
        tiles.add(new Tile(53L,ROAD, new int[]{2,1,2,1}, new int[]{1,2,1, 1,2,1, 1,2,1}, new boolean[]{true, false, false, false, true, false, false, false, true}, false));
        tiles.add(new Tile(54L,ROAD, new int[]{2,1,2,1}, new int[]{1,2,1, 1,2,1, 1,2,1}, new boolean[]{true, false, false, false, true, false, false, false, true}, false));
        tiles.add(new Tile(55L,ROAD, new int[]{2,1,2,1}, new int[]{1,2,1, 1,2,1, 1,2,1}, new boolean[]{true, false, false, false, true, false, false, false, true}, false));
        tiles.add(new Tile(56L,ROAD, new int[]{2,1,2,1}, new int[]{1,2,1, 1,2,1, 1,2,1}, new boolean[]{true, false, false, false, true, false, false, false, true}, false));
        tiles.add(new Tile(57L,ROAD, new int[]{2,1,2,1}, new int[]{1,2,1, 1,2,1, 1,2,1}, new boolean[]{true, false, false, false, true, false, false, false, true}, false));
        tiles.add(new Tile(58L,ROAD, new int[]{2,1,2,1}, new int[]{1,2,1, 1,2,1, 1,2,1}, new boolean[]{true, false, false, false, true, false, false, false, true}, false));

        //9x road_curve
        tiles.add(new Tile(59L,ROAD_CURVE, new int[]{1,1,2,2}, new int[]{1,1,1, 2,2,1, 1,2,1}, new boolean[]{false, true, false, false, true, false, true, false, false}, false));
        tiles.add(new Tile(60L,ROAD_CURVE, new int[]{1,1,2,2}, new int[]{1,1,1, 2,2,1, 1,2,1}, new boolean[]{false, true, false, false, true, false, true, false, false}, false));
        tiles.add(new Tile(61L,ROAD_CURVE, new int[]{1,1,2,2}, new int[]{1,1,1, 2,2,1, 1,2,1}, new boolean[]{false, true, false, false, true, false, true, false, false}, false));
        tiles.add(new Tile(62L,ROAD_CURVE, new int[]{1,1,2,2}, new int[]{1,1,1, 2,2,1, 1,2,1}, new boolean[]{false, true, false, false, true, false, true, false, false}, false));
        tiles.add(new Tile(63L,ROAD_CURVE, new int[]{1,1,2,2}, new int[]{1,1,1, 2,2,1, 1,2,1}, new boolean[]{false, true, false, false, true, false, true, false, false}, false));
        tiles.add(new Tile(64L,ROAD_CURVE, new int[]{1,1,2,2}, new int[]{1,1,1, 2,2,1, 1,2,1}, new boolean[]{false, true, false, false, true, false, true, false, false}, false));
        tiles.add(new Tile(65L,ROAD_CURVE, new int[]{1,1,2,2}, new int[]{1,1,1, 2,2,1, 1,2,1}, new boolean[]{false, true, false, false, true, false, true, false, false}, false));
        tiles.add(new Tile(66L,ROAD_CURVE, new int[]{1,1,2,2}, new int[]{1,1,1, 2,2,1, 1,2,1}, new boolean[]{false, true, false, false, true, false, true, false, false}, false));
        tiles.add(new Tile(67L,ROAD_CURVE, new int[]{1,1,2,2}, new int[]{1,1,1, 2,2,1, 1,2,1}, new boolean[]{false, true, false, false, true, false, true, false, false}, false));

        //4x road_junction_small (cross)
        tiles.add(new Tile(68L,ROAD_JUNCTION_SMALL, new int[]{1,2,2,2}, new int[]{1,1,1, 2,4,2, 1,2,1}, new boolean[]{false, true, false, true, false, true, true, true, true}, true));
        tiles.add(new Tile(69L,ROAD_JUNCTION_SMALL, new int[]{1,2,2,2}, new int[]{1,1,1, 2,4,2, 1,2,1}, new boolean[]{false, true, false, true, false, true, true, true, true}, true));
        tiles.add(new Tile(70L,ROAD_JUNCTION_SMALL, new int[]{1,2,2,2}, new int[]{1,1,1, 2,4,2, 1,2,1}, new boolean[]{false, true, false, true, false, true, true, true, true}, true));
        tiles.add(new Tile(71L,ROAD_JUNCTION_SMALL, new int[]{1,2,2,2}, new int[]{1,1,1, 2,4,2, 1,2,1}, new boolean[]{false, true, false, true, false, true, true, true, true}, true));

        return tiles;
    }
}

