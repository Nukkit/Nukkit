package cn.nukkit.block.redstoneBehavior;

import cn.nukkit.block.Block;


/**
 * Created by NycuRO on 07.08.2017
 * Credits: Steadfast2
 */
public abstract class RedstoneComponent extends Block {

    public RedstoneComponent() {
        this(0);
    }

    public RedstoneComponent(int meta) {
        super(0);
    }

    int[] REDSTONE_BLOCK = {
            Block.REDSTONE_WIRE,
            Block.UNLIT_REDSTONE_TORCH,
            Block.REDSTONE_TORCH
    };

    public static final int REDSTONE_POWER_MIN = 0;
    public static final int REDSTONE_POWER_MAX = 15;

    protected int[] neighbors = {};

    protected abstract boolean isSuitableBlock(Integer blockId, Integer direction);

    protected abstract boolean updateNeighbors();

    protected abstract boolean redstoneUpdater(boolean power);
}