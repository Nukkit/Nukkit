package cn.nukkit.inventory.transaction;

import cn.nukkit.Player;

public class InventoryMismatchTransactionGroup extends BaseTransactionGroup {

    @Override
    public boolean executeOn(Player player, boolean force) {
        return false;
    }

    @Override
    public boolean canExecute(Player player) {
        return false;
    }

    @Override
    public int getType() {
        return TYPE_INVENTORY_MISMATCH;
    }
}
