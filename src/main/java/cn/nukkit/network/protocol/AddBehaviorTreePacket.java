package cn.nukkit.network.protocol;

public class AddBehaviorTreePacket extends DataPacket {

    @Override
    public byte pid() {
        return ProtocolInfo.ADD_BEHAVIOR_TREE_PACKET;
    }

    @Override
    public void decode() {

    }

    @Override
    public void encode() {
        //TODO
    }
}
