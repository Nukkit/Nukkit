package cn.nukkit.entity.ai;

import cn.nukkit.entity.Entity;
import cn.nukkit.entity.EntityCreature;
import cn.nukkit.entity.mob.EntityMob;
import cn.nukkit.entity.passive.EntityTameable;
import cn.nukkit.math.Vector3;
import java.util.HashSet;

public class NeutralMobAI extends PathFinderAI {

    public NeutralMobAI(EntityCreature minion, double trackingDistance, double activationDistance, double targetDistance) {
        super(minion, trackingDistance, activationDistance, targetDistance);
    }

    @Override
    public boolean isValidTarget(Vector3 target) {
        return target instanceof EntityMob || hostile.contains(target);
    }

    private HashSet<Entity> hostile = new HashSet<>();

    @Override
    public Vector3 findTarget() {
        if (searchTime > 0) {
            searchTime--;
            return null;
        }
        searchTime = RECALCULATE_TIME;
        double min = activationDistSqr;
        Entity closest = null;
        for (Entity entity : minion.level.getEntities()) {
            if (isValidTarget(entity)) {
                double distance = entity.distanceSquared(minion);
                if (distance < min) {
                    min = distance;
                    closest = entity;
                }
            }
        }
        return closest;
    }

    @Override
    public void processAttacker(Entity attacker) {
        // Do nothing
        if (minion instanceof EntityTameable) {
            EntityTameable tameable = (EntityTameable) minion;
            if (tameable.isTamed() && tameable.getOwner() == attacker) {
                return;
            }
        }
        hostile.add(attacker);
    }
}
