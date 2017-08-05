package cn.nukkit.level.generator.biome;

/**
 * author: MagicDroidX
 * Nukkit Project
 */
public abstract class NormalBiome extends Biome {

    @Override
    public int getColor() {
        return this.grassColor;
    }

    @Override
    public int setWaterColor() {
        return this.waterColor;
    }
}
