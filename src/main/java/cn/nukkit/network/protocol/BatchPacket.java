package cn.nukkit.network.protocol;

/**
 * author: MagicDroidX
 * Nukkit Project
 */
public class BatchPacket extends DataPacket {
    public static final byte NETWORK_ID = ProtocolInfo.BATCH_PACKET;

    public byte[] payload;

    @Override
    public byte pid() {
        return NETWORK_ID;
    }

    @Override
    public void decodePayload() {
        this.payload = this.get();
    }

    @Override
    public void encodePayload() {

    }
}
