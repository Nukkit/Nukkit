package cn.nukkit.level.generator;

import cn.nukkit.level.ChunkManager;
import cn.nukkit.math.NukkitRandom;
import cn.nukkit.math.Vector3;

import java.util.Map;

/**
 * Created by CreeperFace on 8.8.2017.
 */
public class End extends Generator {

    private ChunkManager level;


    @Override
    public ChunkManager getChunkManager() {
        return level;
    }

    @Override
    public String getName() {
        return "Nether";
    }

    @Override
    public void init(ChunkManager level, NukkitRandom random) {

    }

    @Override
    public void populateChunk(int chunkX, int chunkZ) {

    }

    @Override
    public void generateChunk(int chunkX, int chunkZ) {

    }

    @Override
    public int getId() {
        return Generator.TYPE_END;
    }

    @Override
    public Vector3 getSpawn() {
        return new Vector3(0, 256, 0);
    }

    @Override
    public Map<String, Object> getSettings() {
        return null;
    }
}
