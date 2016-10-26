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

    private int[] map = new int[4096];

    public BiomeSelector(NukkitRandom random, Biome fallback) {
        this.fallback = fallback;
        this.temperature = new Simplex(random, 2.0D, 0.125D, 9.765625E-4D);
        this.rainfall = new Simplex(random, 2.0D, 0.125D, 9.765625E-4D);
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
        } else {
            if (temperature < 0.25) {
                return Biome.TAIGA;
            } else if (temperature < 0.75) {
                return Biome.FOREST;
            } else {
                return Biome.BIRCH_FOREST;
            }
        }
    }

    public void recalculate() {
        this.map = new int[4096];
        for(int i = 0; i < 64; ++i) {
            for(int j = 0; j < 64; ++j) {
                this.map[i + (j << 6)] = this.lookup((double)i / 63.0D, (double)j / 63.0D);
            }
        }
    }

    public void addBiome(Biome biome) {
        this.biomes.put(Integer.valueOf(biome.getId()), biome);
    }

    public double getTemperature(double x, double z) {
        return (this.temperature.noise2D(x, z, true) + 1.0D) / 2.0D;
    }

    public double getRainfall(double x, double z) {
        return (this.rainfall.noise2D(x, z, true) + 1.0D) / 2.0D;
    }

    public Biome pickBiome(double x, double z) {
        int temperature = (int)(this.getTemperature(x, z) * 63.0D);
        int rainfall = (int)(this.getRainfall(x, z) * 63.0D);
        int biomeId = this.map[temperature + (rainfall << 6)];
        return this.biomes.containsKey(Integer.valueOf(biomeId))?(Biome)this.biomes.get(Integer.valueOf(biomeId)):this.fallback;
    }
}
