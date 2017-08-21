package cn.nukkit.level.generator.biome.normal;

import cn.nukkit.level.generator.biome.SandyBiome;
import cn.nukkit.level.generator.populator.PopulatorCactus;

/**
 * author: MagicDroidX
 * Nukkit Project
 */
public class DesertBiome extends SandyBiome {

    public static final int TYPE_NORMAL = 0;
    public static final int TYPE_M = 1;

    public final int type;

    public DesertBiome() {
        this(TYPE_NORMAL);
    }

    public DesertBiome(int type) {
        super();
        this.type = type;
        this.temperature = 2.0f;
        this.rainfall = 0.0f;
        if (type == TYPE_NORMAL) {
            this.setElevation(63, 69);
            PopulatorCactus populatorCactus = new PopulatorCactus();
            populatorCactus.setBaseAmount(10);
            this.addPopulator(populatorCactus);
        } else {
            this.setElevation(63, 130);
        }
    }

    @Override
    public String getName() {
        return type == TYPE_NORMAL ? "Desert" : "Desert M";
    }
}
