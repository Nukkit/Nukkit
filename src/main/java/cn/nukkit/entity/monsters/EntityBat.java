package cn.nukkit.entity.monsters;

import cn.nukkit.entity.EntityCreature;
import cn.nukkit.item.Item;
import cn.nukkit.level.format.FullChunk;
import cn.nukkit.nbt.tag.CompoundTag;

public class EntityBat extends EntityCreature {

    public static final int NETWORK_ID = 19;

    public EntityBat(FullChunk chunk, CompoundTag nbt) {
        super(chunk, nbt);
    }

    @Override
    public int getNetworkId() {
        return NETWORK_ID;
    }

    @Override
    public float getWidth() {
        return 0.3f;
    }

    @Override
    public float getHeight() {
        return 0.3f;
    }

    @Override
    public void initEntity() {
        super.initEntity();

        this.setMaxHealth(6);
    }

    @Override
    public Item[] getDrops() {
        return new Item[0];
    }

    public int getKillExperience() {
        return 0;
    }
}
