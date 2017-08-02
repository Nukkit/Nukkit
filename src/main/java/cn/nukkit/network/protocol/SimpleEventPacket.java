package cn.nukkit.network.protocol;

public class SimpleEventPacket extends DataPacket {

    @Override
    public byte pid() {
        return ProtocolInfo.SIMPLE_EVENT_PACKET;
    }

    @Override
    public void decode() {

    }

    @Override
    public void encode() {
        //TODO
    }
}
