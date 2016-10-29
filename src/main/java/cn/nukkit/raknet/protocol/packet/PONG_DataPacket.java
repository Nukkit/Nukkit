package cn.nukkit.raknet.protocol.packet;

import cn.nukkit.raknet.protocol.Packet;

/**
 * author: MagicDroidX
 * Nukkit Project
 */
public class PONG_DataPacket extends Packet {
    public static final byte ID = (byte) 0x03;

    public PONG_DataPacket() {
        super(9);
    }

    public PONG_DataPacket(byte[] buffer) {
        super(buffer);
    }

    @Override
    public byte getID() {
        return ID;
    }

    public long pingID;

    @Override
    public void encode() {
        super.encode();
        this.putLong(this.pingID);
    }

    @Override
    public void decode() {
        super.decode();
        this.pingID = this.getLong();
    }

    public static final class Factory implements Packet.PacketFactory {

        @Override
        public Packet create() {
            return new PONG_DataPacket();
        }

    }
}
