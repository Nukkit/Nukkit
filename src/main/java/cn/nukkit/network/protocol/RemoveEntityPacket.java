package cn.nukkit.network.protocol;

/**
 * @author Nukkit Project Team
 */
public class RemoveEntityPacket extends DataPacket {

    public static final byte NETWORK_ID = Info.REMOVE_ENTITY_PACKET;

    public long eid;

    @Override
    public byte getNetworkId() {
        return NETWORK_ID;
    }

    @Override
    public void decode() {

    }

    @Override
    public void encode() {
        this.putLong(eid);
    }
}
