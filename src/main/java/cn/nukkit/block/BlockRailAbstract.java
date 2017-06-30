package cn.nukkit.block;

import cn.nukkit.Player;
import cn.nukkit.item.Item;
import cn.nukkit.item.ItemTool;
import cn.nukkit.level.Level;
import cn.nukkit.level.MovingObjectPosition;
import cn.nukkit.math.AxisAlignedBB;
import cn.nukkit.math.BlockFace;
import cn.nukkit.math.Vector3;
import cn.nukkit.utils.BlockColor;
import cn.nukkit.utils.MinecartTrackLogic;

/**
 * Author: larryTheCoder
 */
public class BlockRailAbstract extends BlockFlowable {

    /**
     * Based on Private source:
     * BasketPE 1.1.2 [My Source]
     * ---
     * Author: larryTheCoder
     * DO NOT COPY!
     * ---
     * todo
     */
    protected final boolean canPowered;

    public BlockRailAbstract() {
        this(0);
    }

    public BlockRailAbstract(int meta) {
        super(meta);
        canPowered = false;
    }

    public BlockRailAbstract(boolean redstone) {
        this(redstone, 0);
    }

    public BlockRailAbstract(boolean redstone, int meta) {
        super(meta);
        canPowered = redstone;
    }

    @Override
    public String getName() {
        return "Rail";
    }

    @Override
    public int getId() {
        return RAIL;
    }

    @Override
    public double getHardness() {
        return 0.7;
    }

    @Override
    public double getResistance() {
        return 3.5;
    }

    @Override
    public int getToolType() {
        return ItemTool.TYPE_PICKAXE;
    }

    @Override
    public int onUpdate(int type) {
        if (type == Level.BLOCK_UPDATE_NORMAL) {
            if (down().isTransparent()) {
                getLevel().useBreakOn(this);
                return Level.BLOCK_UPDATE_NORMAL;
            }
            
        }
        return 0;
    }

    @Override
    public BlockColor getColor() {
        return BlockColor.AIR_BLOCK_COLOR;
    }

    public static final boolean isRail(Level world, int i, int j, int k) {
        return isRail(world.getBlock(new Vector3(i, j, k)));
    }

    @Override
    public MovingObjectPosition calculateIntercept(Vector3 pos1, Vector3 pos2) {
        updateShape(level, x, y, z);
        return super.calculateIntercept(pos1, pos2);
    }
    
    @Override
    public boolean place(Item item, Block block, Block target, BlockFace face, double fx, double fy, double fz, Player player) {
        prepareBlock(level, (int) fx, (int) fy, (int) fz, true);
        if (canPowered) {
            doPhysics(level, (int) fx, (int) fy, (int) fz, this);
        }
        return true;
    }

    public void doPhysics(Level world, int fx, int fy, int fz, Block block) {
        // This should override to Block
        int meta1 = world.getBlock(new Vector3(fx, fy, fz)).getDamage();
        int meta2 = meta1;

        if (canPowered) {
            meta2 = meta1 & 7;
        }

        boolean flag = false;
        if (!canPlaced(fx, fy - 1, fz)) {
            flag = true;
        }

        if (meta2 == 2 && !canPlaced(fx + 1, fy, fz)) {
            flag = true;
        }

        if (meta2 == 3 && !canPlaced(fx - 1, fy, fz)) {
            flag = true;
        }

        if (meta2 == 4 && !canPlaced(fx, fy, fz - 1)) {
            flag = true;
        }

        if (meta2 == 5 && !canPlaced(fx, fy, fz + 1)) {
            flag = true;
        }

        if (flag) {
            level.setBlock(new Vector3(fx, fy, fz), Block.get(AIR));
        } else {
            putBlock(level, fx, fy, fz, meta1, meta2, block);
        }
    }

    public boolean canPlaced(double dx, double dy, double dz) {
        Block block = level.getBlock(new Vector3(dx, dy, dz));
        int metaData = block.getDamage();

        return block.isSolid() && block.canPassThrough() ? true
                // These blocks might have some little power
                : (block instanceof BlockStairs ? (metaData & 4) == 4
                : (block instanceof BlockSlab ? (metaData & 8) == 8
                : (block instanceof BlockHopper ? true
                : (block instanceof BlockSnow ? (metaData & 7) == 7
                : false))));
    }

    protected void prepareBlock(Level world, int i, int j, int k, boolean flag) {
        (new MinecartTrackLogic(this, world, i, j, k))
                .doChange(world.isBlockIndirectlyGettingPowered(new Vector3(i, j, k)) != 0, flag);
    }

    public void putBlock(Level level, double fx, double fy, double fz, int meta1, int meta2, Block block){}

    public boolean redstonePowered() {
        return canPowered;
    }

    public static boolean isRail(Block block) {
        switch (block.getId()) {
            case RAIL:
            case POWERED_RAIL:
            case ACTIVATOR_RAIL:
            case DETECTOR_RAIL:
                return true;
            default:
                return false;
        }
    }

    private void updateShape(Level level, double x, double y, double z) {
        // This should be overriden from Block
        // Also can adjust the block rounding box (aka AntiCheat)
        int l = level.getBlock(new Vector3(x, y, z)).getDamage();
        
        AxisAlignedBB bb = getBoundingBox();
        if (l >= 2 && l <= 5) {
            bb.setBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.625F, 1.0F);
        } else {
            bb.setBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.125F, 1.0F);
        }
        boundingBox = bb;
    }

}
