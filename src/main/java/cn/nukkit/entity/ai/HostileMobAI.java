package cn.nukkit.entity.ai;

import cn.nukkit.Player;
import cn.nukkit.entity.Entity;
import cn.nukkit.entity.EntityCreature;
import cn.nukkit.math.Vector3;
import java.util.Map;

public class HostileMobAI extends PathFinderAI {

    public HostileMobAI(EntityCreature minion, double trackingDistance, double activationDistance, double targetDistance) {
        super(minion, trackingDistance, activationDistance, targetDistance);
    }

    @Override
    public boolean isValidTarget(Vector3 target) {
        if (target instanceof EntityCreature) {
            if (target instanceof Player) {
                Player player = (Player) target;
                return !player.closed && player.spawned && player.isAlive() && !player.isCreative() && player.distanceSquared(minion) <= trackingDistSqr;
            }
            EntityCreature creature = (EntityCreature) target;
            return creature.isAlive() && !creature.closed && creature.distanceSquared(minion) <= trackingDistSqr;
        } else {
            return false;
        }
    }

    @Override
    public Vector3 findTarget() {
        if (searchTime > 0) {
            searchTime--;
            return null;
        }
        searchTime = RECALCULATE_TIME;
        double min = activationDistSqr;
        Player closest = null;
        for (Map.Entry<Long, Player> entry : minion.level.getPlayers().entrySet()) {
            Player player = entry.getValue();
            if (isValidTarget(player)) {
                double distance = player.distanceSquared(minion);
                if (distance < min) {
                    min = distance;
                    closest = player;
                }
            }
        }
        return closest;
    }

    @Override
    public void processAttacker(Entity attacker) {
        // Do nothing
    }
}
