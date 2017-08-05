package cn.nukkit.level.generator.biome.normal;

import cn.nukkit.level.generator.biome.SnowyBiome;

/**
 * Created by NycuRO on 5.08.2017
 *
 * This biome is a part of SnowyBiome.
 *
 * Info:
 * A river with a layer of ice covering its surface.
 * It represents a separation of two cold biomes, but can also divide single biomes.
 * Frozen rivers would connect to frozen oceans, before the latter was removed.
 * Frozen rivers will not generate where a cold biome meets a warmer biome;
 * Regular rivers will generate instead.
 */
public class FrozenRiverBiome extends SnowyBiome {

    public FronzenRiverBiome() {
        super();
        this.temperature = 0.0f;
        this.rainfall = 0.5f;
        this.setElevation(63, 74);
    }

    public String getName() {
        return "Frozen River";
    }
}
