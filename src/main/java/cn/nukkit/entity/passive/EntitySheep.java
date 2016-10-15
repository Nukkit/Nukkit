package cn.nukkit.entity.passive;

import cn.nukkit.entity.ai.CreatureFleeAI;
import cn.nukkit.entity.ai.CreatureWanderAI;
import cn.nukkit.entity.ai.FollowItemAI;
import cn.nukkit.entity.ai.MobAIUnion;
import cn.nukkit.item.Item;
import cn.nukkit.level.format.FullChunk;
import cn.nukkit.nbt.tag.CompoundTag;

/**
 * Author: BeYkeRYkt
 * Nukkit Project
 */
public class EntitySheep extends EntityAnimal {

    public static final int NETWORK_ID = 13;

    public EntitySheep(FullChunk chunk, CompoundTag nbt) {
        super(chunk, nbt);
        setAI(new MobAIUnion(new CreatureFleeAI(this, 32), new FollowItemAI(this, Item.SEEDS, 49, 32), new CreatureWanderAI(this)));
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
        return 1.3f;
    }

    @Override
    public float getEyeHeight() {
        if (isBaby()) {
            return 0.95f * 0.9f; // No have information
        }
        return 0.95f * getHeight();
    }

    @Override
    public String getName() {
        return this.getNameTag();
    }

    @Override
    public Item[] getDrops() {
        return new Item[]{Item.get(Item.WOOL)};
    }

    @Override
    public int getNetworkId() {
        return NETWORK_ID;
    }

    @Override
    public void initEntity() {
        this.setMaxHealth(8);
    }
}
