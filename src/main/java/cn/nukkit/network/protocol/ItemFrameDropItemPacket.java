package cn.nukkit.network.protocol;

import cn.nukkit.item.Item;

public class ItemFrameDropItemPacket extends DataPacket{

    public byte NETWORK_ID = ProtocolInfo.ITEM_FRAME_DROP_ITEM_PACKET;

    public int x;
    public int y;
    public int z;
    public Item dropItem;

    @Override
    public byte pid(){
        return this.NETWORK_ID;
    }

    @Override
    public void decode(){
        z = this.getInt();
        y = this.getInt();
        x = this.getInt();
        dropItem = this.getSlot();
    }

    @Override
    public void encode(){

    }

}
