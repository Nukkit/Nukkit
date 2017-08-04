package cn.nukkit.network.protocol;

/**
 * Created by on 15-10-12.
 */
public class DisconnectPacket extends DataPacket {
    public static final byte NETWORK_ID = ProtocolInfo.DISCONNECT_PACKET;

    public boolean hideDisconnectionScreen = false;
    public String message;

    @Override
    public byte pid() {
        return NETWORK_ID;
    }

    @Override
    public void decodePayload() {
        this.hideDisconnectionScreen = this.getBoolean();
        this.message = this.getString();
    }

    @Override
    public void encodePayload() {
        this.putBoolean(this.hideDisconnectionScreen);
        if (!this.hideDisconnectionScreen) {
            this.putString(this.message);
        }
    }


}
