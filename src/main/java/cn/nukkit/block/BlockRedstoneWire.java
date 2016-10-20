package cn.nukkit.block;

import cn.nukkit.Player;
import cn.nukkit.item.Item;
import cn.nukkit.level.Level;
import cn.nukkit.math.Vector3;
import cn.nukkit.utils.BlockColor;

/**
 * author: Angelic47
 * Nukkit Project
 */
public class BlockRedstoneWire extends BlockFlowable {

    public BlockRedstoneWire() {
        this(0);
    }

    public BlockRedstoneWire(int meta) {
        super(meta);
    }

    @Override
    public String getName() {
        return "Redstone Wire";
    }

    @Override
    public int getId() {
        return REDSTONE_WIRE;
    }

    @Override
    public int onUpdate(int type) {
        if (type == Level.BLOCK_UPDATE_NORMAL) {
            if (this.getSide(Vector3.SIDE_DOWN).isTransparent()) {
                this.getLevel().useBreakOn(this);
                return Level.BLOCK_UPDATE_NORMAL;
            }
        }
        return 0;
    }

    @Override
    public boolean place(Item item, Block block, Block target, int face, double fx, double fy, double fz, Player player) {
        if (this.getSide(Vector3.SIDE_DOWN).isTransparent()) {
            return false;
        } else {
            block.getLevel().setBlock(block, this, true, true);
            return true;
        }
    }

    @Override
    public boolean onBreak(Item item) {
        this.getLevel().setBlock(this, new BlockAir(), true, false);
        return true;
    }

    @Override
    public int[][] getDrops(Item item) {
        return new int[][]{
                {Item.REDSTONE, 0, 1}
        };
    }

    @Override
    public BlockColor getColor() {
        return BlockColor.AIR_BLOCK_COLOR;
    }
}
