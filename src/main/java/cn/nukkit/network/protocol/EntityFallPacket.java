package cn.nukkit.network.protocol;

public class EntityFallPacket extends DataPacket {
    public static final byte NETWORK_ID = ProtocolInfo.ENTITY_FALL_PACKET;

    public long eid;
    public float fallDistance;
    public boolean unknown;

    @Override
    public void decodePayload() {
        this.eid = this.getVarLong();
        this.fallDistance = this.getLFloat();
        this.unknown = this.getBoolean();
    }

    @Override
    public void encodePayload() {

    }

    @Override
    public byte pid() {
        return NETWORK_ID;
    }
}
