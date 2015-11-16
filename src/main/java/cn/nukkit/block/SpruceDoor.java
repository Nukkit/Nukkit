package cn.nukkit.block;

import cn.nukkit.item.Item;
import cn.nukkit.item.Tool;

public class SpruceDoor extends Door{

    public SpruceDoor(){ this(0); }

    public SpruceDoor(int meta) {
        super(Block.SPRUCE_DOOR_BLOCK, meta);
    }

    @Override
    public String getName(){
        return "Spruce Door Block";
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
                {Item.SPRUCE_DOOR, 0, 1}
        };
    }
}