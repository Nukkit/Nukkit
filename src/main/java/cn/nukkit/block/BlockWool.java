package cn.nukkit.block;

import cn.nukkit.item.ItemDye;
import cn.nukkit.item.ItemTool;
import cn.nukkit.utils.BlockColor;

/**
 * Created on 2015/12/2 by xtypr.
 * Package cn.nukkit.block in project Nukkit .
 */
public class BlockWool extends BlockSolid {
    public static final int WHITE = 0;
    public static final int ORANGE = 1;
    public static final int MAGENTA = 2;
    public static final int LIGHT_BLUE = 3;
    public static final int YELLOW = 4;

    public BlockWool() {
        this(0);
    }

    public BlockWool(int meta) {
        super(meta);
    }

    @Override
    public String getName() {
        String[] names = new String[]{
                "White Wool",
                "Orange Wool",
                "Magenta Wool",
                "Light Blue Wool",
        }
        @Override
    public int getId() {
        return WOOL;
    }

    @Override
    public int getToolType() {
        return ItemTool.TYPE_SHEARS;
    }

    @Override
    public double getHardness() {
        return 0.8;
    }

    @Override
    public double getResistance() {
        return 4;
    }

    @Override
    public BlockColor getColor() {
        return BlockColor.getDyeColor(meta);
    }
}
