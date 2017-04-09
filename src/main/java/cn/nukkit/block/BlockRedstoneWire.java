package cn.nukkit.block;

import cn.nukkit.Player;
import cn.nukkit.item.Item;
import cn.nukkit.level.Level;
import cn.nukkit.math.BlockFace;
import cn.nukkit.math.BlockFace.Plane;
import cn.nukkit.math.Vector3;
import cn.nukkit.utils.BlockColor;
import com.google.common.collect.Lists;

import java.util.EnumSet;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * author: Angelic47
 * Nukkit Project
 */
public class BlockRedstoneWire extends BlockFlowable {

    private boolean canProvidePower = true;
    private final Set<Vector3> blocksNeedingUpdate = new HashSet<>();

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
    public boolean place(Item item, Block block, Block target, BlockFace face, double fx, double fy, double fz, Player player) {
        this.getLevel().setBlock(block, this, true, false);
        this.updateSurroundingRedstone();
        Vector3 pos = getLocation();

        for (BlockFace blockFace : BlockFace.values()) {
            this.level.updateAround(pos.getSide(blockFace));
        }

        for (BlockFace blockFace : Plane.HORIZONTAL) {
            Vector3 v = pos.getSide(blockFace);

            if (this.level.getBlock(v).isSolid()) {
                this.updateAround(v.up());
            } else {
                this.updateAround(v.down());
            }
        }
        return true;
    }

    private void updateAround(Vector3 pos) {
        if (this.level.getBlock(pos).getId() == Block.REDSTONE_WIRE) {
            this.level.updateAround(pos);

            for (BlockFace face : BlockFace.values()) {
                this.level.updateAround(pos.getSide(face));
            }
        }
    }

    private void updateSurroundingRedstone() {
        this.calculateCurrentChanges();
        List<Vector3> list = Lists.newArrayList(this.blocksNeedingUpdate);
        this.blocksNeedingUpdate.clear();

        for (Vector3 pos : list) {
            level.updateAround(pos);
        }
    }

    private void calculateCurrentChanges() {
        Vector3 pos = this.getLocation();

        int meta = this.meta;
        int maxStrength = 0;
        maxStrength = this.getMaxCurrentStrength(this, maxStrength);
        this.canProvidePower = false;
        int power = this.level.isBlockIndirectlyGettingPowered(this);
        this.canProvidePower = true;

        if (power > 0 && power > maxStrength - 1) {
            maxStrength = power;
        }

        int strength = 0;

        for (BlockFace face : Plane.HORIZONTAL) {
            Vector3 v = pos.getSide(face);
            boolean flag = v.getX() != this.getX() || v.getZ() != this.getZ();

            if (flag) {
                strength = this.getMaxCurrentStrength(v, strength);
            }

            if (this.level.getBlock(v).isSolid() && !this.level.getBlock(this.up()).isSolid()) {
                if (flag) {
                    strength = this.getMaxCurrentStrength(v.up(), strength);
                }
            } else if (!this.level.getBlock(v).isSolid() && flag) {
                strength = this.getMaxCurrentStrength(v.down(), strength);
            }
        }

        if (strength > maxStrength) {
            maxStrength = strength - 1;
        } else if (maxStrength > 0) {
            --maxStrength;
        } else {
            maxStrength = 0;
        }

        if (power > maxStrength - 1) {
            maxStrength = power;
        }

        if (meta != maxStrength) {
            this.meta = maxStrength;

            this.blocksNeedingUpdate.add(pos);

            for (BlockFace face : BlockFace.values()) {
                this.blocksNeedingUpdate.add(pos.getSide(face));
            }
        }
    }

    private int getMaxCurrentStrength(Vector3 pos, int strength) {
        if (this.level.getBlock(pos).getId() != this.getId()) {
            return strength;
        } else {
            int i = this.meta;
            return i > strength ? i : strength;
        }
    }

    @Override
    public boolean onBreak(Item item) {
        this.getLevel().setBlock(this, new BlockAir(), true, true);

        Vector3 pos = getLocation();

        for (BlockFace blockFace : BlockFace.values()) {
            this.level.updateAround(pos.getSide(blockFace));
        }

        for (BlockFace blockFace : Plane.HORIZONTAL) {
            Vector3 v = pos.getSide(blockFace);

            if (this.level.getBlock(v).isSolid()) {
                this.updateAround(v.up());
            } else {
                this.updateAround(v.down());
            }
        }
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

    @Override
    public int onUpdate(int type) {
        if (type != Level.BLOCK_UPDATE_NORMAL) {
            return 0;
        }

        if (this.canBePlacedAt(this.getLocation().down())) {
            this.updateSurroundingRedstone();
        } else {
            this.getLevel().useBreakOn(this);
        }

        return Level.BLOCK_UPDATE_NORMAL;
    }

    public boolean canBePlacedAt(Vector3 v) {
        Block b = this.level.getBlock(v);

        return b.isSolid() && !b.isTransparent() && b.getId() != Block.GLOWSTONE;
    }

    public int getStrongPower(BlockFace side) {
        return !this.canProvidePower ? 0 : getWeakPower(side);
    }

    public int getWeakPower(BlockFace side) {
        if (!this.canProvidePower) {
            return 0;
        } else {
            int power = this.meta;

            if (power == 0) {
                return 0;
            } else if (side == BlockFace.UP) {
                return power;
            } else {
                EnumSet<BlockFace> enumset = EnumSet.noneOf(BlockFace.class);

                for (BlockFace face : Plane.HORIZONTAL) {
                    if (this.isPowerSourceAt(face)) {
                        enumset.add(face);
                    }
                }

                if (side.getAxis().isHorizontal() && enumset.isEmpty()) {
                    return power;
                } else if (enumset.contains(side) && !enumset.contains(side.rotateYCCW()) && !enumset.contains(side.rotateY())) {
                    return power;
                } else {
                    return 0;
                }
            }
        }
    }

    private boolean isPowerSourceAt(BlockFace side) {
        Vector3 pos = getLocation();
        Vector3 v = pos.getSide(side);
        Block block = this.level.getBlock(v);
        Block block2 = this.level.getBlock(pos.up());
        boolean flag = block.isSolid() && !block.isTransparent();
        boolean flag1 = block2.isSolid() && !block2.isTransparent();
        return !flag1 && flag && canConnectUpwardsTo(this.level, v.up()) || (canConnectTo(block, side) || !flag && canConnectUpwardsTo(this.level, block.down()));
    }

    protected static boolean canConnectUpwardsTo(Level level, Vector3 pos) {
        return canConnectUpwardsTo(level.getBlock(pos));
    }

    protected static boolean canConnectUpwardsTo(Block block) {
        return canConnectTo(block, null);
    }

    protected static boolean canConnectTo(Block block, BlockFace side) {
        if (block.getId() == Block.REDSTONE_WIRE) {
            return true;
        } else if (block.getId() == Block.POWERED_REPEATER || block.getId() == Block.UNPOWERED_REPEATER) {
            //TODO: repeater
            return false;
        } else {
            return block.isPowerSource() && side != null;
        }
    }

    @Override
    public boolean isPowerSource() {
        return this.canProvidePower;
    }
}
