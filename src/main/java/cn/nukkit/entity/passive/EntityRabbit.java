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
public class EntityRabbit extends EntityAnimal {

    public static final int NETWORK_ID = 18;

    public EntityRabbit(FullChunk chunk, CompoundTag nbt) {
        super(chunk, nbt);
    }

    @Override
    public float getWidth() {
        return 0.9f;
    }

    @Override
    public float getHeight() {
        if (isBaby()) {
            return 0.9f; // No have information
        }
        return 0.9f;
    }

    @Override
    public float getEyeHeight() {
        if (isBaby()) {
            return 0.9f; // No have information
        }
        return 0.9f;
    }

    @Override
    public String getName() {
        return this.getNameTag();
    }

    @Override
    public Item[] getDrops() {
        List<Item> drops = new ArrayList<>();
        if (this.lastDamageCause instanceof EntityDamageByEntityEvent) {
            int rawrabbit = Drops.rand(1, 3); // drops 1-2 raws
            int rabbithide = Drops.rand(1, 3); // drops 1-2 hide
            int rabbitfoot = Drops.rand(0, 101) <= 9 ? 1 : 0; // with a 8,5% chance a rabbit foot is dropped
            for (int i=0; i < rawrabbit; i++) {
                drops.add(Item.get(Item.RAW_RABBIT, 0, 1));
            }
            for (int i=0; i < rabbithide; i++) {
                drops.add(Item.get(Item.RABBIT_HIDE, 0, 1));
            }
            for (int i=0; i < rabbitfoot; i++) {
                drops.add(Item.get(Item.RABBIT_FOOT, 0, 1));
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
}
