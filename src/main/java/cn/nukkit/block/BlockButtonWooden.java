package cn.nukkit.block;

import cn.nukkit.item.Item;
import cn.nukkit.item.ItemTool;

public class BlockButtonWooden extends BlockButton {
    public BlockButtonWooden() {
        this(0);
    }

    public BlockButtonWooden(int meta) {
        super(meta);
    }

    @Override
    public int getId() {
        return WOODEN_BUTTON;
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
        return ItemTool.TYPE_AXE;
    }

    @Override
    public String getName() {
        return "Wooden button";
    }

    @Override
    public int[][] getDrops(Item item) {
        return new int[][]{{getId(), 0, 1}};
    }
}