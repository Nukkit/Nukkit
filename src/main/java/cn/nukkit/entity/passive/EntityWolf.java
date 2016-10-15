package cn.nukkit.entity.passive;

import cn.nukkit.entity.ai.CreatureWanderAI;
import cn.nukkit.entity.ai.FollowOwnerAI;
import cn.nukkit.entity.ai.MobAIUnion;
import cn.nukkit.entity.ai.NeutralMobAI;
import cn.nukkit.item.Item;
import cn.nukkit.level.format.FullChunk;
import cn.nukkit.nbt.tag.CompoundTag;

/**
 * Author: BeYkeRYkt
 * Nukkit Project
 */
public class EntityWolf extends EntityTameable {

    public static final int NETWORK_ID = 14;

    public EntityWolf(FullChunk chunk, CompoundTag nbt) {
        super(chunk, nbt);
        setAI(new MobAIUnion(new NeutralMobAI(this, 16, 16, 0), new FollowOwnerAI(this, 128), new CreatureWanderAI(this)));
    }

    @Override
    public float getWidth() {
        return 0.6f;
    }

    @Override
    public float getLength() {
        return 0.6f;
    }

    @Override
    public float getHeight() {
        if (isBaby()) {
            return 0.8f; // No have information
        }
        return 0.8f;
    }

    @Override
    public double getSpeed(){
        return 1.2;
    }

    @Override
    public float getEyeHeight() {
        if (isBaby()) {
            return 0.8f * getHeight(); // No have information
        }
        return 0.8f * getHeight();
    }

    @Override
    public String getName() {
        return this.getNameTag();
    }

    @Override
    public Item[] getDrops() {
        return new Item[]{};
    }

    @Override
    public int getNetworkId() {
        return NETWORK_ID;
    }

    @Override
    public void initEntity() {
        super.initEntity();
        setMaxHealth(8);
    }
}
