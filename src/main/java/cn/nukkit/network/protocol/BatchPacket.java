package cn.nukkit.network.protocol;

import cn.nukkit.utils.Zlib;
import java.util.zip.Deflater;

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
        this.payload = this.getByteArray();
    }

    @Override
    public void encode() {
        setBuffer(new byte[5 + this.payload.length]);
        this.reset();
        this.putByteArray(this.payload);
    }

    public static BatchPacket compressPackets(DataPacket[] packets, int level, int strategy) throws Exception {
        byte[] data = DataPacket.join(packets);
        Deflater deflater = new Deflater();
        byte[] compressed = Zlib.deflate(deflater, data, level, strategy);
        final BatchPacket pk = new BatchPacket();
        pk.payload = compressed;
        pk.encode();
        pk.isEncoded = true;
        return pk;
    }
}
