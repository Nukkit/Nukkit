package cn.nukkit.entity.ai;

import cn.nukkit.level.Level;
import cn.nukkit.math.Vector3;

public class FlyingVisitor implements BlockVisitor {

    private final Level level;
    private final double height;

    public FlyingVisitor(Level level, int height) {
        this.level = level;
        this.height = height;
    }

    private Vector3 temporalVector = new Vector3();


    @Override
    public boolean isVisitable(Vector3 from, Vector3 to) {
        for (int y = 0; y < height; y++) {
            if (level.getBlock(temporalVector.setComponents(to.getFloorX(), to.getFloorY() + y, to.getFloorZ())).isSolid()) {
                return false;
            }
        }
        return true;
    }
}