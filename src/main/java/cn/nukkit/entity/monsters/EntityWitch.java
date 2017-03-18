package cn.nukkit.entity.monsters;

import cn.nukkit.entity.EntityCreature;
import cn.nukkit.level.format.FullChunk;
import cn.nukkit.nbt.tag.CompoundTag;

public class EntityWitch extends EntityCreature {

    public static final int NETWORK_ID = 45;

    public EntityWitch(FullChunk chunk, CompoundTag nbt) {
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
        return 1.0;
    }

    @Override
    protected void initEntity() {
        super.initEntity();
        this.setMaxHealth(26);
    }

    public int getKillExperience() {
        return 5; 
    }
}
