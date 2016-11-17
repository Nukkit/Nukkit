package cn.nukkit.level.generator.biome;

import cn.nukkit.level.generator.noise.Simplex;
import cn.nukkit.math.NukkitRandom;

import java.util.HashMap;
import java.util.Map;

/**
 * author: MagicDroidX
 * Nukkit Project
 */
public class BiomeSelector {
    private final Biome fallback;
    private final Simplex temperature;
    private final Simplex rainfall;

    private final Map<Integer, Biome> biomes = new HashMap<>();

    private int[] map = new int[128 * 128];

    public BiomeSelector(NukkitRandom random, Biome fallback) {
        this.fallback = fallback;
        this.temperature = new Simplex(random, 4F, 2F / 16F, 2F / 1024F);
        this.rainfall = new Simplex(random, 4F, 2F / 16F, 2F / 1024F);
    }

    public int lookup(double temperature, double rainfall) {
        if (rainfall < 0.25) {
            return Biome.SWAMP;
        } else if (rainfall < 0.60) {
            if (temperature < 0.25) {
                return Biome.ICE_PLAINS;
            } else if (temperature < 0.75) {
                return Biome.PLAINS;
            } else {
                return Biome.DESERT;
            }
        } else if (rainfall < 0.80) {
            if (temperature < 0.25) {
                return Biome.TAIGA;
            } else if (temperature < 0.75) {
                return Biome.FOREST;
            } else {
                return Biome.BIRCH_FOREST;
            }
        } else {
            if (temperature < 0.25) {
                return Biome.MOUNTAINS;
            } else {
                return Biome.SMALL_MOUNTAINS;
            }
        }
    }

    public void recalculate() {
        this.map = new int[128 * 128];
        for (int i = 0; i < 128; ++i) {
            for (int j = 0; j < 128; ++j) {
                this.map[i + (j << 12)] = this.lookup(i / 127d, j / 127d);
            }
        }
    }

    public void addBiome(Biome biome) {
        this.biomes.put(biome.getId(), biome);
    }

    public double getTemperature(double x, double z) {
        return (this.temperature.noise2D(x, z, true) + 2) / 4;
    }

    public double getRainfall(double x, double z) {
        return (this.rainfall.noise2D(x, z, true) + 2) / 4;
    }

    public Biome pickBiome(double x, double z) {
        int temperature = (int) (this.getTemperature(x, z) * 127);
        int rainfall = (int) (this.getRainfall(x, z) * 127);

        int biomeId = this.map[temperature + (rainfall << 12)];
        return this.biomes.containsKey(biomeId) ? this.biomes.get(biomeId) : this.fallback;
    }

}
