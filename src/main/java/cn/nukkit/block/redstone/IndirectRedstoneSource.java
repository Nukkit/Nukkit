package cn.nukkit.block.redstone;

import cn.nukkit.block.Block;

/**
 * Defines a material that can power other redstone target materials, excluding solid blocks<br> Includes methods to obtain the redstone power level of the material itself
 *
 * @author PeratX
 */
public interface IndirectRedstoneSource {

    /**
     * Gets how much redstone power this redstone source block provides to the direction given.<br> This is indirect power, which only powers redstone targets, not other solid blocks
     *
     * @param block of this redstone source
     * @param direction it provides power to
     * @param powerMode to use to get the power
     * @return how much power this block provides to the given direction
     */
    short getIndirectRedstonePower(Block block, int direction, RedstonePowerMode powerMode);

    /**
     * Gets if this redstone source block provides power to the direction given.<br> This is indirect power, which only powers redstone targets, not other solid blocks
     *
     * @param block of this redstone source
     * @param direction it provides power to
     * @param powerMode to use to get the power
     * @return True if this redstone source block provides power
     */
    boolean hasIndirectRedstonePower(Block block, int direction, RedstonePowerMode powerMode);

    /**
     * Gets the power level of this material at a block
     *
     * @param block to get it of
     * @return the redstone power level
     */
    short getRedstonePower(Block block);

    /**
     * Gets the power level of this material at a block
     *
     * @param block to get it of
     * @param powerMode to use to find the power
     * @return the redstone power level
     */
    short getRedstonePower(Block block, RedstonePowerMode powerMode);

    /**
     * Gets if this material is powered at a block
     *
     * @param block to get it of
     * @return True if the block receives power
     */
    boolean hasRedstonePower(Block block);

    /**
     * Gets if this material is powered at a block
     *
     * @param block to get it of
     * @param powerMode to use to find out the power levels
     * @return True if the block receives power
     */
    boolean hasRedstonePower(Block block, RedstonePowerMode powerMode);

    /**
     * Gets whether this material acts as a redstone conductor<br> A redstone conductor can conduct power from direct redstone sources, and power other blocks indirectly
     *
     * @return True if it is a conductor, False if not
     */
    boolean isRedstoneConductor();
}
