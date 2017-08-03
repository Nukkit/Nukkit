package cn.nukkit.network.protocol;

/**
 * @author Nukkit Project Team
 */
public class SetDifficultyPacket extends DataPacket {

    public static final byte NETWORK_ID = ProtocolInfo.SET_DIFFICULTY_PACKET;

    public int difficulty;

    @Override
    public void decodePayload() {
        this.difficulty = (int) this.getUnsignedVarInt();
    }

    @Override
    public void encodePayload() {
        this.putUnsignedVarInt(this.difficulty);
    }

    @Override
    public byte pid() {
        return NETWORK_ID;
    }

}
