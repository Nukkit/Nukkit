package cn.nukkit.level;

import cn.nukkit.block.Block;
import cn.nukkit.block.BlockNetherPortal;
import cn.nukkit.math.Vector3;

/**
 * Created by CreeperFace on 8.8.2017.
 */
public class Teleporter {

    private Level level;

    public Teleporter(Level level) {
        this.level = level;
    }

    public Vector3 findPortalPosition(Vector3 pos, Dimension to) { //TODO: a lot more complex portal finding
        Vector3 target = to == Dimension.NETHER ? pos.divide(8) : pos.multiply(8);
        target = level.getSafeSpawn(target);


        if (level.getBlockIdAt(target.getFloorX(), target.getFloorY(), target.getFloorZ()) == Block.NETHER_PORTAL) {
            return target;
        }

        if (BlockNetherPortal.trySpawnPortal(level, pos, to == Dimension.NETHER)) {
            return pos;
        }

        return null;
    }
}
