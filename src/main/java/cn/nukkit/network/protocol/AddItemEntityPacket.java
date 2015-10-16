package cn.nukkit.network.protocol;

import cn.nukkit.item.Item;

/**
 * author: MagicDroidX
 * Nukkit Project
 */
public class AddItemEntityPacket extends DataPacket {

    public static final byte NETWORK_ID = Info.ADD_ITEM_ENTITY_PACKET;

    public long eid;
    public Item item;
    public float x;
    public float y;
    public float z;
    public float speedX;
    public float speedY;
    public float speedZ;

    @Override
    public void decode() {

    }

    @Override
    public void encode() {
        this.putLong(this.eid);
        this.putItem(this.item);
        this.putFloat(this.x);
        this.putFloat(this.y);
        this.putFloat(this.z);
        this.putFloat(this.speedX);
        this.putFloat(this.speedY);
        this.putFloat(this.speedZ);
    }

    @Override
    public byte getNetworkId() {
        return NETWORK_ID;
    }

}
