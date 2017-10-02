package cn.nukkit.block;

import cn.nukkit.Player;
import cn.nukkit.item.Item;
import cn.nukkit.item.ItemTool;

/**
 * Created by Leonidius20 on 02.10.17.
 */
public class BlockChorusFlower extends Block {
    public BlockChorusFlower() {
        super (0);
    }

    public BlockChorusFlower(int meta) {
        super (meta);
    }

    @Override
    public String getName() {
        return "Chorus Flower";
    }

    @Override
    public int getId() {
        return 200;
    }

    @Override
    public boolean place(Item item, Block block, Block target, int face, double fx, double fy, double fz, Player player) {
        int down = this.getSide(0).getId();
        if (down == Block.END_STONE || down == Block.CHORUS_PLANT) {
            this.getLevel().setBlock(block, this, true);
            return true;
        }
        return false;
    }

    @Override
    public double getHardness() {
        return 0.4;
    }

    @Override
    public int getToolType() {
        return ItemTool.TYPE_AXE;
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
    public int[][] getDrops(Item item) {
        return new int[][] {
                {Item.CHORUS_FLOWER, 0, 1}
        };
    }
}
