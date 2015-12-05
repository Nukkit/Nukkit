package cn.nukkit.block;

import cn.nukkit.item.Item;
import cn.nukkit.item.Tool;

/**
 * author: rcsuperman
 * Nukkit Project
 */
public class Sand extends Fallable {

    public Sand() {
        this(0);
    }

    public Sand(int meta) {
        super(0);
    }

    @Override
    public int getId() {
        return SAND;
    }

    @Override
    public double getHardness() {
        return 0.5;
    }

    @Override
    public String getName() {
        return "Sand";
    }

    @Override
    public int[][] getDrops(Item item) {
            return new int[][]{new int[]{Item.SAND, this.meta & 0x03, 1}};
    }

    @Override
    public int getToolType() {
        return Tool.TYPE_SHOVEL;
    }
}
