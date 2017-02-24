package cn.nukkit.entity.monsters;

import cn.nukkit.entity.EntityCreature;
import cn.nukkit.item.Item;
import cn.nukkit.level.format.FullChunk;
import cn.nukkit.nbt.tag.CompoundTag;

public class EntityZombieVillager extends EntityCreature {

    public static final int NETWORK_ID = 44;

    public EntityZombieVillager(FullChunk chunk, CompoundTag nbt) {
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
    public void initEntity() {
        super.initEntity();
        setMaxHealth(20);
    }

    @Override
    public Item[] getDrops() {
       return new Item[]{Item.get(Item.ROTTEN_FLESH)};  
    }

    public int getKillExperience() {
        return 5; 
    }

}
