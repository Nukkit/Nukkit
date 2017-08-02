package cn.nukkit.network.protocol;

public class GUIDataPickItemPacket extends DataPacket {

    @Override
    public byte pid() {
        return ProtocolInfo.GUI_DATA_PICK_ITEM_PACKET;
    }

    @Override
    public void encode() {
        //TODO
    }

    @Override
    public void decode() {

    }
}
