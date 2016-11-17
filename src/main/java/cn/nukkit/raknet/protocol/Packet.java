package cn.nukkit.raknet.protocol;

import cn.nukkit.utils.BinaryStream;
import java.net.InetSocketAddress;

/**
 * author: MagicDroidX
 * Nukkit Project
 */
public abstract class Packet extends BinaryStream implements Cloneable {

    public Long sendTime;

    public Packet(){}

    public Packet(byte[] buffer) {
        super(buffer);
    }

    public Packet(int buffer) {
        super(buffer);
    }

    public abstract byte getID();

    @Override
    public int getBlockSize() {
        return 32;
    }

    protected byte[] getAll() {
        return this.get();
    }

    protected InetSocketAddress getAddress() {
        int version = this.getByte();
        if (version == 4) {
            String addr = ((~this.getByte()) & 0xff) + "." + ((~this.getByte()) & 0xff) + "." + ((~this.getByte()) & 0xff) + "." + ((~this.getByte()) & 0xff);
            int port = this.getShort();
            return new InetSocketAddress(addr, port);
        } else {
            //todo IPV6 SUPPORT
            return null;
        }
    }

    protected void putAddress(String addr, int port) {
        this.putAddress(addr, port, (byte) 4);
    }

    protected void putAddress(String addr, int port, byte version) {
        this.putByte(version);
        if (version == 4) {
            for (String b : addr.split("\\.")) {
                this.putByte((byte) ((~Integer.valueOf(b)) & 0xff));
            }
            this.putShort(port);
        } else {
            //todo ipv6
        }
    }

    protected void putAddress(InetSocketAddress address) {
        this.putAddress(address.getHostString(), address.getPort());
    }

    public void encode() {
        if (offset != 0) {
            setBuffer(new byte[1]);
        }
        put(getID());
    }

    public void decode() {
        this.offset = 1;
    }

    public Packet clean() {
        reset();
        this.sendTime = null;
        return this;
    }

    @Override
    public Packet clone() throws CloneNotSupportedException {
        getRawBuffer();
        Packet packet = (Packet) super.clone();
        return packet;
    }

    /**
     * A factory to create new packet instances
     */
    public interface PacketFactory {
        /**
         * Creates the packet
         */
        Packet create();
    }
}
