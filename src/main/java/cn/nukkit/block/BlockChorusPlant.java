package cn.nukkit.block;

import cn.nukkit.item.Item;
import cn.nukkit.item.ItemTool;

/**
 * Created by Leonidius20 on 02.10.17.
 */
public class BlockChorusPlant extends Block {

    public BlockChorusPlant(){
        super (0);
    }

    public BlockChorusPlant(int meta){
        super(meta);
    }

    @Override
    public String getName() {
        return "Chorus Plant";
    }

    @Override
    public int getId() {
        return 240;
    }

    @Override
    public double getHardness() {
        return 0.4;
    }

    @Override
    public double getBreakTime(Item item) {
        if (item.getId() == Item.WOODEN_AXE) return 0.35;
        if (item.getId() == Item.STONE_AXE) return 0.2;
        if (item.getId() == Item.IRON_AXE) return 0.1;
        if (item.getId() == Item.GOLD_AXE) return 0.05;
        if (item.getId() == Item.DIAMOND_AXE) return 0.1;
        else return 0.65;
    }

    @Override
    public int getToolType() {
        return ItemTool.TYPE_AXE;
    }

    @Override
    public int[][] getDrops(Item item) {
        if (item.isAxe()) {
            return new int[][]{
                    {Item.CHORUS_FRUIT, 0, 1}
            };
        } else return new int[0][0];
    }
}
