package cn.nukkit.entity.passive;

import java.util.ArrayList;
import java.util.List;

import cn.nukkit.event.entity.EntityDamageByEntityEvent;
import cn.nukkit.item.Item;
import cn.nukkit.level.format.FullChunk;
import cn.nukkit.nbt.tag.CompoundTag;
import cn.nukkit.entity.mobutils.Drops;

public class EntityMooshroom extends EntityAnimal {

    public static final int NETWORK_ID = 16;

    public EntityMooshroom(FullChunk chunk, CompoundTag nbt) {
        super(chunk, nbt);
    }

    @Override
    public int getNetworkId() {
        return NETWORK_ID;
    }

    @Override
    public float getWidth() {
        return 1.45f;
    }

    @Override
    public float getHeight() {
        return 1.12f;
    }

    @Override
    public void initEntity() {
        super.initEntity();
        this.setMaxHealth(10);
    }

    @Override
    public Item[] getDrops() {
        List<Item> drops = new ArrayList<>();
        if (this.lastDamageCause instanceof EntityDamageByEntityEvent) {
            int leatherDrop = Drops.rand(0, 3); // drops 0-2 leather
            int beefDrop = Drops.rand(1, 4); // drops 1-3 raw beef / steak when on fire
            for (int i=0; i < leatherDrop; i++) {
                drops.add(Item.get(Item.LEATHER, 0, 1));
            }
            for (int i=0; i < beefDrop; i++) {
                drops.add(Item.get(this.isOnFire() ? Item.STEAK : Item.RAW_BEEF, 0, 1));
            }
        }
        return drops.toArray(new Item[drops.size()]);
    }

    public int getKillExperience () {
        return Drops.rand(1, 4); // gain 1-3 experience
    }
}
