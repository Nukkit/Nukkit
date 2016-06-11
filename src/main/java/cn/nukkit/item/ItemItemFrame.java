package cn.nukkit.item;

import cn.nukkit.block.Block;

public class ItemItemFrame extends Item{

    public ItemItemFrame(){
        this(0, 1);
    }

    public ItemItemFrame(int meta){
        this(meta, 1);
    }

    public ItemItemFrame(int meta, int count){
        super(389, meta, count, "Item Frame");
        this.block = Block.get(Item.ITEM_FRAME);
    }
}
