package cn.nukkit.network.protocol;

public class SetLastHurtByPacket extends DataPacket {

    @Override
    public byte pid(PlayerProtocol protocol) {
        return protocol.equals(PlayerProtocol.PLAYER_PROTOCOL_113) ?
                0 :
                ProtocolInfo.SET_LAST_HURT_BY_PACKET;
    }

    @Override
    public void decode(PlayerProtocol protocol) {

    }

    @Override
    public void encode(PlayerProtocol protocol) {
        //TODO
    }
}
