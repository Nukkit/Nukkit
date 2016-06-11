package GameTeam;

import cn.nukkit.item.Item;
import cn.nukkit.network.protocol.DataPacket;

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
