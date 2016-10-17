package cn.nukkit.network.protocol;

/**
 * Created by on 15-10-12.
 */
public class DisconnectPacket extends DataPacket {
    public static final byte NETWORK_ID = ProtocolInfo.DISCONNECT_PACKET;

    public String message;

    public DisconnectPacket() {
        super(null);
    }

    @Override
    public byte pid() {
        return NETWORK_ID;
    }

    @Override
    public void decode() {
        this.message = this.getString();
    }

    @Override
    public void encode() {
        setBuffer(new byte[3 + this.message.length()]);
        this.reset();
        this.putString(this.message);
    }


}
