package cn.nukkit.block;

import cn.nukkit.item.Item;

/**
 * Created by Pub4Game on 30.03.2016.
 */
public class BlockLitRedstoneLamp extends BlockRedstoneLamp {

    public BlockLitRedstoneLamp(int meta) {
        super(meta);
    }

    public BlockLitRedstoneLamp() {
        this(0);
    }

    @Override
    public String getName() {
        return "Lit Redstone Lamp";
    }

    @Override
    public int getId() {
        return LIT_REDSTONE_LAMP;
    }

    @Override
    public int getLightLevel() {
        return 15;
    }

    @Override
    public int onUpdate(int type) {
        return 0;
    }

    @Override
    public int[][] getDrops(Item item) {
        return new int[][]{
                {Item.REDSTONE_LAMP, 0, 1}
        };
    }
}
