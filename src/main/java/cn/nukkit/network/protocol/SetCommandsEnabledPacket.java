package cn.nukkit.network.protocol;

public class SetCommandsEnabledPacket extends DataPacket {

    public static final byte NETWORK_ID = ProtocolInfo.SET_COMMANDS_ENABLED_PACKET;

    public boolean enabled;

    @Override
    public byte pid() {
        return NETWORK_ID;
    }

    @Override
    public void decodePayload() {

    }

    @Override
    public void encodePayload() {
        this.putBoolean(this.enabled);
    }
}
