package se2.carcassonne.model;

import java.util.ArrayList;

public class TileInitializer {
    public static ArrayList<Tile> initializeTiles() {

        ArrayList<Tile> tiles = new ArrayList<>(72);

        //1x castle_wall_road (4 in total but that's the start tile)
        tiles.add(new Tile(0L,"castle_wall_road", new int[]{3,2,1,2}, new int[]{3,3,3, 2,2,2, 1,1,1}, new boolean[]{false, false, false, false, false, false, false, false, false}));

        //1x road_junction_large
        tiles.add(new Tile(1L, "road_junction_large", new int[]{2,2,2,2}, new int[]{1,2,1, 2,4,2, 1,2,1}, new boolean[]{false, true, false, true, false, true, false, true, false}));

        //1x castle_center
        tiles.add(new Tile(2L, "castle_center", new int[]{3,3,3,3}, new int[]{3,3,3, 3,3,3, 3,3,3}, new boolean[]{false, false, false, false, true, false, false, false, false}));

        //3x castle_center_entry
        tiles.add(new Tile(3L, "castle_center_entry", new int[]{3,3,2,3}, new int[]{3,3,3, 3,3,3, 3,2,3}, new boolean[]{false, false, false, false, true, false, false, true, false}));
        tiles.add(new Tile(4L, "castle_center_entry", new int[]{3,3,2,3}, new int[]{3,3,3, 3,3,3, 3,2,3}, new boolean[]{false, false, false, false, true, false, false, true, false}));
        tiles.add(new Tile(5L, "castle_center_entry", new int[]{3,3,2,3}, new int[]{3,3,3, 3,3,3, 3,2,3}, new boolean[]{false, false, false, false, true, false, false, true, false}));

        //4x castle_center_side
        tiles.add(new Tile(6L, "castle_center_side", new int[]{3,3,1,3}, new int[]{3,3,3, 3,3,3, 3,1,3}, new boolean[]{false, false, false, false, true, false, false, true, false}));
        tiles.add(new Tile(7L, "castle_center_side", new int[]{3,3,1,3}, new int[]{3,3,3, 3,3,3, 3,1,3}, new boolean[]{false, false, false, false, true, false, false, true, false}));
        tiles.add(new Tile(8L, "castle_center_side", new int[]{3,3,1,3}, new int[]{3,3,3, 3,3,3, 3,1,3}, new boolean[]{false, false, false, false, true, false, false, true, false}));
        tiles.add(new Tile(9L, "castle_center_side", new int[]{3,3,1,3}, new int[]{3,3,3, 3,3,3, 3,1,3}, new boolean[]{false, false, false, false, true, false, false, true, false}));

        //5x castle_edge TODO: Check features!
        tiles.add(new Tile(10L,"castle_edge", new int[]{3,3,1,1}, new int[]{3,3,3, 1,1,3, 1,1,3}, new boolean[]{false, false, true, false, false, false, true, false, false}));
        tiles.add(new Tile(11L,"castle_edge", new int[]{3,3,1,1}, new int[]{3,3,3, 1,1,3, 1,1,3}, new boolean[]{false, false, true, false, false, false, true, false, false}));
        tiles.add(new Tile(12L,"castle_edge", new int[]{3,3,1,1}, new int[]{3,3,3, 1,1,3, 1,1,3}, new boolean[]{false, false, true, false, false, false, true, false, false}));
        tiles.add(new Tile(13L,"castle_edge", new int[]{3,3,1,1}, new int[]{3,3,3, 1,1,3, 1,1,3}, new boolean[]{false, false, true, false, false, false, true, false, false}));
        tiles.add(new Tile(14L,"castle_edge", new int[]{3,3,1,1}, new int[]{3,3,3, 1,1,3, 1,1,3}, new boolean[]{false, false, true, false, false, false, true, false, false}));

        //5x castle_edge_road
        tiles.add(new Tile(15L,"castle_edge_road", new int[]{3,3,2,2}, new int[]{3,3,3, 2,2,3, 1,2,3}, new boolean[]{false, false, true, false, true, false, true, false, false}));
        tiles.add(new Tile(16L,"castle_edge_road", new int[]{3,3,2,2}, new int[]{3,3,3, 2,2,3, 1,2,3}, new boolean[]{false, false, true, false, true, false, true, false, false}));
        tiles.add(new Tile(17L,"castle_edge_road", new int[]{3,3,2,2}, new int[]{3,3,3, 2,2,3, 1,2,3}, new boolean[]{false, false, true, false, true, false, true, false, false}));
        tiles.add(new Tile(18L,"castle_edge_road", new int[]{3,3,2,2}, new int[]{3,3,3, 2,2,3, 1,2,3}, new boolean[]{false, false, true, false, true, false, true, false, false}));
        tiles.add(new Tile(19L,"castle_edge_road", new int[]{3,3,2,2}, new int[]{3,3,3, 2,2,3, 1,2,3}, new boolean[]{false, false, true, false, true, false, true, false, false}));

        //3x castle_sides //TODO: Check features!
        tiles.add(new Tile(20L,"castle_sides", new int[]{3,1,3,1}, new int[]{3,3,3, 1,1,1, 3,3,3}, new boolean[]{false, true, false, false, true, false, false, true, false}));
        tiles.add(new Tile(21L,"castle_sides", new int[]{3,1,3,1}, new int[]{3,3,3, 1,1,1, 3,3,3}, new boolean[]{false, true, false, false, true, false, false, true, false}));
        tiles.add(new Tile(22L,"castle_sides", new int[]{3,1,3,1}, new int[]{3,3,3, 1,1,1, 3,3,3}, new boolean[]{false, true, false, false, true, false, false, true, false}));

        //2x castle_sides_edge //TODO: Check features!
        tiles.add(new Tile(23L,"castle_sides_edge", new int[]{3,1,1,3}, new int[]{3,3,3, 3,1,1, 3,1,1}, new boolean[]{false, true, false, true, true, false, false, false, false}));
        tiles.add(new Tile(24L,"castle_sides_edge", new int[]{3,1,1,3}, new int[]{3,3,3, 3,1,1, 3,1,1}, new boolean[]{false, true, false, true, true, false, false, false, false}));

        //3x castle_tube
        tiles.add(new Tile(25L,"castle_tube", new int[]{1,3,1,3}, new int[]{3,1,3, 3,3,3, 3,1,3}, new boolean[]{false, true, false, false, true, false, false, true, false}));
        tiles.add(new Tile(26L,"castle_tube", new int[]{1,3,1,3}, new int[]{3,1,3, 3,3,3, 3,1,3}, new boolean[]{false, true, false, true, true, false, false, false, false}));
        tiles.add(new Tile(27L,"castle_tube", new int[]{1,3,1,3}, new int[]{3,1,3, 3,3,3, 3,1,3}, new boolean[]{false, true, false, true, true, false, false, false, false}));

        //5x castle_wall
        tiles.add(new Tile(28L,"castle_wall", new int[]{3,1,1,1}, new int[]{3,3,3, 1,1,1, 1,1,1}, new boolean[]{false, true, false, true, true, true, true, true, true}));
        tiles.add(new Tile(29L,"castle_wall", new int[]{3,1,1,1}, new int[]{3,3,3, 1,1,1, 1,1,1}, new boolean[]{false, true, false, true, true, true, true, true, true}));
        tiles.add(new Tile(30L,"castle_wall", new int[]{3,1,1,1}, new int[]{3,3,3, 1,1,1, 1,1,1}, new boolean[]{false, true, false, true, true, true, true, true, true}));
        tiles.add(new Tile(31L,"castle_wall", new int[]{3,1,1,1}, new int[]{3,3,3, 1,1,1, 1,1,1}, new boolean[]{false, true, false, true, true, true, true, true, true}));
        tiles.add(new Tile(32L,"castle_wall", new int[]{3,1,1,1}, new int[]{3,3,3, 1,1,1, 1,1,1}, new boolean[]{false, true, false, true, true, true, true, true, true}));

        //3x castle_wall_curve_left
        tiles.add(new Tile(33L,"castle_wall_curve_left", new int[]{3,1,2,2}, new int[]{3,3,3, 2,2,1, 1,2,1}, new boolean[]{false, true, false, true, true, true, true, true, true}));
        tiles.add(new Tile(34L,"castle_wall_curve_left", new int[]{3,1,2,2}, new int[]{3,3,3, 2,2,1, 1,2,1}, new boolean[]{false, true, false, true, true, true, true, true, true}));
        tiles.add(new Tile(35L,"castle_wall_curve_left", new int[]{3,1,2,2}, new int[]{3,3,3, 2,2,1, 1,2,1}, new boolean[]{false, true, false, true, true, true, true, true, true}));

        //3x castle_wall_curve_right
        tiles.add(new Tile(36L,"castle_wall_curve_right", new int[]{3,2,2,1}, new int[]{3,3,3, 1,2,2, 1,2,1}, new boolean[]{false, true, false, true, true, true, true, true, true}));
        tiles.add(new Tile(37L,"castle_wall_curve_right", new int[]{3,2,2,1}, new int[]{3,3,3, 1,2,2, 1,2,1}, new boolean[]{false, true, false, true, true, true, true, true, true}));
        tiles.add(new Tile(38L,"castle_wall_curve_right", new int[]{3,2,2,1}, new int[]{3,3,3, 1,2,2, 1,2,1}, new boolean[]{false, true, false, true, true, true, true, true, true}));

        //3x castle_wall_junction
        tiles.add(new Tile(39L,"castle_wall_junction", new int[]{3,2,2,2}, new int[]{3,3,3, 2,4,2, 1,2,1}, new boolean[]{false, true, false, true, false, true, true, true, true}));
        tiles.add(new Tile(40L,"castle_wall_junction", new int[]{3,2,2,2}, new int[]{3,3,3, 2,4,2, 1,2,1}, new boolean[]{false, true, false, true, false, true, true, true, true}));
        tiles.add(new Tile(41L,"castle_wall_junction", new int[]{3,2,2,2}, new int[]{3,3,3, 2,4,2, 1,2,1}, new boolean[]{false, true, false, true, false, true, true, true, true}));

        //3x castle_wall_road (1 more but that's the start tile)
        tiles.add(new Tile(42L,"castle_wall_road", new int[]{3,2,1,2}, new int[]{3,3,3, 2,2,2, 1,1,1}, new boolean[]{false, true, false, true, true, true, true, true, true}));
        tiles.add(new Tile(43L,"castle_wall_road", new int[]{3,2,1,2}, new int[]{3,3,3, 2,2,2, 1,1,1}, new boolean[]{false, true, false, true, true, true, true, true, true}));
        tiles.add(new Tile(44L,"castle_wall_road", new int[]{3,2,1,2}, new int[]{3,3,3, 2,2,2, 1,1,1}, new boolean[]{false, true, false, true, true, true, true, true, true}));

        //4x monastery
        tiles.add(new Tile(45L,"monastery", new int[]{1,1,1,1}, new int[]{1,1,1, 1,5,1, 1,1,1}, new boolean[]{true, true, true, true, true, true, true, true, true}));
        tiles.add(new Tile(46L,"monastery", new int[]{1,1,1,1}, new int[]{1,1,1, 1,5,1, 1,1,1}, new boolean[]{true, true, true, true, true, true, true, true, true}));
        tiles.add(new Tile(47L,"monastery", new int[]{1,1,1,1}, new int[]{1,1,1, 1,5,1, 1,1,1}, new boolean[]{true, true, true, true, true, true, true, true, true}));
        tiles.add(new Tile(48L,"monastery", new int[]{1,1,1,1}, new int[]{1,1,1, 1,5,1, 1,1,1}, new boolean[]{true, true, true, true, true, true, true, true, true}));

        //2x monastery_road
        tiles.add(new Tile(49L,"monastery_road", new int[]{1,1,2,1}, new int[]{1,1,1, 1,5,1, 1,2,1}, new boolean[]{true, true, true, true, true, true, true, true, true}));
        tiles.add(new Tile(50L,"monastery_road", new int[]{1,1,2,1}, new int[]{1,1,1, 1,5,1, 1,2,1}, new boolean[]{true, true, true, true, true, true, true, true, true}));

        //8x road
        tiles.add(new Tile(51L,"road", new int[]{2,1,2,1}, new int[]{1,2,1, 1,2,1, 1,2,1}, new boolean[]{true, true, true, true, true, true, true, true, true}));
        tiles.add(new Tile(52L,"road", new int[]{2,1,2,1}, new int[]{1,2,1, 1,2,1, 1,2,1}, new boolean[]{true, true, true, true, true, true, true, true, true}));
        tiles.add(new Tile(53L,"road", new int[]{2,1,2,1}, new int[]{1,2,1, 1,2,1, 1,2,1}, new boolean[]{true, true, true, true, true, true, true, true, true}));
        tiles.add(new Tile(54L,"road", new int[]{2,1,2,1}, new int[]{1,2,1, 1,2,1, 1,2,1}, new boolean[]{true, true, true, true, true, true, true, true, true}));
        tiles.add(new Tile(55L,"road", new int[]{2,1,2,1}, new int[]{1,2,1, 1,2,1, 1,2,1}, new boolean[]{true, true, true, true, true, true, true, true, true}));
        tiles.add(new Tile(56L,"road", new int[]{2,1,2,1}, new int[]{1,2,1, 1,2,1, 1,2,1}, new boolean[]{true, true, true, true, true, true, true, true, true}));
        tiles.add(new Tile(57L,"road", new int[]{2,1,2,1}, new int[]{1,2,1, 1,2,1, 1,2,1}, new boolean[]{true, true, true, true, true, true, true, true, true}));
        tiles.add(new Tile(58L,"road", new int[]{2,1,2,1}, new int[]{1,2,1, 1,2,1, 1,2,1}, new boolean[]{true, true, true, true, true, true, true, true, true}));

        //9x road_curve
        tiles.add(new Tile(59L,"road_curve", new int[]{1,1,2,2}, new int[]{1,1,1, 2,2,1, 1,2,1}, new boolean[]{true, true, true, true, true, true, true, true, false}));
        tiles.add(new Tile(60L,"road_curve", new int[]{1,1,2,2}, new int[]{1,1,1, 2,2,1, 1,2,1}, new boolean[]{true, true, true, true, true, true, true, true, false}));
        tiles.add(new Tile(61L,"road_curve", new int[]{1,1,2,2}, new int[]{1,1,1, 2,2,1, 1,2,1}, new boolean[]{true, true, true, true, true, true, true, true, false}));
        tiles.add(new Tile(62L,"road_curve", new int[]{1,1,2,2}, new int[]{1,1,1, 2,2,1, 1,2,1}, new boolean[]{true, true, true, true, true, true, true, true, false}));
        tiles.add(new Tile(63L,"road_curve", new int[]{1,1,2,2}, new int[]{1,1,1, 2,2,1, 1,2,1}, new boolean[]{true, true, true, true, true, true, true, true, false}));
        tiles.add(new Tile(64L,"road_curve", new int[]{1,1,2,2}, new int[]{1,1,1, 2,2,1, 1,2,1}, new boolean[]{true, true, true, true, true, true, true, true, false}));
        tiles.add(new Tile(65L,"road_curve", new int[]{1,1,2,2}, new int[]{1,1,1, 2,2,1, 1,2,1}, new boolean[]{true, true, true, true, true, true, true, true, false}));
        tiles.add(new Tile(66L,"road_curve", new int[]{1,1,2,2}, new int[]{1,1,1, 2,2,1, 1,2,1}, new boolean[]{true, true, true, true, true, true, true, true, false}));
        tiles.add(new Tile(67L,"road_curve", new int[]{1,1,2,2}, new int[]{1,1,1, 2,2,1, 1,2,1}, new boolean[]{true, true, true, true, true, true, true, true, false}));

        //4x road_junction_small (cross)
        tiles.add(new Tile(68L,"road_junction_small", new int[]{1,2,2,2}, new int[]{1,1,1, 2,4,2, 1,2,1}, new boolean[]{true, true, true, true, false, true, true, true, true}));
        tiles.add(new Tile(69L,"road_junction_small", new int[]{1,2,2,2}, new int[]{1,1,1, 2,4,2, 1,2,1}, new boolean[]{true, true, true, true, false, true, true, true, true}));
        tiles.add(new Tile(70L,"road_junction_small", new int[]{1,2,2,2}, new int[]{1,1,1, 2,4,2, 1,2,1}, new boolean[]{true, true, true, true, false, true, true, true, true}));
        tiles.add(new Tile(71L,"road_junction_small", new int[]{1,2,2,2}, new int[]{1,1,1, 2,4,2, 1,2,1}, new boolean[]{true, true, true, true, false, true, true, true, true}));

        return tiles;
    }
}

