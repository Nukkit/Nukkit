package cn.nukkit.block;

import cn.nukkit.Player;
import cn.nukkit.block.redstone.*;
import cn.nukkit.item.Item;
import cn.nukkit.level.Level;
import cn.nukkit.level.range.*;
import cn.nukkit.math.Vector3;
import cn.nukkit.utils.BlockColor;
import cn.nukkit.utils.RedstoneUtil;

/**
 * author: Angelic47 & PeratX
 * Nukkit Project
 */
public class BlockRedstoneWire extends BlockFlowable implements RedstoneSource, RedstoneTarget {
    private static final EffectRange physicsRange = new ListEffectRange(
            new PlusEffectRange(2, false),
            new CubicEffectRange(1));

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
    public boolean hasPhysics() {
        return true;
    }

    @Override
    public EffectRange getPhysicsRange() {
        return physicsRange;
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


    private void disableRedstone(Block middle) {
        Block block;
        for (int face : RedstoneUtil.NESWDU) {
            block = middle.getSide(face);
            if (block.getId() == this.getId()) {
                if (block.getDamage() > 0) {
                    block.setDamage(0);
                    this.getLevel().setBlock(this, this, false, true);
                    this.disableRedstone(block);
                }
            }
        }
    }

    @Override
    public int onUpdate(int type) {
        if (type == Level.BLOCK_UPDATE_NORMAL) {
            if (this.getSide(Vector3.SIDE_DOWN).isTransparent()) {
                this.getLevel().useBreakOn(this);
                return Level.BLOCK_UPDATE_NORMAL;
            }
            short receiving = this.getReceivingPower();
            short current = this.getRedstonePower(this);
            //DEBUG
            System.out.println(receiving);
            System.out.println(current);

            if (current == receiving) {

            } else if (receiving > current) {
                //Power became more, perform simple updating
                this.setDamage((int) receiving);
                this.getLevel().setBlock(this, this, false, true);
            } else {
                //Power became less, disable all attached wires and recalculate
                this.disableRedstone(this);
                for (int face : RedstoneUtil.NESWD) {
                    this.disableRedstone(this.getSide(face));
                }
            }
        }
        return type;
    }

    @Override
    public short getIndirectRedstonePower(Block block, int direction, RedstonePowerMode powerMode) {
        return 0;
    }

    @Override
    public boolean hasDirectRedstonePower(Block block, int direction, RedstonePowerMode powerMode) {
        return this.getDirectRedstonePower(block, direction, powerMode) > 0;
    }

    @Override
    public short getDirectRedstonePower(Block block, int direction, RedstonePowerMode powerMode) {
        if (powerMode == RedstonePowerMode.ALLEXCEPTWIRE) {
            return REDSTONE_POWER_MIN;
        }

        short power = this.getRedstonePower(block);
        if (power == REDSTONE_POWER_MIN) {
            return power;
        }

        Block mat = block.getSide(direction);
        if (mat instanceof RedstoneSource || !isDistractedFrom(block, direction)) {
            return power;
        }

        return REDSTONE_POWER_MIN;
    }

    @Override
    public short getRedstonePower(Block block, RedstonePowerMode powerMode) {
        return powerMode == RedstonePowerMode.ALLEXCEPTWIRE ? REDSTONE_POWER_MIN : (short) this.getDamage();
    }

    @Override
    public boolean isReceivingPower(Block block) {
        return this.getReceivingPower() > 0;
    }

    public short getReceivingPower() {
        short maxPower = 0;
        Block rel, relvert;
        //detect power from direct neighbouring sources
        boolean topIsConductor = false;
        for (int face : RedstoneUtil.DUEWNS) {
            rel = this.getSide(face);
            if (rel.getId() == this.getId()) {
                //handle neighbouring redstone wires
                maxPower = (short) Math.max(maxPower, this.getRedstonePower(rel) - 1);
            } else {
                //handle solid blocks and redstone sources
                maxPower = (short) Math.max(maxPower, rel.getRedstonePower(rel, RedstonePowerMode.ALLEXCEPTWIRE));
                if (rel instanceof RedstoneSource) {
                    maxPower = (short) Math.max(maxPower, ((RedstoneSource) rel).getDirectRedstonePower(rel, Vector3.getOppositeSide(face), RedstonePowerMode.ALL));
                }
            }
            //shortcut just in case the answer is simple
            if (maxPower == REDSTONE_POWER_MAX) {
                return maxPower;
            }
            //check relatively up and down faces
            if (face == SIDE_UP) {
                topIsConductor = RedstoneUtil.isConductor(rel);
            } else if (face != SIDE_DOWN) {
                //check below for wire
                if (!RedstoneUtil.isConductor(rel)) {
                    relvert = rel.getSide(SIDE_DOWN);
                    if (relvert.getId() == this.getId()) {
                        maxPower = (short) Math.max(maxPower, this.getRedstonePower(relvert) - 1);
                    }
                }
                //check above for wire
                if (!topIsConductor) {
                    relvert = rel.getSide(SIDE_UP);
                    if (relvert.getId() == this.getId()) {
                        maxPower = (short) Math.max(maxPower, this.getRedstonePower(relvert) - 1);
                    }
                }
            }
        }
        return maxPower;
    }

    /**
     * Checks if a redstone wire is connected to a certain block<br> No solid block connections are checked.
     *
     * @param block of the wire
     * @param face  to connect to
     * @return True if connected
     */
    public boolean isConnectedToSource(Block block, int face) {
        Block target = block.getSide(face);
        //check below
        if (!RedstoneUtil.isConductor(block)) {
            if (target.getSide(SIDE_DOWN) instanceof RedstoneSource) {
                return true;
            }
        }
        //check above
        if (target.getSide(SIDE_UP) instanceof RedstoneSource) {
            if (!RedstoneUtil.isConductor(block.getSide(SIDE_UP))) {
                return true;
            }
        }
        return false;
    }

    /**
     * Checks if this wire is distracted from connecting to a certain solid block
     *
     * @param block     of the wire
     * @param direction it tries to connect to a solid block
     * @return True if the wire is distracted
     */
    public boolean isDistractedFrom(Block block, int direction) {
        switch (direction) {
            case SIDE_NORTH:
            case SIDE_SOUTH:
                return this.isConnectedToSource(block, SIDE_EAST) || this.isConnectedToSource(block, SIDE_WEST);
            case SIDE_EAST:
            case SIDE_WEST:
                return this.isConnectedToSource(block, SIDE_NORTH) || this.isConnectedToSource(block, SIDE_SOUTH);
            default:
                return false;
        }
    }

    @Override
    public BlockColor getColor() {
        return BlockColor.AIR_BLOCK_COLOR;
    }
}
