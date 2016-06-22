package cn.nukkit.network.protocol;

/**
 * author: boybook
 * Nukkit Project
 */

public class MapInfoRequestPacket extends DataPacket {
    public static final byte NETWORK_ID = ProtocolInfo.MAP_INFO_REQUEST_PACKET;

    @Override
    public byte pid() {
        return NETWORK_ID;
    }

    public long mapId;

    @Override
    public void decode() {
        this.mapId = getLong();
    }

    @Override
    public void encode() {
        this.reset();
        putLong(mapId);
    }
}
