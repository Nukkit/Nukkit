package cn.nukkit.network.protocol;

/**
 * Created on 15-10-14.
 */
public class TakeItemEntityPacket extends DataPacket {

    public static final byte NETWORK_ID = Info.TAKE_ITEM_ENTITY_PACKET;

    public long entityId;
    public long target;

    @Override
    public void decode() {
        ;
    }

    @Override
    public void encode() {
        putLong(entityId);
        putLong(target);
    }

    @Override
    public byte getNetworkId() {
        return NETWORK_ID;
    }

}
