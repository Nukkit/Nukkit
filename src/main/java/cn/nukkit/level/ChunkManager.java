package cn.nukkit.level;

import cn.nukkit.level.format.generic.BaseFullChunk;

/**
 * author: MagicDroidX
 * Nukkit Project
 */
public interface ChunkManager {

    int getBlockIdAt(int x, int y, int z);

    void setBlockIdAt(int x, int y, int z, int id);

    int getBlockDataAt(int x, int y, int z);

    void setBlockDataAt(int x, int y, int z, int data);

    int getBlockLightAt(int x, int y, int z);

    void setBlockLightAt(int x, int y, int z, int level);

    int getBlockSkyLightAt(int x, int y, int z);

    void setBlockSkyLightAt(int x, int y, int z, int level);

    BaseFullChunk getChunk(int chunkX, int chunkZ);

    void setChunk(int chunkX, int chunkZ);

    void setChunk(int chunkX, int chunkZ, BaseFullChunk chunk);

    int getHeightMap(int x, int z);

    void setHeightMap(int x, int z, int value);

    long getSeed();
}
