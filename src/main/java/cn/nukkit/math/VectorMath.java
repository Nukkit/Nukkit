package cn.nukkit.math;

/**
 * author: MagicDroidX, PeratX
 * Nukkit Project
 */

public abstract class VectorMath {

    public static Vector2 getDirection2D(double azimuth) {
        return new Vector2(Math.cos(azimuth), Math.sin(azimuth));
    }

    public static BlockVector3 getSideOffset(int side){
        switch (side){
            case BlockVector3.SIDE_UP:
                return new BlockVector3(0, 1, 0);
            case BlockVector3.SIDE_DOWN:
                return new BlockVector3(0, -1, 0);
            case BlockVector3.SIDE_NORTH:
                return new BlockVector3(-1, 0, 0);
            case BlockVector3.SIDE_SOUTH:
                return new BlockVector3(1, 0, 0);
            case BlockVector3.SIDE_EAST:
                return new BlockVector3(0, 0, -1);
            case BlockVector3.SIDE_WEST:
                return new BlockVector3(0, 0, 1);
        }
        return new BlockVector3(0, 0, 0);
    }
}
