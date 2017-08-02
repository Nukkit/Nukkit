package cn.nukkit.inventory.transaction;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.event.inventory.InventoryTransactionEvent;
import cn.nukkit.inventory.Inventory;
import cn.nukkit.inventory.PlayerInventory;
import cn.nukkit.item.Item;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class NormalTransactionGroup extends BaseTransactionGroup {

    @Override
    public void addTransaction(Transaction transaction) {
        if (this.transactions.contains(transaction)) {
            return;
        }

        for (Transaction tx : new HashSet<>(this.transactions)) {
            if (tx.getType() == Transaction.TYPE_CONTAINER
                    && transaction.getType() == Transaction.TYPE_CONTAINER
                    && ((ContainerTransaction) tx).getInventoryId() == ((ContainerTransaction) transaction).getInventoryId()
                    && tx.getSlot() == transaction.getSlot()) {
                if (transaction.getCreationTime() >= tx.getCreationTime()) {
                    this.transactions.remove(tx);
                } else {
                    return;
                }
            }
        }

        this.transactions.add(transaction);
    }

    protected boolean matchItems(Player player) {
        List<Item> haveItems = new ArrayList<>();
        List<Item> needItems = new ArrayList<>();

        for (Transaction ts : this.transactions) {
            if (ts.getTargetItem().getId() != Item.AIR) {
                needItems.add(ts.getTargetItem());
            }
            Item sourceItem = ts.getSourceItem();
            if (ts.getType() == Transaction.TYPE_CONTAINER) {
                Item checkSourceItem = ((ContainerTransaction) ts).getInventory(player).getItem(ts.getSlot());
                if (!checkSourceItem.deepEquals(sourceItem) || sourceItem.getCount() != checkSourceItem.getCount()) {
                    return false;
                }
            }
            if (sourceItem.getId() != Item.AIR) {
                haveItems.add(sourceItem);
            }
        }

        for (Item needItem : new ArrayList<>(needItems)) {
            for (Item haveItem : new ArrayList<>(haveItems)) {
                if (needItem.deepEquals(haveItem)) {
                    int amount = Math.min(haveItem.getCount(), needItem.getCount());
                    needItem.setCount(needItem.getCount() - amount);
                    haveItem.setCount(haveItem.getCount() - amount);
                    if (haveItem.getCount() == 0) {
                        haveItems.remove(haveItem);
                    }
                    if (needItem.getCount() == 0) {
                        needItems.remove(needItem);
                        break;
                    }
                }
            }
        }

        return haveItems.isEmpty() && needItems.isEmpty();
    }

    @Override
    public boolean canExecute(Player player) {
        return !this.transactions.isEmpty() && this.matchItems(player);
    }

    @Override
    public boolean executeOn(Player player, boolean force) {
        if (this.hasExecuted || (!force && !this.canExecute(player))) {
            return false;
        }

        InventoryTransactionEvent ev = new InventoryTransactionEvent(this);
        Server.getInstance().getPluginManager().callEvent(ev);
        if (ev.isCancelled()) {
            for (Transaction ts : transactions) {
                Inventory inv;
                if (ts.getType() == Transaction.TYPE_CONTAINER && (inv = ((ContainerTransaction) ts).getInventory(player)) instanceof PlayerInventory) {
                    ((PlayerInventory) inv).sendArmorContents(player);
                    inv.sendContents(player);
                }
            }
            return false;
        }

        for (Transaction ts : this.transactions) {
            if (ts.getType() == Transaction.TYPE_CONTAINER) {
                Inventory inv = ((ContainerTransaction) ts).getInventory(player);
                if (inv != null) {
                    inv.setItem(ts.getSlot(), ts.getTargetItem());
                }
            }
        }

        this.hasExecuted = true;

        return true;
    }

    @Override
    public int getType() {
        return TYPE_NORMAL;
    }
}
