package cn.nukkit.entity.ai;

import cn.nukkit.entity.Entity;
import cn.nukkit.entity.EntityCreature;
import cn.nukkit.math.Vector3;

public class CreatureFleeAI extends PathFinderAI {

    private Vector3 attacker;

    public CreatureFleeAI(EntityCreature minion, double fleeDistance) {
        super(minion, fleeDistance, fleeDistance, 0);
    }

    @Override
    public boolean isValidTarget(Vector3 target) {
        return target == attacker && target != null && target.distanceSquared(minion) < trackingDistSqr;
    }

    @Override
    public Vector3 findTarget() {
        return attacker;
    }

    @Override
    public void processAttacker(Entity attacker) {
        this.attacker = attacker;
    }

    @Override
    public PathFinder getPathFinder(Vector3 target) {
        return new PathFinder(minion.hasGravity() ? new DefaultVisitor(minion.getLevel(), (int) Math.ceil(minion.getHeight())) : new FlyingVisitor(minion.getLevel(), (int) Math.ceil(minion.getHeight())), new RandomQueue(), new FurthestPathGoal(), minion, target);
    }

    @Override
    public Vector3 calculateNextMove(Vector3 target) {
        if (attacker == null) {
            return null;
        }
        Vector3 result = super.calculateNextMove(target);
        if (result == null) {
            attacker = null;
        }
        return result;
    }
}
