package cn.nukkit.block.redstone;

import cn.nukkit.block.Block;

/**
 * Defines a material that can supply redstone power to targets and solid blocks<br> Redstone wire will automatically attach to this material
 *
 * @author PeratX
 */
public interface RedstoneSource extends IndirectRedstoneSource {
    /**
     * Gets how much redstone power this redstone source block provides to the direction given.<br> This is direct power, which can power other solid blocks
     *
     * @param block of this redstone source
     * @param direction it provides power to
     * @param powerMode to use to get the power
     * @return how much power this block provides to the given direction
     */
    short getDirectRedstonePower(Block block, int direction, RedstonePowerMode powerMode);

    /**
     * Gets if this redstone source block provides power to the direction given.<br> This is direct power, which can power other solid blocks
     *
     * @param block of this redstone source
     * @param direction it provides power to
     * @param powerMode to use to get the power
     * @return True if this redstone source block provides power
     */
    boolean hasDirectRedstonePower(Block block, int direction, RedstonePowerMode powerMode);
}

