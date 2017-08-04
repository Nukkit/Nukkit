package cn.nukkit.network.protocol;

import cn.nukkit.raknet.protocol.EncapsulatedPacket;
import cn.nukkit.utils.BinaryStream;

/**
 * author: MagicDroidX
 * Nukkit Project
 */
public abstract class DataPacket extends BinaryStream implements Cloneable {

    public final byte NETWORK_ID = 0;
    public boolean isEncoded = false;
    private int channel = 0;

    public EncapsulatedPacket encapsulatedPacket;
    public byte reliability;
    public Integer orderIndex = null;
    public Integer orderChannel = null;

    public abstract void decodePayload();

    public abstract void encodePayload();

    public void decode() {
        this.offset = 1;
        this.decodePayload();
    }

    public void encode() {
        super.reset();
        this.encodePayload();
        this.isEncoded = true;
    }

    @Override
    public void reset() {
        //this.buffer = NETWORK_ID;
        this.offset = 0;
    }

    public byte pid() {
        return NETWORK_ID;
    }

    public void setChannel(int channel) {
        this.channel = channel;
    }

    public int getChannel() {
        return channel;
    }

    public DataPacket clean() {
        this.setBuffer(null);
        this.setOffset(0);
        this.isEncoded = false;
        return this;
    }

    @Override
    public DataPacket clone() {
        try {
            return (DataPacket) super.clone();
        } catch (CloneNotSupportedException e) {
            return null;
        }
    }

    public int getEntityUniqueId() {
        return this.getVarInt();
    }

    @Override
    public void putEntityUniqueId(int eid) {
        return this.putVarLong(eid);
    }

    @Override
    public void getEntityRuntimeId() {
        return this.getUnsignedVarLong();
    }

    @Override
    public void putEntityRuntimeId(int eid) {
        return this.putUnsignedVarLong(eid);
    }

    @Override
    public void getByteRotation() {
        return (float) this.getByte();
    }

    @Override
    public void putByteRotation(float rotation) {
        this.putByte((int) (rotation / (360 / 256)));
    }
}
