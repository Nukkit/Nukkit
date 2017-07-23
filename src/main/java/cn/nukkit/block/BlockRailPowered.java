package cn.nukkit.block;

import cn.nukkit.level.Level;
import cn.nukkit.math.Vector3;
import cn.nukkit.utils.Rail;

/**
 * Created by Snake1999 on 2016/1/11. 
 * Contributed by: larryTheCoder on 2017/7/18.
 * <p>
 * Nukkit Project,
 * Minecart and Riding Project,
 * Package cn.nukkit.block in project Nukkit.
 */
public class BlockRailPowered extends BlockRail {

    public BlockRailPowered() {
        this(0);
        canBePowered = true;
    }

    public BlockRailPowered(int meta) {
        super(meta);
    }

    @Override
    public int getId() {
        return POWERED_RAIL;
    }

    @Override
    public String getName() {
        return "Powered Rail";
    }

    @Override
    public int onUpdate(int type) {
        if (type == Level.BLOCK_UPDATE_NORMAL || type == Level.BLOCK_UPDATE_SCHEDULED) {
            super.onUpdate(type);
            boolean wasPowered = isActive();
            boolean isPowered = level.isBlockPowered(this)
                    || checkSurrounding(this, true, 0)
                    || checkSurrounding(this, false, 0);
            boolean hasUpdate = false;

            if (isPowered && !wasPowered) {
                setActive(true);
                hasUpdate = true;
            } else if (!isPowered && wasPowered) {
                setActive(false);
                hasUpdate = true;
            }

            if (hasUpdate) {
                level.updateAround(down());
                if (getOrientation().isAscending()) {
                    level.updateAround(up());
                }
            }
            return type;
        }
        return 0;
    }

    /**
     * Check the surrounding of the rail
     *
     * @param pos The rail position
     * @param relative The relative of the rail that will be checked
     * @param power The count of the rail that had been counted
     * @return Boolean of the surrounding area. Where the powered rail on!
     */
    protected boolean checkSurrounding(Vector3 pos, boolean relative, int power) {
        // The powered rail can power up to 8 blocks only
        if (power >= 8) {
            return false;
        }
        // The position of the floor numbers
        int dx = pos.getFloorX();
        int dy = pos.getFloorY();
        int dz = pos.getFloorZ();
        // First: get the base block
        BlockRail block;
        Block block2 = level.getBlock(new Vector3(dx, dy, dz));

        // Second: check if the rail is Powered rail
        if (Rail.isRailBlock(block2)) {
            block = (BlockRail) block2;
        } else {
            return false;
        }
        
        // Used to check if the next ascending rail should be what
        Rail.Orientation base = null;
        // Third: Recalculate the base position
        switch (block.getOrientation()) {
            case STRAIGHT_NORTH_SOUTH:
                if (relative) {
                    dz++;
                } else {
                    dz--;
                }
                break;
            case STRAIGHT_EAST_WEST:
                if (relative) {
                    dx--;
                } else {
                    dx++;
                }
                break;
            case ASCENDING_EAST:
                if (relative) {
                    dx--;
                } else {
                    dx++;
                    dy++;
                }
                base = Rail.Orientation.STRAIGHT_EAST_WEST;
                break;
            case ASCENDING_WEST:
                if (relative) {
                    dx--;
                    dy++;
                } else {
                    dx++;
                }
                base = Rail.Orientation.STRAIGHT_EAST_WEST;
                break;
            case ASCENDING_NORTH:
                if (relative) {
                    dz++;
                } else {
                    dz--;
                    dy++;
                }
                base = Rail.Orientation.STRAIGHT_NORTH_SOUTH;
                break;
            case ASCENDING_SOUTH:
                if (relative) {
                    dz++;
                    dy++;
                } else {
                    dz--;
                }
                base = Rail.Orientation.STRAIGHT_NORTH_SOUTH;
       } 
        // Next check the if rail is on power state
        return canPowered(new Vector3(dx, dy, dz), base, power, relative);
    }

    protected boolean canPowered(Vector3 pos, Rail.Orientation state, int power, boolean relative) {
        Block block = level.getBlock(pos);
        // What! My block is air??!! Impossible! XD
        if (!(block instanceof BlockRailPowered)) {
            return false;
        }

        // Sometimes the rails are diffrent orientation
        Rail.Orientation base = ((BlockRailPowered) block).getOrientation();

        // Possible way how to know when the rail is activated is rail were directly powered
        // OR recheck the surrounding... Which will returns here =w=        
        return (state != Rail.Orientation.STRAIGHT_EAST_WEST 
                || base != Rail.Orientation.STRAIGHT_NORTH_SOUTH 
                && base != Rail.Orientation.ASCENDING_NORTH 
                && base != Rail.Orientation.ASCENDING_SOUTH) 
                && (state != Rail.Orientation.STRAIGHT_NORTH_SOUTH 
                || base != Rail.Orientation.STRAIGHT_EAST_WEST 
                && base != Rail.Orientation.ASCENDING_EAST 
                && base != Rail.Orientation.ASCENDING_WEST) 
                && (level.isBlockPowered(pos) || checkSurrounding(pos, relative, power + 1));
    }

}
