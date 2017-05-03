package cn.nukkit.entity.passive;

import cn.nukkit.entity.mobutils.Drops;
import cn.nukkit.event.entity.EntityDamageByEntityEvent;
import cn.nukkit.item.Item;
import cn.nukkit.level.format.FullChunk;
import cn.nukkit.nbt.tag.CompoundTag;

import java.util.ArrayList;
import java.util.List;

/**
 * Author: BeYkeRYkt
 * Nukkit Project
 */
public class EntityCow extends EntityAnimal {

    public static final int NETWORK_ID = 11;

    public EntityCow(FullChunk chunk, CompoundTag nbt) {
        super(chunk, nbt);
    }

    @Override
    public float getWidth() {
        return 0.9f;
    }

    @Override
    public float getHeight() {
        if (isBaby()) {
            return 0.65f;
        }
        return 1.3f;
    }

    @Override
    public float getEyeHeight() {
        if (isBaby()) {
            return 0.65f;
        }
        return 1.2f;
    }

    @Override
    public String getName() {
        return this.getNameTag();
    }
    
    @Override
    public Item[] getDrops() {
        List<Item> drops = new ArrayList<>();
        if (this.lastDamageCause instanceof EntityDamageByEntityEvent) {
            int leather = Drops.rand(0, 3); // drops 0-2 leather
            int rawbeef = Drops.rand(1, 4); // drops 1-3 rawbeef
            for (int i=0; i < leather; i++) {
                drops.add(Item.get(Item.LEATHER, 0, 1));
            }
            for (int i=0; i < rawbeef; i++) {
                drops.add(Item.get(Item.RAW_BEEF, 0, 1));
            }
        }
        return drops.toArray(new Item[drops.size()]);
    }

    @Override
    public int getNetworkId() {
        return NETWORK_ID;
    }

    @Override
    protected void initEntity() {
        super.initEntity();
        setMaxHealth(10);
    }
    
    public int getKillExperience() {
        return 3;
    }
}
