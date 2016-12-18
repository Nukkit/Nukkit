package cn.nukkit.block;

import cn.nukkit.Player;
import cn.nukkit.item.Item;
import cn.nukkit.item.ItemTool;
import cn.nukkit.utils.BlockColor;

public class BlockPurpur extends BlockSolid {

    public BlockPurpur() {
        this(0);
    }

    public BlockPurpur(int meta) {
        super(0);
    }

    @Override
    public String getName() {
        String[] names = new String[]{
                "Purpur Block",
                "Purpur Pillar",
        };

        return names[this.meta & 0x01];
    }

    @Override
    public int getId() {
        return PURPUR_BLOCK;
    }

    @Override
    public double getHardness() {
        return 1.5;
    }

    @Override
    public double getResistance() {
        return 30;
    }

    @Override
    public int getToolType() {
        return ItemTool.TYPE_PICKAXE;
    }

    @Override
    public boolean place(Item item, Block block, Block target, int face, double fx, double fy, double fz, Player player) {
        short[] faces = new short[]{
                0,
                0,
                0b1000,
                0b1000,
                0b0100,
                0b0100
        };

        this.meta = ((this.meta & 0x03) | faces[face]);
        this.getLevel().setBlock(block, this, true, true);

        return true;
    }

    @Override
    public int[][] getDrops(Item item) {
        if (item.isPickaxe() && item.getTier() >= ItemTool.TIER_WOODEN) {
            return new int[][]{
                    {Item.PURPUR_BLOCK, this.meta & 0x01, 1}
            };
        } else {
            return new int[0][0];
        }
    }

    @Override
    public BlockColor getColor() {
        return BlockColor.PURPLE_BLOCK_COLOR; //TODO: Correct to PURPUR_BLOCK_COLOR
    }
}
