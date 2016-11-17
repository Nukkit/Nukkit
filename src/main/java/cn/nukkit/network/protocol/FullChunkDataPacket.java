package cn.nukkit.network.protocol;

/**
 * author: MagicDroidX
 * Nukkit Project
 */
public class FullChunkDataPacket extends DataPacket {
    public static final byte NETWORK_ID = ProtocolInfo.FULL_CHUNK_DATA_PACKET;

    public static final byte ORDER_COLUMNS = 0;
    public static final byte ORDER_LAYERED = 1;

    public FullChunkDataPacket() {
        super(null);
    }

    @Override
    public byte pid() {
        return NETWORK_ID;
    }

    public int chunkX;
    public int chunkZ;
    public byte order = ORDER_COLUMNS;
    public byte[] data;

    @Override
    public void decode() {

    }

    @Override
    public void encode() {
        setBuffer(new byte[14 + data.length]);
        this.reset();
        this.putVarInt(this.chunkX);
        this.putVarInt(this.chunkZ);
        this.putByte(this.order);
        this.putByteArray(this.data);
    }
}
