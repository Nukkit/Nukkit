package cn.nukkit.entity.monsters;

import cn.nukkit.entity.EntityCreature;
import cn.nukkit.item.Item;
import cn.nukkit.level.format.FullChunk;
import cn.nukkit.nbt.tag.CompoundTag;

public class Spider extends EntityCreature {

    public static final int NETWORK_ID = 35;

    public Spider(FullChunk chunk, CompoundTag nbt) {
        super(chunk, nbt);
    }

    @Override
    public int getNetworkId() {
        return NETWORK_ID;
    }

    @Override
    public float getWidth() {
        return 1.3f;
    }

    @Override
    public float getHeight() {
        return 1.12f;
    }

    @Override
    public float getEyeHeight() {
        return 1;
    }

    public double getSpeed() {
        return 1.13;
    }

    @Override
    public void initEntity() {
        super.initEntity();
        this.setMaxHealth(16);
    }

   @Override
    public Item[] getDrops() {
        return new Item[]{Item.get(Item.STRING), Item.get(Item.SPIDER_EYE)};   
    }

    public int getKillExperience() {
        return 5;
    }

}
