package cn.nukkit.network.protocol;

/**
 * @author Nukkit Project Team
 */
public class LevelEventPacket extends DataPacket {

    public static final byte NETWORK_ID = Info.LEVEL_EVENT_PACKET;

    public short eventId;
    public float x;
    public float y;
    public float z;
    public int data;

    @Override
    public void decode() {

    }

    @Override
    public void encode() {
        this.putShort(this.eventId);
        this.putFloat(this.x);
        this.putFloat(this.y);
        this.putFloat(this.z);
        this.putInt(this.data);
    }

    @Override
    public byte getNetworkId() {
        return NETWORK_ID;
    }

}
