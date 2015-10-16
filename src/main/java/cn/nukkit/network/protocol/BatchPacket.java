package cn.nukkit.network.protocol;

/**
 * @author Nukkit Project Team
 */
public class BatchPacket extends DataPacket {

    public static final byte NETWORK_ID = Info.BATCH_PACKET;

    public byte[] payload;

    @Override
    public byte getNetworkId() {
        return NETWORK_ID;
    }

    @Override
    public void decode() {
        this.payload = get(getInt());
    }

    @Override
    public void encode() {
        this.putInt(this.payload.length);
        this.put(this.payload);
    }

}
