package cn.nukkit.network.protocol;

import cn.nukkit.utils.Binary;

import java.util.Map;

/**
 * @author Nukkit Project Team
 */
public class SetEntityDataPacket extends DataPacket {
    public static final byte NETWORK_ID = Info.SET_ENTITY_DATA_PACKET;

    @Override
    public byte getNetworkId() {
        return NETWORK_ID;
    }

    public long eid;
    public Map<Integer, Object[]> metadata;

    @Override
    public void decode() {

    }

    @Override
    public void encode() {
        this.putLong(this.eid);
        this.put(Binary.writeMetadata(this.metadata));
    }
}
