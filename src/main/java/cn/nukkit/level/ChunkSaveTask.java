package cn.nukkit.level;

import cn.nukkit.Server;
import cn.nukkit.level.format.FullChunk;
import cn.nukkit.level.format.LevelProvider;

/**
 * Created by Igor on 04.07.2016.
 */
public class ChunkSaveTask implements Runnable {

    LevelProvider provider;
    FullChunk chunk;

    public ChunkSaveTask(LevelProvider provider, FullChunk chunk){
        this.chunk = chunk;
        this.provider = provider;
    }

    public void start(){
        Server.getInstance().getScheduler().scheduleTask(this, true);
    }

    @Override
    public void run() {
        try {
            this.provider.saveChunk(chunk.getX(), chunk.getZ());
            chunk.setChanged(false);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }
}
