package cn.nukkit.network.protocol;

/**
 * author: MagicDroidX
 * Nukkit Project
 */
public class SetEntityMotionPacket extends DataPacket {
    public static final byte NETWORK_ID = ProtocolInfo.SET_ENTITY_MOTION_PACKET;

    public long entityId;
    public double motionX;
    public double motionY;
    public double motionZ;

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

        this.putLong(entityId);
        this.putFloat((float) motionX);
        this.putFloat((float) motionY);
        this.putFloat((float) motionZ);

    }
}
