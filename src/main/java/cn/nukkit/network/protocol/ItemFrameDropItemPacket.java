package cn.nukkit.network.protocol;

import cn.nukkit.item.Item;

public class ItemFrameDropItemPacket extends DataPacket{

    public static final byte NETWORK_ID = ProtocolInfo.ITEM_FRAME_DROP_ITEM_PACKET;

    public int x;
    public int y;
    public int z;
    public Item dropItem;

    @Override
    public void decode(){
        this.z = getInt();
        this.y = getInt();
        this.x = getInt();
        this.dropItem = getSlot();
    }

    @Override
    public void encode(){

    }

    @Override
    public byte pid(){
        return NETWORK_ID;
    }
}
