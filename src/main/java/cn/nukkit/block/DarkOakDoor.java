package cn.nukkit.block;

import cn.nukkit.item.Item;
import cn.nukkit.item.Tool;

class DarkOakDoor extends Door{
    public DarkOakDoor(){ this(0); }

    public DarkOakDoor(int meta) {
        super(Block.ACACIA_DOOR_BLOCK, meta);
    }

    @Override
    public String getName(){
        return "Dark Oak Door Block";
    }

    @Override
    public boolean canBeActivated(){
        return true;
    }

    @Override
    public double getHardness(){
        return 3;
    }

    @Override
    public int getToolType(){
        return Tool.TYPE_AXE;
    }

    @Override
    public int[][] getDrops(Item item){
        return new int[][]{
                {Item.DARK_OAK_DOOR, 0, 1}
        };
    }
}