package cn.nukkit.level.generator.biome.normal;

import cn.nukkit.level.generator.biome.SmallMountainsBiome;

public class ExtremeHillsPlus extends SmallMountainsBiome {

    public static final int TYPE_NORMAL = 0;
    public static final int TYPE_M = 1;

    public final int type;

    public ExtremeHillsPlus() {
        this(TYPE_NORMAL);
    }

    public ExtremeHillsPlus(int type) {
        super();
        this.type = type;
        this.temperature = 0.2f;
        this.rainfall = 0.3f;

        if (type == TYPE_NORMAL) {
            this.setElevation(63, 100);
        } else {
            this.setElevation(63, 130);
        }
    }

    @Override
    public String getName() {
        return type == TYPE_NORMAL ? "Extreme Hills+" : "Extreme Hills+ M";
    }
}
