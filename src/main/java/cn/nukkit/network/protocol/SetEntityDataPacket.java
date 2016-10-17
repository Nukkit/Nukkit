package cn.nukkit.network.protocol;

import cn.nukkit.entity.data.EntityMetadata;
import cn.nukkit.utils.Binary;

/**
 * author: MagicDroidX
 * Nukkit Project
 */
public class SetEntityDataPacket extends DataPacket {
    public static final byte NETWORK_ID = ProtocolInfo.SET_ENTITY_DATA_PACKET;

    public SetEntityDataPacket() {
        super(null);
    }

    @Override
    public byte pid() {
        return NETWORK_ID;
    }

    public long eid;
    public EntityMetadata metadata;

    @Override
    public void decode() {

    }

    @Override
    public void encode() {
        byte[] meta = Binary.writeMetadata(this.metadata);
        setBuffer(new byte[9 + meta.length]);
        this.reset();
        this.putLong(this.eid);
        this.put(meta);
    }
}
