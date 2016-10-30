package cn.nukkit.entity.ai;

import cn.nukkit.entity.Entity;
import cn.nukkit.entity.EntityCreature;
import cn.nukkit.math.BlockVector3;
import cn.nukkit.math.Vector3;
import cn.nukkit.utils.FastRandom;
import java.util.ArrayList;
import java.util.List;

public class CreatureWanderAI implements MobAI{

    private final BlockVisitor visitor;
    public int SEARCH_DEPTH = 1000;
    public int RECALCULATE_TIME = 20;

    public final EntityCreature minion;

    private final List<Vector3> directions = new ArrayList<>();

    private int recalculate = 0;
    private BlockVector3 lastFrom;
    private Vector3 lastTo;

    public CreatureWanderAI(EntityCreature minion) {
        this.minion = minion;
        this.visitor = minion.hasGravity() ? new DefaultVisitor(minion.getLevel(), (int) Math.ceil(minion.getHeight())) : new FlyingVisitor(minion.getLevel(), (int) Math.ceil(minion.getHeight()));
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
    }

    @Override
    public boolean isValidTarget(Vector3 target) {
        return lastFrom == null || lastTo != null;
    }

    @Override
    public Vector3 findTarget() {
        lastFrom = null;
        return null;
    }

    @Override
    public Vector3 calculateNextMove(Vector3 target) {
        // Return cached move
        BlockVector3 pos = new BlockVector3(minion.getFloorX(), minion.getFloorY(), minion.getFloorZ());
        if (pos.equals(lastFrom)) {
            return lastTo;
        }
        lastFrom = pos;

        // No positions and not allowed to recalculate yet
        if (recalculate-- > 0) {
            return lastTo = null;
        }

        recalculate = RECALCULATE_TIME;
        Vector3 next = minion.add(directions.get(FastRandom.random.random(directions.size())));
        if (visitor.isVisitable(minion, next)) {
            return lastTo = next;
        }
        return lastTo = null;
    }

    @Override
    public void processAttacker(Entity attacker) {
        // Do nothing
    }
}
