package cn.nukkit.entity.mob;

import cn.nukkit.entity.EntityCreature;
import cn.nukkit.entity.EntityExplosive;
import cn.nukkit.entity.ai.CreatureWanderAI;
import cn.nukkit.entity.ai.HostileMobAI;
import cn.nukkit.entity.ai.MobAIUnion;
import cn.nukkit.entity.data.ByteEntityData;
import cn.nukkit.entity.weather.EntityLightningStrike;
import cn.nukkit.event.entity.CreeperPowerEvent;
import cn.nukkit.event.entity.EntityDamageByEntityEvent;
import cn.nukkit.event.entity.ExplosionPrimeEvent;
import cn.nukkit.item.Item;
import cn.nukkit.level.Explosion;
import cn.nukkit.level.format.FullChunk;
import cn.nukkit.math.Vector3;
import cn.nukkit.nbt.tag.CompoundTag;
import cn.nukkit.utils.FastRandom;

/**
 * @author Box.
 */
public class EntityCreeper extends EntityMob implements EntityExplosive {
    public static final int NETWORK_ID = 33;

    public static final int DATA_SWELL_DIRECTION = 16;
    public static final int DATA_SWELL = 17;
    public static final int DATA_SWELL_OLD = 18;
    public static final int DATA_POWERED = 19;

    public static int EXPLOSION_TIME = 48;
    public static float EXPLOSION_TARGET_DISTANCE_SQUARED = 6f * 6f;
    public static float BLAST_FORCE = 2.8f;

    private int bombTime = 0;

    @Override
    public double getSpeed() {
        return super.getSpeed() - 0.02 * bombTime;
    }

    @Override
    public int getNetworkId() {
        return NETWORK_ID;
    }

    public EntityCreeper(FullChunk chunk, CompoundTag nbt) {
        super(chunk, nbt);
        setAI(new MobAIUnion(new HostileMobAI(this, 100, 32, 0), new CreatureWanderAI(this)));
    }

    public boolean isPowered() {
        return getDataPropertyBoolean(DATA_POWERED);
    }

    @Override
    public float getHeight() {
        return 1.8f;
    }

    @Override
    public boolean onUpdate(int currentTick) {
        if (super.onUpdate(currentTick)) {
            Vector3 target = getTarget();
            if (target != null && target instanceof EntityCreature && target.distanceSquared(this) < EXPLOSION_TARGET_DISTANCE_SQUARED) {
                if (bombTime++ >= EXPLOSION_TIME) {
                    this.explode();
                    return false;
                }
            } else if (bombTime > 0) {
                bombTime = Math.max(0, bombTime - 1);
            }
            return true;
        } else {
            return false;
        }
    }

    public void setPowered(EntityLightningStrike bolt) {
        CreeperPowerEvent ev = new CreeperPowerEvent(this, bolt, CreeperPowerEvent.PowerCause.LIGHTNING);
        this.getServer().getPluginManager().callEvent(ev);

        if (!ev.isCancelled()) {
            this.setDataProperty(new ByteEntityData(DATA_POWERED, 1));
            this.namedTag.putBoolean("powered", true);
        }
    }

    public void setPowered(boolean powered) {
        CreeperPowerEvent ev = new CreeperPowerEvent(this, powered ? CreeperPowerEvent.PowerCause.SET_ON : CreeperPowerEvent.PowerCause.SET_OFF);
        this.getServer().getPluginManager().callEvent(ev);

        if (!ev.isCancelled()) {
            this.setDataProperty(new ByteEntityData(DATA_POWERED, powered ? 1 : 0));
            this.namedTag.putBoolean("powered", powered);
        }
    }

    @Override
    protected void initEntity() {
        super.initEntity();
        if (this.namedTag.getBoolean("powered") || this.namedTag.getBoolean("IsPowered")) {
            this.dataProperties.putBoolean(DATA_POWERED, true);
        }
    }

    @Override
    public void explode() {
        ExplosionPrimeEvent ev = new ExplosionPrimeEvent(this, BLAST_FORCE);
        this.server.getPluginManager().callEvent(ev);
        if(!ev.isCancelled()){
            Explosion explosion = new Explosion(this, (float) ev.getForce(), this);
            if(ev.isBlockBreaking()) {
                explosion.explode();
            } else {
                explosion.explodeB();
            }
        }
        this.close();
    }

    @Override
    public float getWidth() {
        return 0.72f;
    }

    public Item[] getDrops(){
        if(this.lastDamageCause instanceof EntityDamageByEntityEvent){
            switch(FastRandom.random.random(2)){
                case 0:
                    return new Item[]{Item.get(Item.FLINT, 0, 1)};
                case 1:
                    return new Item[]{Item.get(Item.GUNPOWDER, 0, 1)};
                case 2:
                    return new Item[]{Item.get(Item.REDSTONE_DUST, 0, 1)};
            }
        }
        return new Item[0];
    }
}
