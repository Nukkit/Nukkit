package cn.nukkit.network.protocol;

/**
 * Created on 15-10-14.
 */
public class TakeItemEntityPacket extends DataPacket {

    public static final byte NETWORK_ID = ProtocolInfo.TAKE_ITEM_ENTITY_PACKET;

    public long entityId;
    public long target;

    public TakeItemEntityPacket() {
        super(17);
    }

    @Override
    public void decode() {
    }

    @Override
    public void encode() {
        this.reset();
        this.putVarLong(this.target);
        this.putVarLong(this.entityId);
    }

    @Override
    public byte pid() {
        return NETWORK_ID;
    }

}
