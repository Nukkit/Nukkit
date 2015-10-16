package cn.nukkit.network.protocol;

/**
 * @author Nukkit Project Team
 */
public class MoveEntityPacket extends DataPacket {

    public static final byte NETWORK_ID = Info.MOVE_ENTITY_PACKET;

    // eid, x, y, z, yaw, pitch
    public double[][] entities = new double[0][];

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
            putFloat((float) d[4]);
            putFloat((float) d[5]);
            putFloat((float) d[6]);
        }
    }

    @Override
    public byte getNetworkId() {
        return NETWORK_ID;
    }

}
