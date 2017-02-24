package cn.nukkit.entity.monsters;

import cn.nukkit.entity.EntityCreature;
import cn.nukkit.item.Item;
import cn.nukkit.level.format.FullChunk;
import cn.nukkit.nbt.tag.CompoundTag;

public class EntityEnderman extends EntityCreature {

    public static final int NETWORK_ID = 38;

    public EntityEnderman(FullChunk chunk, CompoundTag nbt) {
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
        return 2.8f;
    }

    public double getSpeed() {
        return 1.21;
    }

    @Override
    protected void initEntity() {
        this.setMaxHealth(40);
        super.initEntity();
    }

    @Override
    public Item[] getDrops() {
       return new Item[]{Item.get(Item.ENDER_PEARL)};
    }

    public int getKillExperience() {
        return 5; 
    }
}
