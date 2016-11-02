package cn.nukkit.block.redstone;

import cn.nukkit.block.Block;

/**
 * Represents a target for redstone power, wire does not attach to this material
 *
 * @author PeratX
 */
public interface RedstoneTarget {
    /**
     * Checks if a block is receiving power from neighboring blocks.
     *
     * @param block to check
     * @return True if the block is receiving power
     */
    public boolean isReceivingPower(Block block);
}
