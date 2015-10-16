package cn.nukkit.network.protocol;

/**
 * @author Nukkit Project Team
 */
public class InteractPacket extends DataPacket {

    public static final byte NETWORK_ID = Info.INTERACT_PACKET;

    public long eid;
    public byte action;
    public long target;

    @Override
    public void decode() {
        action = getByte();
        target = getLong();
    }

    @Override
    public void encode() {
        putByte(action);
        putLong(target);
    }

    @Override
    public byte getNetworkId() {
        return NETWORK_ID;
    }

}
