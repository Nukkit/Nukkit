package cn.nukkit.network.protocol;

import cn.nukkit.inventory.transaction.*;
import cn.nukkit.math.BlockFace;

import java.util.ArrayList;
import java.util.List;

public class InventoryTransactionPacket extends DataPacket {

    public TransactionGroup transaction;

    @Override
    public byte pid() {
        return ProtocolInfo.INVENTORY_TRANSACTION_PACKET;
    }

    @Override
    public void encode() {
        this.putVarInt(transaction.getType());
        this.putVarInt(transaction.getTransactions().size());
        for (Transaction ts : transaction.getTransactions()) {
            this.putVarInt(ts.getType());
            switch (ts.getType()) {
                case Transaction.TYPE_CONTAINER:
                    this.putVarInt(((ContainerTransaction) ts).getInventoryId());
                    break;
                case Transaction.TYPE_WORLD_INTERACTON:
                    this.putVarInt(((WorldInteractionTransaction) ts).getFlags());
                    break;
                case Transaction.TYPE_CRAFT:
                    this.putVarInt(((CraftTransaction) ts).getAction());
                    break;
            }
            this.putVarInt(ts.getSlot());
            this.putSlot(ts.getSourceItem());
            this.putSlot(ts.getTargetItem());
        }
        switch (transaction.getType()) {
            case TransactionGroup.TYPE_ITEM_USE:
                ItemUseTransactionGroup iu = (ItemUseTransactionGroup) transaction;
                this.putVarInt(iu.getAction());
                this.putBlockVector3(iu.getBlockPos());
                this.putVarInt(iu.getFace());
                this.putVarInt(iu.getSlot());
                this.putSlot(iu.getItem());
                this.putVector3f(iu.getPlayerPos());
                this.putVector3f(iu.getClickPos());
                break;
            case TransactionGroup.TYPE_ITEM_USE_ON_ENTITY:
                ItemUseOnEntityTransactionGroup oe = (ItemUseOnEntityTransactionGroup) transaction;
                this.putVarLong(oe.getEntityId());
                this.putVarInt(oe.getAction());
                this.putVarInt(oe.getSlot());
                this.putSlot(oe.getItem());
                this.putVector3f(oe.getPlayerPos());
                break;
            case TransactionGroup.TYPE_ITEM_RELEASE:
                ItemReleaseTransactionGroup ir = (ItemReleaseTransactionGroup) transaction;
                this.putVarInt(ir.getAction());
                this.putVarInt(ir.getSlot());
                this.putSlot(ir.getItem());
                this.putVector3f(ir.getPlayerPos());
        }
    }

    @Override
    public void decode() {
        int type = this.getVarInt();
        int count = this.getVarInt();
        List<Transaction> list = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            Transaction ts;
            switch (this.getVarInt()) {
                case Transaction.TYPE_CONTAINER:
                    ts = new ContainerTransaction(this.getVarInt(), this.getVarInt(), this.getSlot(), this.getSlot());
                    break;
                case Transaction.TYPE_GLOBAL:
                    ts = new GlobalTransaction(this.getVarInt(), this.getSlot(), this.getSlot());
                    break;
                case Transaction.TYPE_WORLD_INTERACTON:
                    ts = new WorldInteractionTransaction(this.getVarInt(), this.getVarInt(), this.getSlot(), this.getSlot());
                    break;
                case Transaction.TYPE_CREATIVE:
                    ts = new CreativeTransaction(this.getVarInt(), this.getSlot(), this.getSlot());
                    break;
                case Transaction.TYPE_CRAFT:
                    ts = new CraftTransaction(this.getVarInt(), this.getVarInt(), this.getSlot(), this.getSlot());
                    break;
                default:
                    continue;
            }
            list.add(ts);
        }
        switch (type) {
            case TransactionGroup.TYPE_NORMAL:
                transaction = new NormalTransactionGroup();
                break;
            case TransactionGroup.TYPE_INVENTORY_MISMATCH:
                transaction = new InventoryMismatchTransactionGroup();
                break;
            case TransactionGroup.TYPE_ITEM_USE:
                transaction = new ItemUseTransactionGroup(
                        this.getVarInt(),
                        this.getBlockVector3(),
                        this.getVarInt(),
                        this.getVarInt(),
                        this.getSlot(),
                        this.getVector3f(),
                        this.getVector3f()
                );
                break;
            case TransactionGroup.TYPE_ITEM_USE_ON_ENTITY:
                transaction = new ItemUseOnEntityTransactionGroup(
                        this.getVarLong(),
                        this.getVarInt(),
                        this.getVarInt(),
                        this.getSlot(),
                        this.getVector3f()
                );
                break;
            case TransactionGroup.TYPE_ITEM_RELEASE:
                transaction = new ItemReleaseTransactionGroup(
                        this.getVarInt(),
                        this.getVarInt(),
                        this.getSlot(),
                        this.getVector3f()
                );
                break;
            default:
                return;
        }
        list.forEach(transaction::addTransaction);
    }
}
