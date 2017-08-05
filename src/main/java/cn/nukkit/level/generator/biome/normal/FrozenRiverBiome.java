package cn.nukkit.level.generator.biome.normal;

import cn.nukkit.level.generator.biome.SnowyBiome;

public class FrozenRiverBiome extends SnowyBiome {

    public FrozenRiverBiome() {
        super();
        this.setElevation(63, 74);
        this.temperature = 0.0f;
        this.rainfall = 0.5f;
    }

    @Override
    public String getName() {
        return "Frozen River";
    }
}
