package cn.nukkit.inventory;

import cn.nukkit.Player;

/**
 * author: MagicDroidX
 * Nukkit Project
 */
public class CraftingGrid extends BaseInventory {

    public CraftingGrid(InventoryHolder holder) {
        super(holder, InventoryType.CRAFTING);
    }

    public int getDefaultSize() {
        return 4;
    }

    public void setSize(int size) {
        throw new RuntimeException("Cannot change the size of a crafting grid");
    }

    public String getName() {
        return "Crafting";
    }

    public void sendSlot(int index, Player... target) {
        //we can't send a inventorySlot of a client-sided inventory window
    }

    public void sendContents(Player... target) {
        //we can't send the contents of a client-sided inventory window
    }
}
