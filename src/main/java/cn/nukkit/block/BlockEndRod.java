package cn.nukkit.block;

import cn.nukkit.Player;
import cn.nukkit.item.Item;
import cn.nukkit.item.ItemTool;
import cn.nukkit.math.AxisAlignedBB;

/**
 * Created on 2015/12/7 by xtypr.
 * Package cn.nukkit.block in project Nukkit .
 */
public class BlockEndRod extends BlockTransparent {

    public BlockEndRod() {
        this(0);
    }

    public BlockEndRod(int meta) {
        super(meta);
    }

    @Override
    public int getId() {
        return END_ROD;
    }

    @Override
    public double getHardness() {
        return 2;
    }

    @Override
    public double getResistance() {
        return 15;
    }

    @Override
    public int getLightLevel() {
        return 15;
    }

    @Override
    public int getToolType() {
        return ItemTool.TYPE_PICKAXE;
    }

    @Override
    public String getName() {
        return "End Rod";
    }

    protected AxisAlignedBB recalculateBoundingBox() {
        return new AxisAlignedBB(
                this.x + 0.4,
                this.y,
                this.z + 0.4,
                this.x + 0.6,
                this.y + 1,
                this.z + 0.6
        );
    }

    @Override
    public boolean place(Item item, Block block, Block target, int face, double fx, double fy, double fz, Player player) {
        int[] faces = {0,1,3,2,5,4};
        this.meta = faces[face];
        this.getLevel().setBlock(block, this, true, true);

        return true;
    }
}
