package cn.nukkit.item;

import cn.nukkit.block.Block;

/**
 * author: MagicDroidX
 * Nukkit Project
 */
public class OakDoor extends Item {

    public OakDoor() {
        this(0, 1);
    }

    public OakDoor(Integer meta) {
        this(meta, 1);
    }

    public OakDoor(Integer meta, int count) {
        super(OAK_DOOR, 0, count, "Oak Door");
        this.block = Block.get(Item.OAK_DOOR_BLOCK);
    }

    @Override
    public int getMaxStackSize() {
        return 1;
    }
}
