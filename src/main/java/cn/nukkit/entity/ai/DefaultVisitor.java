package cn.nukkit.entity.ai;

import cn.nukkit.block.Block;
import cn.nukkit.block.BlockFence;
import cn.nukkit.block.BlockFenceGate;
import cn.nukkit.level.Level;
import cn.nukkit.math.Vector3;

public class DefaultVisitor implements BlockVisitor {

    private final Level level;
    private final double height;

    public DefaultVisitor(Level level, int height) {
        this.level = level;
        this.height = height;
    }

    private Vector3 temporalVector = new Vector3();

    @Override
    public boolean isVisitable(Vector3 from, Vector3 to) {
        // TODO check bounding box + stuff
        if (to.getY() != from.getY()) { // Check clearance
            if (to.getY() > from.getY()) {
                int start = (int) (from.getY() + height);
                int end = (int) (to.getY() + height);
                for (int y = start; y < end; y++) {
                    if (level.getBlock(temporalVector.setComponents(from.getFloorX(), y, from.getFloorZ())).isSolid()) {
                        return false;
                    }
                }
            } else {
                int start = (int) (to.getY() + height);
                int end = (int) (from.getY() + height);
                for (int y = start; y < end; y++) {
                    if (level.getBlock(temporalVector.setComponents(to.getFloorX(), y, to.getFloorZ())).isSolid()) {
                        return false;
                    }
                }
            }
        }
        Block toFloor = level.getBlock(temporalVector.setComponents(to.getFloorX(), to.getFloorY() - 1, to.getFloorZ()));
        if ((toFloor.isSolid() || toFloor.getId() == Block.WATER || toFloor.getId() == Block.STILL_WATER) && (to.getFloorY() <= from.getFloorY() || !(toFloor instanceof BlockFence) || !(toFloor instanceof BlockFenceGate))) {
            for (int y = 1; y < height; y++) {
                if (level.getBlock(temporalVector.setComponents(to.getFloorX(), to.getFloorY() + y, to.getFloorZ())).isSolid()) {
                    return false;
                }
            }
            if (!level.getBlock(to).isSolid()) {
                return true;
            }
            return false;
        } else {
            return false;
        }
    }
}
