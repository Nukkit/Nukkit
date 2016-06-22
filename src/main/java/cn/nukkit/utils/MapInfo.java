package cn.nukkit.utils;

/**
 * author: boybook
 * Nukkit Project
 */

public class MapInfo implements Cloneable {

    public long mapId;
    public byte updateType;
    public byte direction;
    public String levelName;
    public byte x;
    public byte z;
    public int col;
    public int row;
    public int xOffset;
    public int zOffset;
    public byte[] data;

    @Override
    public String toString() {
        return "MapId: {mapId}, UpdateType: {updateType}, Direction: {direction}, X: {x}, Z: {z}, Col: {col}, Row: {row}, X-offset: {xOffset}, Z-offset: {zOffset}, Data: {data.length}";
}

    @Override
    public MapInfo clone() {
        MapInfo clone = new MapInfo();
        clone.mapId = mapId;
        clone.updateType = updateType;
        clone.direction = direction;
        clone.levelName = levelName;
        clone.x = x;
        clone.z = z;
        clone.col = col;
        clone.row = row;
        clone.xOffset = xOffset;
        clone.zOffset = zOffset;
        clone.data = data.clone();

        return clone;
    }

}
