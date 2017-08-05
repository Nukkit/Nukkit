package cn.nukkit.level.generator.biome;

public abstract class NetherBiome extends Biome {

    @Override
    public float setWaterColor() {
        return this.waterColor;
    }

    @Override
    public int getColor() {
        return this.grassColor;
    }
}
