package cn.nukkit.network.protocol;

import cn.nukkit.item.Item;

/**
 * @author Nukkit Project Team
 */
public class MobArmorEquipmentPacket extends DataPacket {
    
    public static final byte NETWORK_ID = Info.MOB_ARMOR_EQUIPMENT_PACKET;

    public long   eid;
    public Item[] items = new Item[4];

    @Override
    public void decode() {
        eid = getLong();
        items    = new Item[4];
        items[0] = getItem();
        items[1] = getItem();
        items[2] = getItem();
        items[3] = getItem();
    }

    @Override
    public void encode() {
        putLong(eid);
        putItem(items[0]);
        putItem(items[1]);
        putItem(items[2]);
        putItem(items[3]);
    }

    @Override
    public byte getNetworkId() {
        return NETWORK_ID;
    }
    
}
