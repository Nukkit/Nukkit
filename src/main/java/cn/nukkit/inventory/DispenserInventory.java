package cn.nukkit.inventory;

import cn.nukkit.blockentity.BlockEntityDispenser;

/**
 * author: MagicDroidX
 * Nukkit Project
 */
public class DispenserInventory extends ContainerInventory {

    public DispenserInventory(BlockEntityDispenser hopper) {
        super(hopper, InventoryType.DISPENSER);
    }

    @Override
    public BlockEntityDispenser getHolder() {
        return (BlockEntityDispenser) this.holder;
    }
}
