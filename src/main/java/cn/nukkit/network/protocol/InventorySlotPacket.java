package cn.nukkit.network.protocol;

import cn.nukkit.item.Item;

/**
 * author: MagicDroidX
 * Nukkit Project
 */
public class InventorySlotPacket extends DataPacket {
    public static final byte NETWORK_ID = ProtocolInfo.INVENTORY_SLOT_PACKET;

    @Override
    public byte pid() {
        return NETWORK_ID;
    }

    public int inventoryId;
    public int slot;
    public Item item;

    @Override
    public void decode() {
        this.inventoryId = this.getVarInt();
        this.slot = this.getVarInt();
        this.item = this.getSlot();
    }

    @Override
    public void encode() {
        this.reset();
        this.putVarInt((byte) this.inventoryId);
        this.putVarInt(this.slot);
        this.putSlot(this.item);
    }
}
