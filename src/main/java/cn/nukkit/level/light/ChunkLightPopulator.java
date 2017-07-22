package cn.nukkit.level.light;

import cn.nukkit.level.ChunkManager;
import cn.nukkit.level.format.generic.BaseFullChunk;

/**
 * Created by CreeperFace on 22.7.2017.
 */
public class ChunkLightPopulator {

    protected ChunkManager level;

    protected int chunkX;
    protected int chunkZ;

    protected LightUpdate blockLightUpdates = null;
    protected LightUpdate skyLightUpdates = null;

    public ChunkLightPopulator(ChunkManager level, int chunkX, int chunkZ) {
        this.level = level;
        this.chunkX = chunkX;
        this.chunkZ = chunkZ;

        this.blockLightUpdates = new BlockLightUpdate(level);
        this.skyLightUpdates = new SkyLightUpdate(level);
    }

    public void populate() {
        BaseFullChunk chunk = this.level.getChunk(this.chunkX, this.chunkZ);


    }
}
