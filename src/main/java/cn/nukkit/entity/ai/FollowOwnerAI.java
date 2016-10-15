package cn.nukkit.entity.ai;

import cn.nukkit.entity.Entity;
import cn.nukkit.entity.passive.EntityTameable;
import cn.nukkit.math.Vector3;

public class FollowOwnerAI extends PathFinderAI {

    public FollowOwnerAI(EntityTameable minion, double trackingDistance) {
        super(minion, trackingDistance, 0, 0);
    }

    public EntityTameable getTamed() {
        return (EntityTameable) minion;
    }

    @Override
    public boolean isValidTarget(Vector3 target) {
        return target != null && target == getTamed().getOwner();
    }

    @Override
    public Vector3 findTarget() {
        return getTamed().getOwner();
    }

    @Override
    public void processAttacker(Entity attacker) {
        // Do nothing
    }
}
