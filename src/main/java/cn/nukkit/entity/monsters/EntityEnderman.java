package cn.nukkit.entity.monsters;

import cn.nukkit.entity.EntityCreature;
import cn.nukkit.entity.mobutils.Drops;
import cn.nukkit.event.entity.EntityDamageByEntityEvent;
import cn.nukkit.item.Item;
import cn.nukkit.level.format.FullChunk;
import cn.nukkit.nbt.tag.CompoundTag;

import java.util.ArrayList;
import java.util.List;

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
        List<Item> drops = new ArrayList<>();
        if (this.lastDamageCause instanceof EntityDamageByEntityEvent) {
            int EnderPearl = Drops.rand(0, 3); // drops 0-2 Pearls
            for (int i = 0; i < EnderPearl; i++) {
                drops.add(Item.get(Item.ENDER_PEARL, 0, 1));
            }
        }
        return drops.toArray(new Item[drops.size()]);
    }
    
    public int getKillExperience() {
        return 5; 
    }
}