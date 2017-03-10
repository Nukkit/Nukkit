package cn.nukkit.item;

import cn.nukkit.block.BlockHopper;

/**
 * Created by Pub4Game on 03.07.2016.
 */
public class ItemHopper extends Item {

    public ItemHopper() {
        this(0, 1);
    }

    public ItemHopper(Integer meta) {
        this(meta, 1);
    }

    public ItemHopper(Integer meta, int count) {
        super(HOPPER, meta, count, "Hopper");
        this.block = new BlockHopper();
    }
}
