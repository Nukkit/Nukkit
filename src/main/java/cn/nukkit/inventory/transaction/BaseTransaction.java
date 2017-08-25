package cn.nukkit.inventory.transaction;

import cn.nukkit.item.Item;

/**
 * author: MagicDroidX
 * Nukkit Project
 */
public abstract class BaseTransaction implements Transaction {

    protected final int slot;

    protected final Item sourceItem;

    protected final Item targetItem;

    protected final long creationTime = System.currentTimeMillis();

    BaseTransaction(int slot, Item sourceItem, Item targetItem) {
        this.slot = slot;
        this.sourceItem = sourceItem.clone();
        this.targetItem = targetItem.clone();
    }

    @Override
    public long getCreationTime() {
        return creationTime;
    }

    @Override
    public int getSlot() {
        return slot;
    }

    @Override
    public Item getSourceItem() {
        return sourceItem.clone();
    }

    @Override
    public Item getTargetItem() {
        return targetItem.clone();
    }
}
