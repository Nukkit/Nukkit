package cn.nukkit.inventory.transaction;

import cn.nukkit.Player;
import cn.nukkit.item.Item;
import cn.nukkit.math.Vector3f;

public class ItemUseOnEntityTransactionGroup extends BaseTransactionGroup {

    private final long entityId;
    private final int action;
    private final int slot;
    private final Item item;
    private final Vector3f playerPos;

    public ItemUseOnEntityTransactionGroup(long entityId, int action, int slot, Item item, Vector3f playerPos) {
        this.entityId = entityId;
        this.action = action;
        this.slot = slot;
        this.item = item;
        this.playerPos = playerPos;
    }

    @Override
    public boolean executeOn(Player player, boolean force) {
        return false;
    }

    @Override
    public boolean canExecute(Player player) {
        return false;
    }

    public long getEntityId() {
        return entityId;
    }

    public int getAction() {
        return action;
    }

    public int getSlot() {
        return slot;
    }

    public Item getItem() {
        return item;
    }

    public Vector3f getPlayerPos() {
        return playerPos;
    }

    @Override
    public int getType() {
        return TYPE_ITEM_USE_ON_ENTITY;
    }
}
