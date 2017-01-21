package cn.nukkit.inventory;

import cn.nukkit.blockentity.BlockEntityDropper;

/**
 * author: MagicDroidX
 * Nukkit Project
 */
public class DropperInventory extends ContainerInventory {

    public DropperInventory(BlockEntityDropper hopper) {
        super(hopper, InventoryType.DROPPER);
    }

    @Override
    public BlockEntityDropper getHolder() {
        return (BlockEntityDropper) this.holder;
    }
}
