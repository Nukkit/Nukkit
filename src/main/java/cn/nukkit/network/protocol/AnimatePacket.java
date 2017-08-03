package cn.nukkit.network.protocol;

/**
 * @author Nukkit Project Team
 */
public class AnimatePacket extends DataPacket {

    public static final byte NETWORK_ID = ProtocolInfo.ANIMATE_PACKET;

    public long eid;
    public int action;
    public float unknown;

    @Override
    public void decodePayload() {
        this.action = (int) this.getUnsignedVarInt();
        this.eid = getVarLong();
        if ((this.action & 0x80) != 0) {
            this.unknown = this.getLFloat();
        }
    }

    @Override
    public void encodePayload() {
        this.putUnsignedVarInt(this.action);
        this.putVarLong(this.eid);
        if ((this.action & 0x80) != 0) {
            this.putLFloat(this.unknown);
        }
    }

    @Override
    public byte pid() {
        return NETWORK_ID;
    }

}
