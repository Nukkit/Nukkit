package cn.nukkit.level.generator.biome.normal;

import cn.nukkit.level.generator.biome.MountainsBiome;

public class ExtremeHillsBiome extends MountainsBiome {

    public static final int TYPE_NORMAL = 0;
    public static final int TYPE_M = 1;

    public final int type;

    public ExtremeHillsBiome() {
        this(TYPE_NORMAL);
    }

    public ExtremeHillsBiome(int type) {
        super();
        this.type = type;
        this.temperature = 0.2f;
        this.rainfall = 0.3f;
        if (type == TYPE_NORMAL) {
            this.setElevation(64, 100); // Need check
        } else {
            this.setElevation(120, 130); // Need check
        }
    }

    public String getName() {
        return type == TYPE_NORMAL ? "Extreme Hills" : "Extreme Hills M";
    }
}
