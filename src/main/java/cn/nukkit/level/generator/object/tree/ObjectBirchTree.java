package cn.nukkit.level.generator.object.tree;

import cn.nukkit.block.Block;
import cn.nukkit.block.BlockWood;
import cn.nukkit.level.ChunkManager;
import cn.nukkit.math.NukkitRandom;

import java.util.Random;

/**
 * author: MagicDroidX
 * Nukkit Project
 */
public class ObjectBirchTree extends ObjectTree {
    
    /*Random random = new Random();
    protected int treeHeight = random.nextInt(8 - 6 + 1) + 8;*/

    @Override
    public int getTrunkBlock() {
        return Block.LOG;
    }

    @Override
    public int getLeafBlock() {
        return Block.LEAVES;
    }

    @Override
    public int getType() {
        return BlockWood.BIRCH;
    }

    @Override
    public int getTreeHeight() {
        return this.treeHeight;
    }

    @Override
    public void placeObject(ChunkManager level, int x, int y, int z, NukkitRandom random) {
        //this.treeHeight = random.nextBoundedInt(3) + 5;
        super.placeObject(level, x, y, z, random);
    }
}
