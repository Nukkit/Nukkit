package cn.nukkit.network.protocol;

/**
 * Created by CreeperFace on 30. 10. 2016.
 */
public class BossEventPacket extends DataPacket {

    public static final byte NETWORK_ID = ProtocolInfo.BOSS_EVENT_PACKET;

    public static final int TYPE_SHOW = 0;
    public static final int TYPE_UPDATE = 1;
    public static final int TYPE_HIDE = 2;

    public long bossEid;
    public int type;
    
    @Override
    public byte pid() {
        return NETWORK_ID;
    }

    @Override
    public void decode() {
    }

    @Override
    public void encode() {
        this.reset();
        this.putEntityUniqueId(this.bossEid);
        this.putUnsignedVarInt(this.type);
    }
}
