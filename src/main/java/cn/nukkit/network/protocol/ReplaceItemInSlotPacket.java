package cn.nukkit.network.protocol;

import cn.nukkit.item.Item;

/**
 * Created by Pub4Game on 29.04.2016.
 */
public class ReplaceItemInSlotPacket extends DataPacket {

    public static final byte NETWORK_ID = ProtocolInfo.REPLACE_ITEM_IN_SLOT_PACKET;

    public Item item;

    @Override
    public void decodePayload() {

    }

    @Override
    public void encodePayload() {
        this.putSlot(this.item);
    }

    @Override
    public byte pid() {
        return NETWORK_ID;
    }

}