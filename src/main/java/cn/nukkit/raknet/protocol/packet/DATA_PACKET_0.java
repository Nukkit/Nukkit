package cn.nukkit.raknet.protocol.packet;

import cn.nukkit.raknet.protocol.DataPacket;
import cn.nukkit.raknet.protocol.Packet;

/**
 * author: MagicDroidX
 * Nukkit Project
 */
public class DATA_PACKET_0 extends DataPacket {
    public static final byte ID = (byte) 0x80;

    public DATA_PACKET_0() {
        super(1450);
    }

    @Override
    public byte getID() {
        return ID;
    }

    @Override
    public byte[] getBuffer() {
        return super.getBuffer();
    }

    public static final class Factory implements Packet.PacketFactory {

        @Override
        public Packet create() {
            return new DATA_PACKET_0();
        }

    }

}
