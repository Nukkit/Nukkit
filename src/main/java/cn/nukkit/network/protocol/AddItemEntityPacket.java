package cn.nukkit.network.protocol;

import cn.nukkit.item.Item;

/**
 * author: MagicDroidX
 * Nukkit Project
 */
public class AddItemEntityPacket extends DataPacket {
    public static final byte NETWORK_ID = ProtocolInfo.ADD_ITEM_ENTITY_PACKET;

    @Override
    public byte pid() {
        return NETWORK_ID;
    }

    public long eid;
    public Item item;
    public float x;
    public float y;
    public float z;
    public float speedX;
    public float speedY;
    public float speedZ;

    public AddItemEntityPacket() {
        super(null);
    }

    @Override
    public void decode() {

    }

    @Override
    public void encode() {
        if (this.item == null) {
            this.setBuffer(new byte[35]);
        } else {
            this.setBuffer(new byte[40]);
        }
        this.reset();
        this.putLong(this.eid);
        this.putSlot(this.item);
        this.putFloat(this.x);
        this.putFloat(this.y);
        this.putFloat(this.z);
        this.putFloat(this.speedX);
        this.putFloat(this.speedY);
        this.putFloat(this.speedZ);
    }
}
