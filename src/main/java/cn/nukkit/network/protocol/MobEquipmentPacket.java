package cn.nukkit.network.protocol;

import cn.nukkit.item.Item;

/**
 * @author Nukkit Project Team
 */
public class MobEquipmentPacket extends DataPacket {

    public static final byte NETWORK_ID = Info.MOB_EQUIPMENT_PACKET;

    public long eid;
    public Item item;
    public byte slot;
    public byte selectedSlot;

    @Override
    public void decode() {
        this.eid  = this.getLong();
        this.item = this.getItem();
        this.slot = this.getByte();
        this.selectedSlot = this.getByte();
    }

    @Override
    public void encode() {
        this.putLong(this.eid);
        this.putItem(this.item);
        this.putByte(this.slot);
        this.putByte(this.selectedSlot);
    }

    @Override
    public byte getNetworkId() {
        return NETWORK_ID;
    }

}
