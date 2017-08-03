package cn.nukkit.network.protocol;

import cn.nukkit.network.protocol.types.WindowType;

public class UpdateTradePacket extends DataPacket {

    public static final byte NETWORK_ID = ProtocolInfo.UPDATE_TRADE_PACKET;

    public byte windowId;
    public byte windowType = WindowType.TRADING;
    public int varint1;
    public int varint2;
    public boolean isWilling;
    public long traderEid;
    public long playerEid;
    public String displayName;
    public byte[] offers;

    @Override
    public void decodePayload() {
        this.windowId = this.getByte();
        this.windowType = this.getByte();
        this.varint1 = this.getVarInt();
        this.varint2 = this.getVarInt();
        this.isWilling = this.getBoolean();
        this.traderEid = this.getEntityUniqueId();
        this.playerEid = this.getEntityUniqueId();
        this.displayName = this.getString();
        // this.offers = this.getRemaining();
    }

    @Override
    public void encodePayload() {
        this.putByte(windowId);
        this.putByte(windowType);
        this.putVarInt(varint1);
        this.putVarInt(varint2);
        this.putBoolean(isWilling);
        this.putVarLong(traderEid);
        this.putVarLong(playerEid);
        this.putString(displayName);
        this.put(this.offers);
    }

}
