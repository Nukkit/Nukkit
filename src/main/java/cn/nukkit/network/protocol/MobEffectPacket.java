package cn.nukkit.network.protocol;

/**
 * author: MagicDroidX
 * Nukkit Project
 */
public class MobEffectPacket extends DataPacket {

    public static final byte NETWORK_ID = Info.MOB_EFFECT_PACKET;

    public static final byte EVENT_ADD = 1;
    public static final byte EVENT_MODIFY = 2;
    public static final byte EVENT_REMOVE = 3;

    public long eid;
    public byte eventId;
    public byte effectId;
    public byte amplifier;
    public boolean particles = true;
    public int duration;

    @Override
    public void decode() {

    }

    @Override
    public void encode() {
        putLong(eid);
        putByte(eventId);
        putByte(effectId);
        putByte(amplifier);
        putByte(particles ? 1 : 0);
        putInt(duration);
    }

    @Override
    public byte getNetworkId() {
        return NETWORK_ID;
    }

}
