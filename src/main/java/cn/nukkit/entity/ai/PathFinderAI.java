package cn.nukkit.entity.ai;

import cn.nukkit.entity.EntityCreature;
import cn.nukkit.math.BlockVector3;
import cn.nukkit.math.Vector3;
import java.util.HashMap;
import java.util.Map;

public abstract class PathFinderAI implements MobAI {

    public final int SEARCH_DEPTH = 1000; // Lower this to decrease cpu
    public final int RECALCULATE_TIME = 20; // Raise this to decrease cpu
    public final int RECALCULATE_DISTANCE_SQUARED = 8; // Raise this to decrease cpu

    public final double activationDistSqr; // Lower this to decrease cpu
    public final double trackingDistSqr; // Lower this to decrease cpu
    public final double attackDistance;

    public final EntityCreature minion;

    public PathFinderAI(EntityCreature minion, double trackingDistance, double activationDistance, double targetDistance) {
        this.minion = minion;
        this.trackingDistSqr = trackingDistance * trackingDistance;
        this.activationDistSqr = activationDistance * activationDistance;
        this.attackDistance = targetDistance;
    }


    public int recalculate = 0;
    public int searchTime = 0;
    public Map<BlockVector3, BlockVector3> movements = new HashMap<>();
    public PathFinder finder;
    private BlockVector3 lastFrom;
    private Vector3 lastTo;
    private Vector3 lastTarget;
    public final Vector3 temporal = new Vector3();

    public PathFinder getPathFinder(Vector3 target) {
        return new DefaultEntityPathFinder(minion, target, attackDistance);
    }

    @Override
    public Vector3 calculateNextMove(Vector3 target) {
        // Return cached move
        BlockVector3 pos = new BlockVector3(minion.getFloorX(), minion.getFloorY(), minion.getFloorZ());
        if (pos.equals(lastFrom)) {
            return lastTo;
        }

        // No positions and not allowed to recalculate yet
        if (movements.isEmpty() && recalculate-- > 0) {
            return null;
        }

        // Get the next move
        BlockVector3 next = movements.get(pos);
        if (next == null || target.distanceSquared(lastTarget) > RECALCULATE_DISTANCE_SQUARED) {
            if (recalculate-- <= 0) { // No valid move and not allowed to recalculate yet
                lastTarget = new Vector3(target);
                // Calculate a new path
                recalculate = RECALCULATE_TIME;
                finder = getPathFinder(target);
                if (minion.hasGravity()) {
                    finder.addWalkingDirections();
                } else {
                    finder.addFlyingDirection();
                }
                PathFinder.Node result = finder.solve(SEARCH_DEPTH);
                if (result == null) { // No result
                    return null;
                }
                movements = result.toBlockMap();
                next = movements.get(pos);
            }
        }
        // Return the next move
        if (next != null) {
            if (finder.isVisitable(minion, temporal.setComponents(next.x, next.y, next.z))) {
                recalculate--;
                lastFrom = pos;
                return lastTo = temporal.setComponents(next.x + 0.5, Math.max(next.y, pos.y), next.z + 0.5);
            }
        }
        return null;
    }
}
