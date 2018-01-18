package cn.nukkit.network.protocol;

/**
 * author: MagicDroidX
 * Nukkit Project
 */
public class RemoveEntityPacket extends DataPacket {

    public long eid;

    @Override
    public byte pid(PlayerProtocol protocol) {
        return protocol.equals(PlayerProtocol.PLAYER_PROTOCOL_113) ?
                ProtocolInfo113.REMOVE_ENTITY_PACKET :
                ProtocolInfo.REMOVE_ENTITY_PACKET;
    }

    @Override
    public void decode(PlayerProtocol protocol) {

    }

    @Override
    public void encode(PlayerProtocol protocol) {
        this.reset(protocol);
        this.putEntityUniqueId(this.eid);
    }
}
