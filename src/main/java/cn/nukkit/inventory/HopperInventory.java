package cn.nukkit.inventory;

import cn.nukkit.blockentity.BlockEntityHopper;

/**
 * author: MagicDroidX
 * Nukkit Project
 */
public class HopperInventory extends ContainerInventory {

    public HopperInventory(BlockEntityHopper hopper) {
        super(hopper, InventoryType.HOPPER);
    }

    @Override
    public BlockEntityHopper getHolder() {
        return (BlockEntityHopper) this.holder;
    }
}
