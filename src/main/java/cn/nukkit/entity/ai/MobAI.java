package cn.nukkit.entity.ai;

import cn.nukkit.entity.Entity;
import cn.nukkit.math.Vector3;

public interface MobAI {
    boolean isValidTarget(Vector3 target);

    Vector3 findTarget();

    Vector3 calculateNextMove(Vector3 target);

    void processAttacker(Entity attacker);
}
