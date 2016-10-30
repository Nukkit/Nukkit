package cn.nukkit.entity.ai;

import cn.nukkit.math.BlockVector3;
import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.Queue;

public class ASQueue implements PathFinderQueue {
    public Queue<PathFinder.Node> getNewQueue(BlockVector3 target) {
        return new PriorityQueue<PathFinder.Node>(new Comparator<PathFinder.Node>() {
            @Override
            public int compare(PathFinder.Node a, PathFinder.Node b) {
                int aValue = a.getMoves() + a.distance();
                int bValue = b.getMoves() + b.distance();
                return Integer.compare(aValue, bValue);
            }
        });
    }
}
