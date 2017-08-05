package cn.nukkit.network.protocol;

/**
 * Created on 15-10-15.
 */
public class InteractPacket extends DataPacket {

    public static final byte NETWORK_ID = ProtocolInfo.INTERACT_PACKET;

    public static final int ACTION_RIGHT_CLICK = 1;
    public static final int ACTION_LEFT_CLICK = 2;
    public static final int ACTION_VEHICLE_EXIT = 3;
    public static final int ACTION_MOUSEOVER = 4;

    public static final int ACTION_OPEN_INVENTORY = 6;

    public int action;
    public long target;

    @Override
    public void decode() {
        this.action = this.getByte();
        this.target = this.getVarLong();
    }

    @Override
    public void encode() {
        this.reset();
        this.putByte((byte) this.action);
        this.putVarLong(this.target);
    }

    @Override
    public byte pid() {
        return NETWORK_ID;
    }

}
