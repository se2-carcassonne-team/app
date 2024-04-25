package se2.carcassonne.model;

import lombok.Data;

@Data
public class Tile {
    /**
     * use same ids as tiles have in backend!
     */
    private Long id;

    private String imageName;

    /**
     * <p>0 = 0 degrees</p>
     * <p>1 = 90 degrees</p>
     * <p>2 = 180 degrees</p>
     * <p>3 = 270 degrees</p>
     */
    private int rotation;

    private Coordinates coordinates;

    /**
     * <p>indices: 0 = North, 1 = East, 2 = South, 3 = West</p>
     * <p>possible values</p>
     * <p>- 1 = field</p>
     * <p>- 2 = road</p>
     * <p>- 3 = castle</p>
     */
    private final int[] edges = new int[4];

    /**
     * for meeple placements, we must divide every tile into a 3x3 grid --> 9 values in a single array for better runtime
     * <p>possible values:</p>
     * <p>1 = field</p>
     * <p>2 = road</p>
     * <p>3 = castle</p>
     * <p>4 = village</p>
     * <p>5 = monastery</p>
     * TODO: more detailed?
     */
    private final int[] features = new int[9];

    /**
     * Edge Types:
     * <p>1 = field</p>
     * <p>2 = road</p>
     * <p>3 = castle</p>
     * @param id the same id that the tile has on the server
     * @param imageName name of image in app
     * @param
     */
    public Tile(Long id, String imageName, int[] edges, int[] features) {
        this.id = id;
        this.imageName = imageName;
        this.rotation = 0;
        this.edges[0] = edges[0];
        this.edges[1] = edges[1];
        this.edges[2] = edges[2];
        this.edges[3] = edges[3];
        this.features[0] = features[0];
        this.features[1] = features[1];
        this.features[2] = features[2];
        this.features[3] = features[3];
        this.features[4] = features[4];
        this.features[5] = features[5];
        this.features[6] = features[6];
        this.features[7] = features[7];
        this.features[8] = features[8];
        this.coordinates = null;
    }

    /**
     *
     * @param rotation clockwise rotation in 90 degree steps: <p>0 = no rotation (0 deg)</p><p>1 = 90 deg</p><p>2 = 180 deg</p><p>3 = 270 deg</p>
     * @return new array of the tile edges rotated by the given rotation.
     */
    private int[] rotatedEdges(int rotation){
        switch (rotation) {
            case 0:
                return getEdges();
            case 1: {
                int[] rotatedEdgeTypes = new int[4];
                int[] defaultEdgeTypes = getEdges();
                // hard-coded 90-degree edge rotation of default edge types:
                // e.g. 1 2 3 4 --> 4 1 2 3
                rotatedEdgeTypes[0] = defaultEdgeTypes[3];
                rotatedEdgeTypes[1] = defaultEdgeTypes[0];
                rotatedEdgeTypes[2] = defaultEdgeTypes[1];
                rotatedEdgeTypes[3] = defaultEdgeTypes[2];
                return rotatedEdgeTypes;
            }
            case 2: {
                int[] rotatedEdgeTypes = new int[4];
                int[] defaultEdgeTypes = getEdges();
                // hard-coded 180-degree edge rotation of default edge types:
                // e.g. 1 2 3 4 --> 3 4 1 2
                rotatedEdgeTypes[0] = defaultEdgeTypes[2];
                rotatedEdgeTypes[1] = defaultEdgeTypes[3];
                rotatedEdgeTypes[2] = defaultEdgeTypes[0];
                rotatedEdgeTypes[3] = defaultEdgeTypes[1];
                return rotatedEdgeTypes;
            }
            case 3: {
                int[] rotatedEdgeTypes = new int[4];
                int[] defaultEdgeTypes = getEdges();
                // hard-coded 270-degree edge rotation of default edge types:
                // e.g. 1 2 3 4 --> 2 3 4 1
                rotatedEdgeTypes[0] = defaultEdgeTypes[1];
                rotatedEdgeTypes[1] = defaultEdgeTypes[2];
                rotatedEdgeTypes[2] = defaultEdgeTypes[3];
                rotatedEdgeTypes[3] = defaultEdgeTypes[0];
                return rotatedEdgeTypes;
            }
            default:
                // TODO
                throw new RuntimeException("Invalid rotation");
        }
    }

    // 0 1 2 3

    /**
     * <p>1 2 3 —> 7 4 1</p>
     * <p>4 5 6 —> 8 5 2</p>
     * <p>7 8 9	—> 9 6 3</p>
     * @param rotation clockwise rotation in 90 degree steps: <p>0 = no rotation (0 deg)</p><p>1 = 90 deg</p><p>2 = 180 deg</p><p>3 = 270 deg</p>
     * @return new array of the tile features rotated by the given rotation.
     */
    private int[] rotatedFeatures(int rotation) {
        // rotate a 3x3 matrix by 90 degrees: transpose matrix & switch first and last columns
        // e.g. {1 2 3 4 5 6 7 8 9} --> {7 4 1 8 5 2 9 6 3}
        // TODO: implement efficiently (hardcoded)
        switch (rotation){
            case 1: {
                // hard-coded 90-degree rotation of the features
                int[] rotatedMatrix = new int[9];
                rotatedMatrix[0] = features[6];
                rotatedMatrix[1] = features[3];
                rotatedMatrix[2] = features[0];
                rotatedMatrix[3] = features[7];
                rotatedMatrix[4] = features[4];
                rotatedMatrix[5] = features[1];
                rotatedMatrix[6] = features[8];
                rotatedMatrix[7] = features[5];
                rotatedMatrix[8] = features[2];
                return rotatedMatrix;
            }
            case 2: {
                int[] rotatedMatrix = new int[9];
                rotatedMatrix[0] = features[8];
                rotatedMatrix[1] = features[7];
                rotatedMatrix[2] = features[6];
                rotatedMatrix[3] = features[5];
                rotatedMatrix[4] = features[4];
                rotatedMatrix[5] = features[3];
                rotatedMatrix[6] = features[2];
                rotatedMatrix[7] = features[1];
                rotatedMatrix[8] = features[0];
                return rotatedMatrix;
            }
            case 3: {
                int[] rotatedMatrix = new int[9];
                rotatedMatrix[0] = features[2];
                rotatedMatrix[1] = features[5];
                rotatedMatrix[2] = features[8];
                rotatedMatrix[3] = features[1];
                rotatedMatrix[4] = features[4];
                rotatedMatrix[5] = features[7];
                rotatedMatrix[6] = features[0];
                rotatedMatrix[7] = features[3];
                rotatedMatrix[8] = features[6];
                return rotatedMatrix;
            }
            default: {
                throw new RuntimeException("Invalid rotation");
            }

        }

    }

}
