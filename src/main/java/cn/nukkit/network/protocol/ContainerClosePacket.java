package cn.nukkit.network.protocol;

/**
 * @author Nukkit Team Project
 */
public class ContainerClosePacket extends DataPacket {

    public static final byte NETWORK_ID = Info.CONTAINER_OPEN_PACKET;

    public byte windowId;

    @Override
    public void decode() {
        this.windowId = this.getByte();
    }

    @Override
    public void encode() {
        this.putByte(this.windowId);
    }

    @Override
    public byte getNetworkId() {
        return NETWORK_ID;
    }

}
