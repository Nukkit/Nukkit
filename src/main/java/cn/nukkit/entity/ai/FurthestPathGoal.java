package cn.nukkit.entity.ai;

import cn.nukkit.math.BlockVector3;

public class FurthestPathGoal implements PathGoal {

    @Override
    public int distance(PathFinder.Node node, BlockVector3 goal) {
        return Integer.MAX_VALUE - (Math.abs(node.getX() - goal.x) + Math.abs(node.getY() - goal.y) + Math.abs(node.getZ() - goal.z));
    }
}
