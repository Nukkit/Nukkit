package cn.nukkit.network.protocol;

/**
 * @author Nukkit Project Team
 */
public class ContainerSetDataPacket extends DataPacket {

    public static final byte NETWORK_ID = Info.CONTAINER_SET_DATA_PACKET;

    public byte  windowId;
    public short property;
    public short value;

    @Override
    public void decode() {
    }

    @Override
    public void encode() {
        putByte (windowId);
        putShort(property);
        putShort(value   );
    }

    @Override
    public byte getNetworkId() {
        return NETWORK_ID;
    }

}
