package cn.nukkit.network.protocol;

public class PlayerSkinPacket extends DataPacket {

    @Override
    public byte pid() {
        return ProtocolInfo.PLAYER_SKIN_PACKET;
    }

    @Override
    public void decode() {

    }

    @Override
    public void encode() {
        //TODO
    }
}
