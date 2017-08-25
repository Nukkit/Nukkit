package cn.nukkit.inventory.transaction;

import cn.nukkit.item.Item;

public class CraftTransaction extends BaseTransaction {

    private final int action;

    public CraftTransaction(int action, int slot, Item sourceItem, Item targetItem) {
        super(slot, sourceItem, targetItem);
        this.action = action;
    }

    public int getAction() {
        return action;
    }

    @Override
    public int getType() {
        return TYPE_CRAFT;
    }
}
