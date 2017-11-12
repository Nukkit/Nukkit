package cn.nukkit.network.protocol;

public class GUIDataPickItemPacket extends DataPacket {

    public int hotbarSlot;

    @Override
    public byte pid(PlayerProtocol protocol) {
        return protocol.equals(PlayerProtocol.PLAYER_PROTOCOL_113) ?
                0 :
                ProtocolInfo.GUI_DATA_PICK_ITEM_PACKET;
    }

    @Override
    public void encode(PlayerProtocol protocol) {
        this.reset(protocol);
        this.putLInt(this.hotbarSlot);
    }

    @Override
    public void decode(PlayerProtocol protocol) {
        this.hotbarSlot = this.getLInt();
    }
}
