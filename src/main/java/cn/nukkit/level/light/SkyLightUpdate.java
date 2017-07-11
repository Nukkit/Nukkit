package cn.nukkit.level.light;

import cn.nukkit.level.ChunkManager;

/**
 * author: dktapps
 */
public class SkyLightUpdate extends LightUpdate {

    public SkyLightUpdate(ChunkManager level) {
        super(level);
    }

    public int getLight(int x, int y, int z) {
        return this.level.getBlockSkyLightAt(x, y, z);
    }

    public void setLight(int x, int y, int z, int level) {
        this.level.setBlockSkyLightAt(x, y, z, level);
    }
}