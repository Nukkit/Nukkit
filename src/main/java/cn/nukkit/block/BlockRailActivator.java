package cn.nukkit.block;

import cn.nukkit.math.BlockFace;

/**
 * @author Nukkit Project Team
 */
public class BlockRailActivator extends BlockRail {

    public BlockRailActivator(int meta) {
        super(meta);
    }

    public BlockRailActivator() {
        this(0);
        setIsDiode(true); // can power
        setComplexDiode(true);
    }

    @Override
    public String getName() {
        return "Activator Rail";
    }

    @Override
    public int getId() {
        return ACTIVATOR_RAIL;
    }
}
