package cn.nukkit.entity.ai;

import cn.nukkit.math.BlockVector3;

public class EquidistantPathGoal implements PathGoal {
    private final double d2;

    public EquidistantPathGoal(double distance) {
        this.d2 = distance * distance;
    }

    @Override
    public int distance(PathFinder.Node node, BlockVector3 goal) {
        int dx = goal.x - node.getX();
        int dy = goal.y - node.getY();
        int dz = goal.z - node.getZ();
        return dx * dx + dy * dy + dz * dz;
    }
}
