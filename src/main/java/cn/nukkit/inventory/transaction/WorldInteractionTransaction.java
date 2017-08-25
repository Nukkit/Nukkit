package cn.nukkit.inventory.transaction;

import cn.nukkit.item.Item;

public class WorldInteractionTransaction extends BaseTransaction {

    private final int flags;

    public WorldInteractionTransaction(int flags, int slot, Item sourceItem, Item targetItem) {
        super(slot, sourceItem, targetItem);
        this.flags = flags;
    }

    public int getFlags() {
        return flags;
    }

    @Override
    public int getType() {
        return TYPE_WORLD_INTERACTON;
    }
}
