package cn.nukkit.block;

import cn.nukkit.Player;
import cn.nukkit.item.Item;
import cn.nukkit.item.ItemTool;

/**
 * author: Angelic47 Nukkit Project
 */
public class BlockPurpurPillar extends BlockSolid {

    public BlockPurpurPillar() {
        this(0);
    }

    public BlockPurpurPillar(int meta) {
        super(meta);
    }

    @Override
    public int getId() {
        return PURPUR_PILLAR;
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
        return "Purpur Pillar";
    }

    @Override
    public int[][] getDrops(Item item) {
        return new int[][]{{this.getId(), 0, 1}};
    }

}
