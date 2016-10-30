package cn.nukkit.network.protocol;

/**
 * @author Nukkit Project Team
 */
public class SetDifficultyPacket extends DataPacket {

    public static final byte NETWORK_ID = ProtocolInfo.SET_DIFFICULTY_PACKET;

    public int difficulty;

    public SetDifficultyPacket() {
        super(5);
    }

    @Override
    public void decode() {
        difficulty = (int) getUnsignedVarInt();
    }

    @Override
    public void encode() {
        reset();
        putUnsignedVarInt(difficulty);
    }

    @Override
    public byte pid() {
        return NETWORK_ID;
    }

}
