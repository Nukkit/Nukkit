package cn.nukkit.inventory.transaction;

import cn.nukkit.item.Item;

public class GlobalTransaction extends BaseTransaction {

    public GlobalTransaction(int slot, Item sourceItem, Item targetItem) {
        super(slot, sourceItem, targetItem);
    }

    @Override
    public int getType() {
        return TYPE_GLOBAL;
    }
}
