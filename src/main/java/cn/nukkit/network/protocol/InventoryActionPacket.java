package cn.nukkit.network.protocol;

import cn.nukkit.item.Item;

public class InventoryActionPacket extends DataPacket {

    public static final byte NETWORK_ID = ProtocolInfo.INVENTORY_ACTION_PACKET;

    public int unknown0;
    public Item item;
    public int unknown1;
    public int unknown2;

    @Override
    public void decode() {

    }

    @Override
    public void encode() {
        this.reset();
        this.putUnsignedVarInt(this.unknown0);
        this.putSlot(this.item);
        this.putVarInt(this.unknown1);
        this.putVarInt(this.unknown2);
    }

    @Override
    public byte pid() {
        return NETWORK_ID;
    }
}
