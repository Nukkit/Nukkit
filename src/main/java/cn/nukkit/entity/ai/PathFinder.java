package cn.nukkit.entity.ai;

import cn.nukkit.math.BlockVector3;
import cn.nukkit.math.Vector3;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class PathFinder {

    private final List<Vector3> directions = new ArrayList<>();
    private final Queue<Node> queue;
    private final BlockVisitor visitor;
    private final BlockVector3 start, end;
    private final PathGoal goal;
    private Node solution;

    public PathFinder(BlockVisitor visitor, PathFinderQueue queue, PathGoal goal, Vector3 start, Vector3 end) {
        this.end = new BlockVector3(end.getFloorX(), end.getFloorY(), end.getFloorZ());
        this.start = new BlockVector3(start.getFloorX(), start.getFloorY(), start.getFloorZ());
        this.visitor = visitor;
        this.goal = goal;
        this.queue = queue.getNewQueue(this.end);

    }

    public void addWalkingDirections() {
        this.directions.add(new Vector3(-1, 0, 0));
        this.directions.add(new Vector3(1, 0, 0));
        this.directions.add(new Vector3(0, 0, -1));
        this.directions.add(new Vector3(0, 0, 1));
        // Jumping
        this.directions.add(new Vector3(-1, 1, 0));
        this.directions.add(new Vector3(1, 1, 0));
        this.directions.add(new Vector3(0, 1, -1));
        this.directions.add(new Vector3(0, 1, 1));
        // Falling
        this.directions.add(new Vector3(-1, -1, 0));
        this.directions.add(new Vector3(1, -1, 0));
        this.directions.add(new Vector3(0, -1, -1));
        this.directions.add(new Vector3(0, -1, 1));
        // Falling 2 blocks
        this.directions.add(new Vector3(-1, -2, 0));
        this.directions.add(new Vector3(1, -2, 0));
        this.directions.add(new Vector3(0, -2, -1));
        this.directions.add(new Vector3(0, -2, 1));
        // Falling 3 blocks
        this.directions.add(new Vector3(-1, -3, 0));
        this.directions.add(new Vector3(1, -3, 0));
        this.directions.add(new Vector3(0, -3, -1));
        this.directions.add(new Vector3(0, -3, 1));
    }

    public void addFlyingDirection() {
        // Directions
        this.directions.add(new Vector3(-1, 0, 0));
        this.directions.add(new Vector3(1, 0, 0));
        this.directions.add(new Vector3(0, 0, -1));
        this.directions.add(new Vector3(0, 0, 1));
        this.directions.add(new Vector3(0, 1, 0));
        this.directions.add(new Vector3(0, -1, 0));
    }

    public boolean isVisitable(Vector3 from, Vector3 to) {
        return visitor.isVisitable(from, to);
    }

    public Collection<Vector3> getDirections() {
        return this.directions;
    }

    private BlockVector3[] getIntDirections() {
        BlockVector3[] array = new BlockVector3[directions.size()];
        for (int i = 0; i < array.length; i++) {
            Vector3 dir = directions.get(i);
            array[i] = new BlockVector3(dir.getFloorX(), dir.getFloorY(), dir.getFloorZ());
        }
        return array;
    }

    private Map<Node, Node> visited = new ConcurrentHashMap<>();

    public Node solve(int maxDepth) {
        Node startNode = new Node(null, start.x, start.y, start.z, 0);
        if (startNode.distance() == 0) {
            return null;
        }
        queue.clear();
        queue.add(startNode);
        Node from = null;
        Node adjacent;
        Vector3 mutable = new Vector3();
        Vector3 mutable2 = new Vector3();
        BlockVector3[] dirs = getIntDirections();
        for (int layer = 0; !queue.isEmpty() && layer <= maxDepth; layer++) {
            from = queue.poll();
            mutable.x = from.getX();
            mutable.y = from.getY();
            mutable.z = from.getZ();
            for (BlockVector3 direction : dirs) {
                mutable2.x = from.getX() + direction.x;
                mutable2.y = from.getY() + direction.y;
                mutable2.z = from.getZ() + direction.z;
                if (from.parent != null) {
                    if (from.parent.x == mutable2.x && from.parent.z == mutable2.z && from.parent.y == mutable2.y) {
                        continue;
                    }
                }
                if (isVisitable(mutable, mutable2)) {
                    adjacent = new Node(from, mutable2.getFloorX(), mutable2.getFloorY(), mutable2.getFloorZ(), from.depth);
                    if (adjacent.distance() == 0) {
                        return optimize(adjacent);
                    }
                    if (!visited.containsKey(adjacent)) {
                        visited.put(adjacent, adjacent);
                        queue.add(adjacent);
                    }
                }
            }
        }
        // No solution found, return closest node
        Node min = from;
        int distanceMin = Integer.MAX_VALUE;
        int moveMin = Integer.MAX_VALUE;
        for (Map.Entry<Node, Node> entry : visited.entrySet()) {
            Node node = entry.getKey();
            if (node.distance() < distanceMin || (node.distance() == distanceMin && node.depth < moveMin)) {
                min = node;
                distanceMin = node.distance();
                moveMin = node.depth;
            }
        }
        if (min.x == start.x && min.y == start.y && min.z == start.z) { // No movement
            return null;
        }
        return optimize(min);
    }

    public Node optimize(Node node) {
        // TODO replace flat sections with diagonal shortcut, use isVisitable(a, b)
        return node;
    }

    public final class Node {
        private Node parent;
        private int x,y,z, depth;

        public Node(Node parent, int x, int y, int z, int depth) {
            this.parent = parent;
            this.x = x;
            this.y = y;
            this.z = z;
            this.depth = depth;
        }

        private int distaince = Integer.MAX_VALUE;

        public int distance() {
            if (distaince == Integer.MAX_VALUE) {
                return distaince = goal.distance(this, end);
            } else {
                return distaince;
            }
        }

        private Node(Node node) {
            this.parent = node.parent;
            this.x = node.x;
            this.y = node.y;
            this.z = node.z;
            this.depth = node.depth;
        }

        private final void set(Node parent, int x, int y, int z, int depth) {
            this.parent = parent;
            this.x = x;
            this.y = y;
            this.z = z;
            this.depth = depth;
        }

        private final void set(Node node) {
            this.parent = node.parent;
            this.x = node.x;
            this.y = node.y;
            this.z = node.z;
            this.depth = node.depth;
        }

        public Node getParent() {
            return parent;
        }

        public int getMoves() {
            return depth;
        }

        public Map<BlockVector3, BlockVector3> toBlockMap() {
            Map<BlockVector3, BlockVector3> movements = new HashMap<>();
            PathFinder.Node result = this;
            // Loop over the result
            while (result != null) {
                Node previous = result.getParent();
                if (previous != null) {
                    if (result.x != previous.x || result.z != previous.z) { // Interpolate jumping
                        if (result.getY() > previous.getY()) {
                            movements.put(new BlockVector3(previous.getX(), result.getY(), previous.getZ()), new BlockVector3(result.getX(), result.getY(), result.getZ()));
                        } else if (result.getY() < previous.getY()) {
                            movements.put(new BlockVector3(result.getX(), previous.getY(), result.getZ()), new BlockVector3(result.getX(), result.getY(), result.getZ()));
                        }
                    }
                    movements.put(new BlockVector3(previous.getX(), previous.getY(), previous.getZ()), new BlockVector3(result.getX(), result.getY(), result.getZ()));
                    result = previous;
                } else {
                    break;
                }
            }
            return movements;
        }

        @Override
        public final int hashCode() {
            return (x ^ (z << 12)) ^ (y << 24);
        }

        public final int getX() {
            return x;
        }

        public final int getY() {
            return y;
        }

        public final int getZ() {
            return z;
        }

        @Override
        public String toString() {
            return x + "," + y + "," + z;
        }

        @Override
        public boolean equals(Object obj) {
            Node other = (Node) obj;
            return other.x == x && other.z == z && other.y == y;
        }
    }
}
