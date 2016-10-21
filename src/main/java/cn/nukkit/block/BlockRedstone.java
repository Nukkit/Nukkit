package cn.nukkit.block;

import cn.nukkit.Player;
import cn.nukkit.block.redstone.RedstonePowerMode;
import cn.nukkit.block.redstone.RedstoneSource;
import cn.nukkit.item.Item;
import cn.nukkit.item.ItemTool;
import cn.nukkit.utils.BlockColor;

/**
 * @author Pub4Game
 */
public class BlockRedstone extends BlockSolid implements RedstoneSource{

    public BlockRedstone() {
        this(0);
    }

    public BlockRedstone(int meta) {
        super(0);
    }

    @Override
    public int getId() {
        return REDSTONE_BLOCK;
    }

    @Override
    public double getResistance() {
        return 10;
    }

    @Override
    public double getHardness() {
        return 5;
    }

    @Override
    public int getToolType() {
        return ItemTool.TYPE_PICKAXE;
    }

    @Override
    public String getName() {
        return "Redstone Block";
    }

    @Override
    public boolean place(Item item, Block block, Block target, int face, double fx, double fy, double fz, Player player) {
        this.getLevel().setBlock(block, this, true, true);
        return true;
    }

    @Override
    public boolean hasPhysics() {
        return true;
    }

    @Override
    public short getDirectRedstonePower(Block block, int direction, RedstonePowerMode powerMode) {
        return 0;
    }

    @Override
    public boolean hasDirectRedstonePower(Block block, int direction, RedstonePowerMode powerMode) {
        return false;
    }

    @Override
    public short getRedstonePower(Block block, RedstonePowerMode powerMode) {
        return REDSTONE_POWER_MAX;
    }

    @Override
    public boolean onBreak(Item item) {
        this.getLevel().setBlock(this, new BlockAir(), true, true);
        return true;
    }

    @Override
    public int[][] getDrops(Item item) {
        if (item.isPickaxe() && item.getTier() >= ItemTool.TIER_WOODEN) {
            return new int[][]{
                    {Item.REDSTONE_BLOCK, 0, 1}
            };
        } else {
            return new int[0][0];
        }
    }

    @Override
    public BlockColor getColor() {
        return BlockColor.REDSTONE_BLOCK_COLOR;
    }
}
