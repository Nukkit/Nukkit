package cn.nukkit.network.protocol;

public class AddHangingEntityPacket extends DataPacket {
    public static final byte NETWORK_ID = ProtocolInfo.ADD_HANGING_ENTITY_PACKET;

    @Override
    public byte pid() {
        return NETWORK_ID;
    }

    public long entityUniqueId;
    public long entityRuntimeId;
    public int x;
    public int y;
    public int z;
    public int unknown;

    @Override
    public void decode() {

    }

    @Override
    public void encode() {
        this.reset();
        this.putVarLong(this.entityUniqueId);
        this.putEntityId(this.entityRuntimeId);
        this.putBlockCoords(x, y, z);
        this.putVarInt(unknown);
    }
}
