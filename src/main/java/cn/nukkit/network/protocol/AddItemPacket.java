package cn.nukkit.network.protocol;

import cn.nukkit.item.Item;

public class AddItemPacket extends DataPacket {
    public static final byte NETWORK_ID = ProtocolInfo.ADD_ITEM_PACKET;

    @Override
    public byte pid() {
        return NETWORK_ID;
    }

    public Item item;

    @Override
    public void decodePayload() {

    }

    @Override
    public void encodePayload() {
        this.putSlot(item);
    }
}
