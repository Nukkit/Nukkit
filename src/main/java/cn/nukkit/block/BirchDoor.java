package cn.nukkit.block;

import cn.nukkit.item.Item;
import cn.nukkit.item.Tool;

public class BirchDoor extends Door{
    public BirchDoor(){ this(0); }

    public BirchDoor(int meta) {
        super(Block.BIRCH_DOOR_BLOCK, meta);
    }

    public String getName(){
        return "Birch Door Block";
    }

    public boolean canBeActivated(){
        return true;
    }

    public double getHardness(){
        return 3;
    }

    public int getToolType(){
        return Tool.TYPE_AXE;
    }

    public int[][] getDrops(Item item){
        return new int[][]{
                {Item.BIRCH_DOOR, 0, 1}
        };
    }
}