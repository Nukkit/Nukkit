package cn.nukkit.utils;

import cn.nukkit.block.Block;
import cn.nukkit.block.redstone.IndirectRedstoneSource;
import cn.nukkit.block.redstone.RedstonePowerMode;
import cn.nukkit.block.redstone.RedstoneSource;
import cn.nukkit.math.Vector3;

/**
 * @author PeratX
 */

public class RedstoneUtil {
    public static final int[] NESWDU = {Vector3.SIDE_NORTH, Vector3.SIDE_EAST, Vector3.SIDE_SOUTH, Vector3.SIDE_WEST, Vector3.SIDE_DOWN, Vector3.SIDE_UP};


    /**
     * Checks if the block given is a redstone conductor
     *
     * @param block to check
     * @return True if it is a redstone conductor
     */
    public static boolean isConductor(Block block) {
        return block instanceof IndirectRedstoneSource && ((IndirectRedstoneSource) block).isRedstoneConductor();
    }

    /**
     * Gets if the given block receives Redstone power or not
     *
     * @param block to get it of
     * @return True if it receives power, False if not
     */
    public static boolean isReceivingPower(Block block) {
        return getReceivingPowerLocation(block) != null;
    }

    /**
     * Gets the block that is powering this block, or null if none
     *
     * @param block to get source of power of
     * @return Source of redstone power, or null if none
     */
    public static Block getReceivingPowerLocation(Block block) {
        for (int face : NESWDU) {
            Block b = block.getSide(face);
            if (isEmittingPower(b, Vector3.getOppositeSide(face))) {
                return b;
            }
        }
        return null;
    }

    /**
     * Gets if the given block is emitting power to surrounding blocks
     *
     * @param block to check
     * @param to the face it is powering
     * @return True if emitting power, False if not
     */
    public static boolean isEmittingPower(Block block, int to) {
        return isEmittingPower(block, to, RedstonePowerMode.ALL);
    }

    /**
     * Gets if the given block is emitting power to surrounding blocks
     *
     * @param block to check
     * @param to the face it is powering
     * @param powerMode to use when reading power
     * @return True if emitting power, False if not
     */
    public static boolean isEmittingPower(Block block, int to, RedstonePowerMode powerMode) {
        if (!(block instanceof IndirectRedstoneSource)) {
            return false;
        }
        // Use direction for direct sources
        if (block instanceof RedstoneSource && ((RedstoneSource) block).hasDirectRedstonePower(block, to, powerMode)) {
            return true;
        }
        // Fall-back to indirect redstone sources
        return ((IndirectRedstoneSource) block).hasIndirectRedstonePower(block, to, powerMode);
    }
}
