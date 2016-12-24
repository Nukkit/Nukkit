package cn.nukkit.block;

import cn.nukkit.Player;
import cn.nukkit.item.Item;
import cn.nukkit.item.ItemTool;

/**
 * author: Angelic47 Nukkit Project
 */
public class BlockPurpurSlab extends BlockSlab {

    public BlockPurpurSlab() {
        this(0);
    }

    public BlockPurpurSlab(int meta) {
        super(meta, DOUBLE_PURPUR_SLAB);
    }

    @Override
    public int getId() {
        return PURPUR_SLAB;
    }

    @Override
    public double getHardness() {
        return 3;
    }

    @Override
    public double getResistance() {
        return 15;
    }

    @Override
    public int getToolType() {
        return ItemTool.TYPE_PICKAXE;
    }

    @Override
    public String getName() {
        return "Purpur Slab";
    }

    @Override
    public int[][] getDrops(Item item) {
        return new int[][]{{this.getId(), 0, 1}};
    }

}
