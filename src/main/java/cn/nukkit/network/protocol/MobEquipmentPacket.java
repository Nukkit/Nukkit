package cn.nukkit.network.protocol;

import cn.nukkit.item.Item;

/**
 * author: MagicDroidX
 * Nukkit Project
 */
public class MobEquipmentPacket extends DataPacket {
    public static final byte NETWORK_ID = ProtocolInfo.MOB_EQUIPMENT_PACKET;

    @Override
    public byte pid() {
        return NETWORK_ID;
    }

    public long eid;
    public Item item;
    public int slot;
    public int selectedSlot;

    public MobEquipmentPacket() {
        super(null);
    }

    @Override
    public void decode() {
        this.eid = this.getVarLong(); //EntityRuntimeID
        this.item = this.getSlot();
        this.slot = this.getByte();
        this.selectedSlot = this.getByte();
    }

    @Override
    public void encode() {
        if (this.item == null) {
            setBuffer(new byte[13]);
        } else {
            setBuffer(new byte[18]);
        }
        this.reset();
        this.putVarLong(this.eid); //EntityRuntimeID
        this.putSlot(this.item);
        this.putByte((byte) this.slot);
        this.putByte((byte) this.selectedSlot);
    }
}
