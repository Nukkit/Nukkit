package cn.nukkit.entity.ai;

import cn.nukkit.entity.EntityCreature;
import cn.nukkit.math.Vector3;

public class DefaultEntityPathFinder extends PathFinder {
    public DefaultEntityPathFinder(EntityCreature entity, Vector3 target, double distance) {
        super(entity.hasGravity() ? new DefaultVisitor(entity.getLevel(), (int) Math.ceil(entity.getHeight())) : new FlyingVisitor(entity.getLevel(), (int) Math.ceil(entity.getHeight())), new RandomQueue(), distance == 0 ? new DefaultPathGoal() : new EquidistantPathGoal(distance), entity, target);
    }
}
