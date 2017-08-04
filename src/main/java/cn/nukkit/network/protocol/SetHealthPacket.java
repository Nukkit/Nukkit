package cn.nukkit.network.protocol;

public class SetHealthPacket extends DataPacket {

    public static final byte NETWORK_ID = ProtocolInfo.SET_HEALTH_PACKET;

    public int health;

    @Override
    public byte pid() {
        return NETWORK_ID;
    }

    @Override
    public void decodePayload() {

    }

    @Override
    public void encodePayload() {
        this.putUnsignedVarInt(this.health);
    }
}
