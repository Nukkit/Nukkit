package cn.nukkit.network.protocol;

import cn.nukkit.math.Vector3;

/**
 * author: MagicDroidX
 * Nukkit Project
 */
public class ExplodePacket extends DataPacket {

    public static final byte NETWORK_ID = ProtocolInfo.EXPLODE_PACKET;

    public float x;
    public float y;
    public float z;
    public float radius;
    public Vector3[] records = new Vector3[0];

    public ExplodePacket() {
        super(null);
    }

    @Override
    public byte pid() {
        return NETWORK_ID;
    }

    @Override
    public DataPacket clean() {
        records = new Vector3[0];
        return super.clean();
    }

    @Override
    public void decode() {

    }

    @Override
    public void encode() {
        setBuffer(new byte[21 + records.length * 3]);
        this.reset();
        this.putFloat(this.x);
        this.putFloat(this.y);
        this.putFloat(this.z);
        this.putFloat(this.radius);
        this.putInt(this.records.length);
        for (Vector3 record : records) {
            this.putByte((byte) record.x);
            this.putByte((byte) record.y);
            this.putByte((byte) record.z);
        }
    }

}
