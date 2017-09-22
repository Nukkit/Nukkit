package cn.nukkit.level.generator.biome.normal;

import cn.nukkit.level.generator.biome.NormalBiome;

public class VoidBiome extends NormalBiome {

    public VoidBiome() {
        this.temperature = 0.8f;
        this.rainfall = 0.4f;
    }

    @Override
    public String getName() {
        return "The Void";
    }
}
