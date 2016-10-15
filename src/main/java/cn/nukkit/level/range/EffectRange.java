package cn.nukkit.level.range;

import cn.nukkit.math.BlockVector3;

/**
 * Nukkit Project
 */
public interface EffectRange extends Iterable<BlockVector3> {
    public static EffectRange THIS = new CubicEffectRange(0);
    public static EffectRange THIS_AND_BELOW = new CuboidEffectRange(0, -1, 0, 0, 0, 0);
    public static EffectRange THIS_AND_ABOVE = new CuboidEffectRange(0, 0, 0, 0, 1, 0);
    public static EffectRange THIS_AND_NEIGHBORS = new DiamondEffectRange(1);
    public static EffectRange NEIGHBORS = new DiamondEffectRange(1, 1);

    /**
     * Gets an iterator to iterate over all blocks in the effect range.
     *
     * @return an effect iterator
     */
    @Override
    public EffectIterator iterator();

    /**
     * Configures an iterator to iterate over all blocks in the effect range.
     *
     * @return an effect iterator
     */
    public void initEffectIterator(EffectIterator reuse);

    /**
     * Checks if the effect is contained within a Region for the given block position
     *
     * @param x coordinate of the block
     * @param y coordinate of the block
     * @param z coordinate of the block
     * @return true if the range is Region specific
     */
    public boolean isRegionLocal(int x, int y, int z);

    /**
     * Translates this Effect Range with the offset specified<br> Note: the returned type does not have to be the same as this class!
     *
     * @param offset to translate
     * @return a new translated EffectRange instance
     */
    public EffectRange translate(int offset);

    /**
     * Translates this Effect Range with the offset specified<br> Note: the returned type does not have to be the same as this class!
     *
     * @param offset to translate
     * @return a new translated EffectRange instance
     */
    public EffectRange translate(BlockVector3 offset);
}
