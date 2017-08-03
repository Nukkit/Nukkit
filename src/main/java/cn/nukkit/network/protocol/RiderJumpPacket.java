package cn.nukkit.network.protocol;

public class RiderJumpPacket extends DataPacket {

    public static final byte NETWORK_ID = ProtocolInfo.RIDER_JUMP_PACKET;

    public int unknown;

    @Override
    public byte pid() {
        return NETWORK_ID;
    }

    @Override
    public void decodePayload() {

    }

    @Override
    public void encodePayload() {
        this.putVarInt(this.unknown);
    }
}
