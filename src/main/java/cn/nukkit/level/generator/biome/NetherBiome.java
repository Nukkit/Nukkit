package cn.nukkit.level.generator.biome;

public abstract class NetherBiome extends Biome {

    @Override
    public int getColor() {
        return this.grassColor;
    }
}
