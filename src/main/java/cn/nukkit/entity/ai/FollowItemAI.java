package cn.nukkit.entity.ai;

import cn.nukkit.Player;
import cn.nukkit.entity.Entity;
import cn.nukkit.entity.EntityCreature;
import cn.nukkit.math.Vector3;
import java.util.Map;

public class FollowItemAI extends PathFinderAI {
    private final int item;

    public FollowItemAI(EntityCreature minion, int item, double trackingDistance, double activationDistance) {
        super(minion, trackingDistance, activationDistance, 0);
        this.item = item;
    }


    @Override
    public boolean isValidTarget(Vector3 target) {
        if(target instanceof Player){
            Player player = (Player) target;
            return player.isAlive() && !player.closed && (item == -1 || player.getInventory().getItemInHand().getId() == item) && player.distanceSquared(minion) <= trackingDistSqr;
        }
        return false;
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
