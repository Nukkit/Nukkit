package cn.nukkit.network.protocol;

/**
 * @author Nukkit Project Team
 */
public class SetEntityMotionPacket extends DataPacket {
    public static final byte NETWORK_ID = Info.SET_ENTITY_MOTION_PACKET;

    // eid, motX, motY, motZ
    public double[][] entities = new double[0][];

    @Override
    public byte getNetworkId() {
        return NETWORK_ID;
    }

    @Override
    public void clean() {
        entities = new double[0][];
        super.clean();
    }

    @Override
    public void decode() {

    }

    @Override
    public void encode() {
        putInt(entities.length);
        for (double[] d : entities) {
            putLong((long) d[0]);
            putFloat((float) d[1]);
            putFloat((float) d[2]);
            putFloat((float) d[3]);
        }
    }

}
