package cn.nukkit.level.generator.populator;

import cn.nukkit.level.ChunkManager;
import cn.nukkit.math.NukkitRandom;

/**
 * author: MagicDroidX
 * Nukkit Project
 */
public abstract class Populator {
    public abstract void populate(ChunkManager level, int ChunkX, int ChunkZ, NukkitRandom random);
}
