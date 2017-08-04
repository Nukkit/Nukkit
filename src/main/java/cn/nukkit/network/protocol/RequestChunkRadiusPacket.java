package cn.nukkit.network.protocol;

/**
 * author: MagicDroidX
 * Nukkit Project
 */
public class RequestChunkRadiusPacket extends DataPacket {

    public static final byte NETWORK_ID = ProtocolInfo.REQUEST_CHUNK_RADIUS_PACKET;

    public int radius;

    @Override
    public void decodePayload() {
        this.radius = this.getVarInt();
    }

    @Override
    public void encodePayload() {

    }

    @Override
    public byte pid() {
        return NETWORK_ID;
    }

}
