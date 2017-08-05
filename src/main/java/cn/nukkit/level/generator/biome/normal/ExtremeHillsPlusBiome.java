package cn.nukkit.level.generator.biome.normal;

import cn.nukkit.level.generator.biome.SmallMountainsBiome;

public class ExtremeHillsPlusBiome extends SmallMountainsBiome {

    public static final int TYPE_NORMAL = 0;
    public static final int TYPE_M = 1;

    public final int type;

    public ExtremeHillsPlusBiome() {
        this(TYPE_NORMAL);
    }

    public ExtremeHillsPlusBiome(int type) {
        super();
        this.type = type;
        this.temperature = 0.2f;
        this.rainfall = 0.3f;

        if (type == TYPE_NORMAL) {
            this.setElevation(63, 100); // Need check
        } else {
            this.setElevation(125, 130); // Need check
        }
    }

    @Override
    public String getName() {
        return type == TYPE_NORMAL ? "Extreme Hills+" : "Extreme Hills+ M";
    }
}
