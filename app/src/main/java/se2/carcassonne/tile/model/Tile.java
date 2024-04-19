package se2.carcassonne.tile.model;

public class Tile {
    int tileId;

    String tileName;

//    TODO: check how to access the image resource int or String?
    int tileImage;
    String northEdgeType;
    String southEdgeType;
    String eastEdgeType;
    String westEdgeType;

    public Tile() {
    }

    public Tile(int tileId, String tileName, int tileImage, String northEdgeType, String southEdgeType, String eastEdgeType, String westEdgeType) {
        this.tileId = tileId;
        this.tileName = tileName;
        this.tileImage = tileImage;
        this.northEdgeType = northEdgeType;
        this.southEdgeType = southEdgeType;
        this.eastEdgeType = eastEdgeType;
        this.westEdgeType = westEdgeType;
    }

    public int getTileId() {
        return tileId;
    }

    public void setTileId(int tileId) {
        this.tileId = tileId;
    }

    public String getTileName() {
        return tileName;
    }

    public void setTileName(String tileName) {
        this.tileName = tileName;
    }

    public int getTileImage() {
        return tileImage;
    }

    public void setTileImage(int tileImage) {
        this.tileImage = tileImage;
    }

    public String getNorthEdgeType() {
        return northEdgeType;
    }

    public void setNorthEdgeType(String northEdgeType) {
        this.northEdgeType = northEdgeType;
    }

    public String getSouthEdgeType() {
        return southEdgeType;
    }

    public void setSouthEdgeType(String southEdgeType) {
        this.southEdgeType = southEdgeType;
    }

    public String getEastEdgeType() {
        return eastEdgeType;
    }

    public void setEastEdgeType(String eastEdgeType) {
        this.eastEdgeType = eastEdgeType;
    }

    public String getWestEdgeType() {
        return westEdgeType;
    }

    public void setWestEdgeType(String westEdgeType) {
        this.westEdgeType = westEdgeType;
    }
}


