package cn.nukkit.network.protocol;

/**
 * @author Nukkit Project Team
 */
public class DisconnectPacket extends DataPacket {

    public static final byte NETWORK_ID = Info.DISCONNECT_PACKET;

    public String message;

    @Override
    public byte getNetworkId() {
        return NETWORK_ID;
    }

    @Override
    public void decode() {
        this.message = this.getString();
    }

    @Override
    public void encode() {
        this.putString(this.message);
    }

}
