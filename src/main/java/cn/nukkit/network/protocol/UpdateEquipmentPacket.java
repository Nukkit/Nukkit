package cn.nukkit.network.protocol;

public class UpdateEquipmentPacket extends DataPacket {

    @Override
    public byte pid() {
        return ProtocolInfo.UPDATE_EQUIPMENT_PACKET;
    }

    @Override
    public void decode() {

    }

    @Override
    public void encode() {
        //TODO
    }
}
