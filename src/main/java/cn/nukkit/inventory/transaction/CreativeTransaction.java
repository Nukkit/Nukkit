package cn.nukkit.inventory.transaction;

import cn.nukkit.item.Item;

public class CreativeTransaction extends BaseTransaction {

    public CreativeTransaction(int slot, Item sourceItem, Item targetItem) {
        super(slot, sourceItem, targetItem);
    }

    @Override
    public int getType() {
        return TYPE_CREATIVE;
    }
}
