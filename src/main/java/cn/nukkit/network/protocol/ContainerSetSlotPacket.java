package cn.nukkit.network.protocol;

import cn.nukkit.item.Item;

/**
 * @author Nukkit Project Team
 */
public class ContainerSetSlotPacket extends DataPacket {

    public static final byte NETWORK_ID = Info.CONTAINER_SET_SLOT_PACKET;

    public byte  windowId;
    public short slot;

    public Item  item;

    @Override
    public void decode() {
        windowId = getByte();
        slot     = getShort();
        item     = getItem();
    }

    @Override
    public void encode() {
        putByte(windowId);
        putShort(slot);
        putItem(item);
    }

    @Override
    public byte getNetworkId() {
        return NETWORK_ID;
    }

}
