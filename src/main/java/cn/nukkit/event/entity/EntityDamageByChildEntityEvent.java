package cn.nukkit.event.entity;

import cn.nukkit.entity.Entity;

/**
 * author: MagicDroidX
 * Nukkit Project
 */
public class EntityDamageByChildEntityEvent extends EntityDamageByEntityEvent {

    private Entity childEntity;

    public EntityDamageByChildEntityEvent(Entity damager, Entity childEntity, Entity entity, int cause, float damage) {
        this(damager, childEntity, entity, cause, damage, 0);
    }

    public EntityDamageByChildEntityEvent(Entity damager, Entity childEntity, Entity entity, int cause, float damage, float knockBack) {
        super(damager, entity, cause, damage);
        this.childEntity = childEntity;
        this.setKnockBack(knockBack);
    }

    public Entity getChild() {
        return childEntity;
    }
}
