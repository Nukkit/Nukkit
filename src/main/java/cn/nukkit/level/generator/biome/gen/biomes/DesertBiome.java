package cn.nukkit.level.generator.biome;

/**
 * author: MagicDroidX
 * Nukkit Project
 */
public class DesertBiome extends SandyBiome {
    public DesertBiome() {
        super();
        this.setElevation(63, 74);
        this.temperature = 2.0f;
        this.rainfall = 0.0f;
    }

    @Override
    public String getName() {
        return "Desert";
    }
}
