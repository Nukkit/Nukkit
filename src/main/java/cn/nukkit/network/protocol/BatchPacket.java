package cn.nukkit.network.protocol;

/**
 * author: MagicDroidX
 * Nukkit Project
 */
public class BatchPacket extends DataPacket {
    public static final byte NETWORK_ID = ProtocolInfo.BATCH_PACKET;

    public byte[] payload;

    public BatchPacket() {
        super(null);
    }

    @Override
    public byte pid() {
        return NETWORK_ID;
    }

    @Override
    public void decode() {
        this.payload = this.get(this.getInt());
    }

    @Override
    public void encode() {
        setBuffer(new byte[5 + this.payload.length]);
        this.reset();
        this.putInt(this.payload.length);
        this.put(this.payload);
    }
}
