package cn.nukkit.level.generator.populator;

import cn.nukkit.block.Block;
import cn.nukkit.level.ChunkManager;
import cn.nukkit.math.BlockVector3;
import cn.nukkit.math.NukkitMath;
import cn.nukkit.math.NukkitRandom;

import java.util.Random;

public class PopulatorCactus extends Populator {

    /**
     * Author: Niall Lindsay <Niall7459>
     */

    private ChunkManager level;
    private int randomAmount;
    private int baseAmount;

    public void setRandomAmount(int randomAmount) {
        this.randomAmount = randomAmount;
    }

    public void setBaseAmount(int baseAmount) {
        this.baseAmount = baseAmount;
    }

    @Override
    public void populate(ChunkManager level, int chunkX, int chunkZ, NukkitRandom random) {
        this.level = level;
        int amount = random.nextBoundedInt(this.randomAmount + 1) + this.baseAmount;
        Random javaRandom = new Random();
        BlockVector3 v = new BlockVector3();
        for (int i = 0; i < amount; ++i) {
            int sourceX = (v.getX() << 4) + javaRandom.nextInt(16);
            int sourceZ = (v.getZ() << 4) + javaRandom.nextInt(16);
            int x = sourceX + javaRandom.nextInt(8) - javaRandom.nextInt(8);
            int z = sourceZ + javaRandom.nextInt(8) - javaRandom.nextInt(8);

            int originalX = NukkitMath.randomRange(random, chunkX * 16, chunkX * 16 + 15);
            int originalZ = NukkitMath.randomRange(random, chunkZ * 16, chunkZ * 16 + 15);
            int y = this.getHighestWorkableBlock(originalX, originalZ);

            if (y != -1 && this.canCactusStay(originalX, y, originalZ)) {
                this.level.setBlockIdAt(x, y, z, Block.CACTUS);
                this.level.setBlockDataAt(x, y, z, 1);
            }
        }
    }

    private boolean canCactusStay(int x, int y, int z) {
        int b = this.level.getBlockIdAt(x, y, z);
        return (b == Block.AIR && this.level.getBlockIdAt(x, y - 1, z) == Block.SAND && this.level.getBlockIdAt(x + 1, y, z) == Block.AIR && this.level.getBlockIdAt(x - 1, y, z) == Block.AIR && this.level.getBlockIdAt(x, y, z + 1) == Block.AIR && this.level.getBlockIdAt(x, y, z - 1) == Block.AIR);
    }

    private int getHighestWorkableBlock(int x, int z) {
        int y;
        for (y = 127; y >= 0; --y) {
            int b = this.level.getBlockIdAt(x, y, z);
            if (b != Block.AIR && b != Block.LEAVES && b != Block.LEAVES2 && b != Block.SNOW_LAYER) {
                break;
            }
        }

        return y == 0 ? -1 : ++y;
    }
}