package cn.nukkit.entity.monsters;

import cn.nukkit.entity.EntityAgeable;
import cn.nukkit.entity.EntityCreature;
import cn.nukkit.event.entity.EntityDamageByEntityEvent;
import cn.nukkit.item.Item;
import cn.nukkit.level.Level;
import cn.nukkit.level.format.FullChunk;
import cn.nukkit.nbt.tag.CompoundTag;
import co.aikar.timings.Timings;
import cn.nukkit.entity.mobutils.Drops;

import java.util.ArrayList;
import java.util.List;

public class EntityHusk extends EntityCreature implements EntityAgeable {
  

    public static final int NETWORK_ID = 47;

    public EntityHusk(FullChunk chunk, CompoundTag nbt) {
        super(chunk, nbt);
    }

    @Override
    public int getNetworkId() {
        return NETWORK_ID;
    }

    @Override
    public float getWidth() {
        return 0.72f;
    }

    @Override
    public float getHeight() {
        return 1.8f;
    }

    public double getSpeed() {
        return 1.1;
    }

    @Override
    protected void initEntity() {
        super.initEntity();
        setMaxHealth(20);
    }

    @Override
    public boolean isBaby() {
        return false;
    }

    @Override
    public boolean entityBaseTick(int tickDiff) {
        boolean hasUpdate = false;
        Timings.entityBaseTickTimer.startTiming();

        hasUpdate = super.entityBaseTick(tickDiff);

        int time = this.getLevel().getTime() % Level.TIME_FULL;
        if (!this.isOnFire() && !this.level.isRaining() && !(time >= Level.TIME_NIGHT && time < Level.TIME_SUNRISE)) {
            this.setOnFire(100);
        }

        Timings.entityBaseTickTimer.stopTiming();
        return hasUpdate;
    }

    @Override
    public Item[] getDrops() {
        List<Item> drops = new ArrayList<>();
        if (this.lastDamageCause instanceof EntityDamageByEntityEvent) {
            int rottenFlesh = Drops.rand(0, 3); // drops 0-2 rotten flesh
            for (int i = 0; i < rottenFlesh; i++) {
                drops.add(Item.get(Item.ROTTEN_FLESH, 0, 1));
            }
        }
        return drops.toArray(new Item[drops.size()]);
    }

    public int getKillExperience() {
        return 5;
    }

}
