package cn.nukkit.level.light;

import cn.nukkit.level.ChunkManager;

/**
 * author: dktapps
 */
public class BlockLightUpdate extends LightUpdate {

    public BlockLightUpdate(ChunkManager level) {
        super(level);
    }

    @Override
    public int getLight(int x, int y, int z) {
        return this.level.getBlockLightAt(x, y, z);
    }

    @Override
    public void setLight(int x, int y, int z, int level) {
        this.level.setBlockLightAt(x, y, z, level);
    }
}