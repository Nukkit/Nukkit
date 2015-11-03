package cn.nukkit.block;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.entity.Effect;
import cn.nukkit.entity.Entity;
import cn.nukkit.event.entity.EntityCombustByBlockEvent;
import cn.nukkit.event.entity.EntityDamageByBlockEvent;
import cn.nukkit.event.entity.EntityDamageEvent;
import cn.nukkit.item.Item;

/**
 * Created on 15-11-3.
 */
public class Lava extends Liquid {

    public Lava() {
        super(Block.LAVA, 0);
    }

    public Lava(int meta) {
        super(Block.LAVA, meta);
    }

    @Override
    public int getLightLevel() {
        return 15;
    }

    @Override
    public String getName() {
        return "Lava";
    }

    @Override
    public void onEntityCollide(Entity entity) {
        entity.fallDistance *= 0.5;

        if (!entity.hasEffect(Effect.FIRE_RESISTANCE)) {
            EntityDamageByBlockEvent e = new EntityDamageByBlockEvent(this, entity, EntityDamageEvent.CAUSE_LAVA, 4);
            entity.attack(e.getFinalDamage(), e);
        }

        EntityCombustByBlockEvent e = new EntityCombustByBlockEvent(this, entity, 15);
        Server.getInstance().getPluginManager().callEvent(e);
        if(!e.isCancelled()){
            entity.setOnFire(e.getDuration());
        }

        entity.resetFallDistance();
    }

    @Override
    public boolean place(Item item, Block block, Block target, int face, double fx, double fy, double fz, Player player) {
        boolean b = getLevel().setBlock(this, this, true, false);
        getLevel().scheduleUpdate(this, tickRate());

        return b;
    }

}
