package cn.nukkit.level.generator.task;

import cn.nukkit.Server;
import cn.nukkit.level.Level;
import cn.nukkit.level.format.generic.BaseFullChunk;
import cn.nukkit.level.generator.biome.Biome;
import cn.nukkit.scheduler.AsyncTask;

/**
 * author: MagicDroidX
 * Nukkit Project
 */
public class LightPopulationTask extends AsyncTask {

    public int levelId;
    public BaseFullChunk chunk;

    public LightPopulationTask(Level level, BaseFullChunk chunk) {
        this.levelId = level.getId();
        this.chunk = chunk;
    }

    @Override
    public void onRun() {
        BaseFullChunk chunk = this.chunk.clone();
        if (chunk == null) {
            return;
        }

        chunk.recalculateHeightMap();
        chunk.populateSkyLight();
        chunk.setLightPopulated();

        for(int x = 0; x < 16; ++ x){
		for(int z = 0; z < 16; ++ z){
		        int[] blockColor = chunk.getBiomeColor(x, z);
		        if(blockColor[0] == 0 && blockColor[1] == 0){
		        	int biomeColor = Biome.getBiome(chunk.getBiomeId(x, z)).getColor();
		        	chunk.setBiomeColor(x, z, (biomeColor >> 16), (biomeColor >> 8) & 0xff, (biomeColor & 0xff));
		        }		        	
		}
        }

        this.chunk = chunk.clone();
    }

    @Override
    public void onCompletion(Server server) {
        Level level = server.getLevel(this.levelId);

        BaseFullChunk chunk = this.chunk.clone();
        if (level != null) {
            if (chunk == null) {
                return;
            }

            level.generateChunkCallback(chunk.getX(), chunk.getZ(), chunk);
        }
    }
}
