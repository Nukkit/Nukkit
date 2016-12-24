package cn.nukkit.network.protocol;

/**
 * author: MagicDroidX
 * Nukkit Project
 */
public class ShowCreditsPacket extends DataPacket {
    public static final byte NETWORK_ID = ProtocolInfo.SHOW_CREDITS_PACKET;

    public long eid;
    public int credit;

    @Override
    public byte pid() {
        return NETWORK_ID;
    }

    @Override
    public void decode() {

    }

    @Override
    public void encode() {
        this.reset();
        this.putVarLong(this.eid);
        this.putVarInt(this.credit);
    }

}
