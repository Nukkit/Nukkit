package cn.nukkit.network.protocol;

/**
 * author: MagicDroidX
 * Nukkit Project
 */
public class MoveEntityPacket extends DataPacket {
    public static final byte NETWORK_ID = ProtocolInfo.MOVE_ENTITY_PACKET;

    public long eid;
    public float x;
    public float y;
    public float z;
    public float yaw;
    public float headYaw;
    public float pitch;

    @Override
    public byte pid() {
        return NETWORK_ID;
    }

    @Override
    public void decode() {

    }

    @Override
    public void encode() {
        this.reset();
        this.putLong(eid);
        this.putFloat(x);
        this.putFloat(y);
        this.putFloat(z);
        this.putByte((byte) (pitch / (360.0f / 256)));
        this.putByte((byte) (yaw / (360.0f / 256)));
        this.putByte((byte) (headYaw / (360.0f / 256)));
    }
}
