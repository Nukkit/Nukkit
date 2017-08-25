package cn.nukkit.inventory.transaction;

import cn.nukkit.Player;
import cn.nukkit.inventory.Inventory;
import cn.nukkit.item.Item;

public class ContainerTransaction extends BaseTransaction {

    private int inventoryId;

    public ContainerTransaction(int inventoryId, int slot, Item sourceItem, Item targetItem) {
        super(slot, sourceItem, targetItem);
        this.inventoryId = inventoryId;
    }

    public Inventory getInventory(Player player) {
        return player.getWindowById(inventoryId);
    }

    public int getInventoryId() {
        return inventoryId;
    }

    @Override
    public int getType() {
        return TYPE_CONTAINER;
    }
}
