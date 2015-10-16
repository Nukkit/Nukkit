package cn.nukkit.network.protocol;

import cn.nukkit.network.DataStream;
import cn.nukkit.raknet.protocol.EncapsulatedPacket;

/**
 * @author Nukkit Project Team
 */
public abstract class DataPacket extends DataStream {

    // TODO This field encapsulatedPacket may deprecated at 0.12.
    private EncapsulatedPacket encapsulatedPacket;
    private int channel;

    @Override
    public byte[] toByteArray() {
        if (getCursor() == 0) {
            putByte(getNetworkId());
            encode();
        }
        return super.toByteArray();
    }

    public void clean() {
        setCursor(0);
        setOffset(0);
    }

    public int getChannel() {
        return channel;
    }

    public void setChannel(int channel) {
        this.channel = channel;
    }

    public int getSize() {
        return getCursor();
    }

    public abstract void encode();

    public abstract void decode();

    public abstract byte getNetworkId();

    @Override
    public DataPacket clone() {
        try {
            return ((DataPacket) super.clone());
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return null;
    }

    public EncapsulatedPacket getEncapsulatedPacket() {
        return encapsulatedPacket;
    }

    public void setEncapsulatedPacket(EncapsulatedPacket encapsulatedPacket) {
        this.encapsulatedPacket = encapsulatedPacket;
    }

}
