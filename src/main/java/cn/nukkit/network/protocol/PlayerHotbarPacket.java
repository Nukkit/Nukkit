package cn.nukkit.network.protocol;

public class PlayerHotbarPacket extends DataPacket {

    public int selectedSlot;
    public int[] data = new int[0];

    @Override
    public byte pid() {
        return ProtocolInfo.PLAYER_HOTBAR_PACKET;
    }

    @Override
    public void decode() {
        this.selectedSlot = this.getByte();
        int count = (int) this.getUnsignedVarInt();
        this.data = new int[count];
        for (int s = 0; s < count && !this.feof(); ++s) {
            this.data[s] = this.getVarInt();
        }
    }

    @Override
    public void encode() {
        this.putByte((byte) selectedSlot);
        this.putUnsignedVarInt(this.data.length);
        for (int slot : this.data) {
            this.putVarInt(slot);
        }
    }
}
