package cn.nukkit.block;

import cn.nukkit.item.Item;
import cn.nukkit.item.ItemTool;

public class BlockButtonStone extends BlockButton {

    public BlockButtonStone() {
        this(0);
    }

    public BlockButtonStone(int meta) {
        super(meta);
    }

    @Override
    public int getId() {
        return STONE_BUTTON;
    }

    @Override
    public double getHardness() {
        return 0.5;
    }

    @Override
    public double getResistance() {
        return 2.5;
    }

    @Override
    public int getToolType() {
        return ItemTool.TYPE_PICKAXE;
    }

    @Override
    public String getName() {
        return "Stone button";
    }

    @Override
    public int[][] getDrops(Item item) {
        return new int[][]{{getId(), 0, 1}};
    }
}