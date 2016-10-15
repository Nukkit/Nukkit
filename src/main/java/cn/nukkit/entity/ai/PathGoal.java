package cn.nukkit.entity.ai;

import cn.nukkit.math.BlockVector3;

public interface PathGoal {
    int distance(PathFinder.Node node, BlockVector3 goal);
}
