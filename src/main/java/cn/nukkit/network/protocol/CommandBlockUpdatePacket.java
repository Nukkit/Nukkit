package cn.nukkit.network.protocol;

public class CommandBlockUpdatePacket extends DataPacket {

    @Override
    public byte pid() {
        return ProtocolInfo.COMMAND_BLOCK_UPDATE_PACKET;
    }

    @Override
    public void decode() {
        //TODO
    }

    @Override
    public void encode() {

    }
}
