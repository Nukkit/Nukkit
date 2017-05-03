package cn.nukkit.entity.monsters;

import cn.nukkit.entity.EntityCreature;
import cn.nukkit.item.Item;
import cn.nukkit.level.format.FullChunk;
import cn.nukkit.nbt.tag.CompoundTag;

public class EntitySilverfish extends EntityCreature {

    public static final int NETWORK_ID = 39;

    public EntitySilverfish(FullChunk chunk, CompoundTag nbt) {
        super(chunk, nbt);
    }

    @Override
    public int getNetworkId() {
        return NETWORK_ID;
    }

    @Override
    public float getWidth() {
        return 0.45f;
    }

    @Override
    public float getHeight() {
        return 0.3f;
    }

    public double getSpeed() {
        return 1.4;
    }

    @Override
    public void initEntity() {
        super.initEntity();

        this.setMaxHealth(8);
    }
    
    public int getKillExperience () {
        return 5; // gain 5 experience
    }
}
