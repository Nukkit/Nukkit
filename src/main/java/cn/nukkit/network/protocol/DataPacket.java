package cn.nukkit.network.protocol;

import cn.nukkit.raknet.protocol.EncapsulatedPacket;
import cn.nukkit.utils.BinaryStream;

/**
 * author: MagicDroidX
 * Nukkit Project
 */
public abstract class DataPacket extends BinaryStream implements Cloneable {

    public boolean isEncoded = false;
    private int channel = 0;

    public EncapsulatedPacket encapsulatedPacket;
    public byte reliability;
    public Integer orderIndex = null;
    public Integer orderChannel = null;

    public DataPacket() {}

    public DataPacket(int buffer) {
        super(buffer);
    }

    public DataPacket(byte[] buffer) {
        super(buffer);
    }

    public abstract byte pid();

    public abstract void decode();

    public abstract void encode();

    @Override
    public int getBlockSize() {
        return 35;
    }

    @Override
    public void reset() {
        super.reset();
        this.putByte(this.pid());
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
            getRawBuffer();
            return (DataPacket) super.clone();
        } catch (CloneNotSupportedException e) {
            return null;
        }
    }

    public static byte[] join(DataPacket[] packets) {
        int size = 0;
        for (DataPacket packet : packets) {
            if (!packet.isEncoded) {
                packet.encode();
                packet.isEncoded = true;
            }
            size += 4 + packet.getCount();
        }
        byte[] data = new byte[size];
        byte[] rawBuf;
        int i = 0;
        for (DataPacket packet : packets) {
            rawBuf = packet.getRawBuffer();
            int len = packet.getCount();
            data[i] = (byte) ((len >>> 24) & 0xFF);
            data[i + 1] = (byte) ((len >>> 16) & 0xFF);
            data[i + 2] = (byte) ((len >>> 8) & 0xFF);
            data[i + 3] = (byte) ((len) & 0xFF);
            System.arraycopy(rawBuf, 0, data, i + 4, len);
            i += 4 + len;
        }
        return data;
    }
}
