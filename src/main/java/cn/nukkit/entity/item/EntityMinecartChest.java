package cn.nukkit.entity.item;

import cn.nukkit.block.*;
import cn.nukkit.item.*;
import cn.nukkit.level.format.FullChunk;
import cn.nukkit.nbt.tag.CompoundTag;

/**
 * Created by Snake1999 on 2016/1/30.
 * Package cn.nukkit.entity.item in project Nukkit.
 */
public class EntityMinecartChest extends EntityMinecartAbstract {

    public static final int NETWORK_ID = 84;

    public EntityMinecartChest(FullChunk chunk, CompoundTag nbt) {
        super(chunk, nbt);
        super.setBlockInside(new BlockChest());
    }

    // TODO: 2016/1/30 inventory

    @Override
    public int getMineId() {
        return 1;
    }

    @Override
    public int getNetworkId() {
        return NETWORK_ID;
    }
    
    @Override
    public Item dropItem() {
        return new ItemMinecartChest();
    }
    
}
