package cn.nukkit.item;

/**
 * author: Justin
 */

import cn.nukkit.block.Block;

public class Skull extends Item{
    public Skull() {
        this(0, 1);
    }

    public Skull(Integer meta) {
        this(meta, 1);
    }

    public Skull(Integer meta, int count) {
        super(SKULL, 0, count, "Skull");
        this.block = Block.get(Item.SKULL_BLOCK);
    }

    @Override
    public int getMaxStackSize() {
        return 64;
    }
}
