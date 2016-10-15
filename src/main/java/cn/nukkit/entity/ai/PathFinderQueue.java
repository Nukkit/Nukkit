package cn.nukkit.entity.ai;

import cn.nukkit.math.BlockVector3;
import java.util.Queue;

public interface PathFinderQueue {
    Queue<PathFinder.Node> getNewQueue(BlockVector3 target);
}
