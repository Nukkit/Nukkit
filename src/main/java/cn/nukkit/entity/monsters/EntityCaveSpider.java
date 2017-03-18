package cn.nukkit.entity.monsters;

import cn.nukkit.entity.EntityCreature;
import cn.nukkit.event.entity.EntityDamageByEntityEvent;
import cn.nukkit.item.Item;
import cn.nukkit.level.format.FullChunk;
import cn.nukkit.nbt.tag.CompoundTag;
import cn.nukkit.entity.mobutils.Drops;

import java.util.ArrayList;
import java.util.List;

public class EntityCaveSpider extends EntityCreature {

    public static final int NETWORK_ID = 40;

    public EntityCaveSpider(FullChunk chunk, CompoundTag nbt) {
        super(chunk, nbt);
    }

    @Override
    public int getNetworkId() {
        return NETWORK_ID;
    }

    @Override
    public float getWidth() {
        return 0.9f;
    }

    @Override
    public float getHeight() {
        return 0.8f;
    }

    public double getSpeed() {
        return 1.3;
    }

    @Override
    public void initEntity() {
        super.initEntity();

        this.setMaxHealth(12);
    }

    @Override
    public Item[] getDrops() {
        List<Item> drops = new ArrayList<>();
        if (this.lastDamageCause instanceof EntityDamageByEntityEvent) {
            int strings = Drops.rand(0, 3); // drops 0-2 strings
            int spiderEye = Drops.rand(0, 3) == 0 ? 1 : 0; // with a 1/3 chance it drops spider eye
            for (int i=0; i < strings; i++) {
                drops.add(Item.get(Item.STRING, 0, 1));
            }
            for (int i=0; i < spiderEye; i++) {
                drops.add(Item.get(Item.SPIDER_EYE, 0, 1));
            }
        }
        return drops.toArray(new Item[drops.size()]);
    }
    
    public int getKillExperience () {
        return 5; // gain 5 experience
    }

}
