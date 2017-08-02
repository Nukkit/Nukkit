package cn.nukkit.inventory.transaction;

import cn.nukkit.Player;

import java.util.HashSet;
import java.util.Set;

/**
 * author: MagicDroidX
 * Nukkit Project
 */
public abstract class BaseTransactionGroup implements TransactionGroup {

    private final long creationTime = System.currentTimeMillis();
    protected boolean hasExecuted = false;

    protected final Set<Transaction> transactions = new HashSet<>();

    @Override
    public long getCreationTime() {
        return creationTime;
    }

    @Override
    public Set<Transaction> getTransactions() {
        return transactions;
    }

    @Override
    public void addTransaction(Transaction transaction) {
        if (this.transactions.contains(transaction)) {
            return;
        }

        this.transactions.add(transaction);
    }

    @Override
    public boolean executeOn(Player player) {
        return executeOn(player, false);
    }

    @Override
    public boolean hasExecuted() {
        return this.hasExecuted;
    }
}
